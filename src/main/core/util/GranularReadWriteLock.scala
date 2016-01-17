package freight
package core
package util


import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.LockSupport
import java.util.concurrent._

/*
 import freight._
 import freight.core.util.GranularReadWriteLock
 GranularReadWriteLock.test
 */


/** A granular lock.
  *
  * The lock is granular, i.e. it locks depending on a resource id.
  * The resource id is an artitary `Long` supplied by calling code.
  *
  * Resource ids should refer to resources with no coupling or
  * interaction, otherwise the lock will not work as
  * intended. Examples of objects identified by a resource id might be
  * data objects or files. The objects can be part of an overall
  * object, as long as the lock is negociating access to uncoupled
  * parts.
  *
  * The lock is strictly sequential (sometimes refered to in Java as
  * `fair`). As much as possible in Java, access is granted in the
  * order access was requested.
  *
  * The lock guarentees it will block access when writing. If readers
  * are reading, writes will block until the readers finish. If
  * readers follow a writer they will be blocked until the writer is
  * finished. Multiple writers queue one behind the other, each
  * triggering the next.
  *
  * A method pair, `enterForAll` and `exitForAll`, exists for overall
  * locking.
  *
  * The lock is reentrant, ie. after entering a resource, a thread can
  * enter another resource, without exiting the first.
  *
  * The lock can not be interrupted. Use the methods `blockUntilDie`
  * and `provoke`.
  *
  * These locks are complex and expensive to run. They are used for
  * database records, for example, where the cost of locking is more
  * than offset by the ability of multiple threads to read records
  * concurrently.
  */
// Plus, this implementation is not savvy, and uses a lock to control
// access. But I have no examples to work from R.C.
// unless this: https://github.com/pveentjer/Multiverse
//TODO: Interrupt protection, untested, remove max params
final class GranularReadWriteLock(
  val maxReaders : Int,
  val maxWriters: Int
)
{
  // NB: The lock keeps resource ids on a hash. Parked items are stored
  // under the hash.
  // The hash data is removed when an item is unparked.
  //
  // Unparked readers are reference counted, so the class knows when
  // it is safe to release a writer.
  //
  // The presence of a writer, parked or not, is always noted, so
  // readers and other writes are parked and not released.
  //

  /** Overall count of current actions.
    */
  //private var liveAccesses: Int = 0
  
  /** Track by thread id.
    */
  private final class TrackedThread()
  {

    /** References the resources requested by this thread.
      *
      * Every access loads a resource id. So each resource can appear
      * more than once, of the thread calls repeatedly without exiting
      * (re-entrant).
      *
      * Each reference is stored in strict order of access. Since the
      * thread can only access/exit one block at a time, `exit` can
      * blindly `pop` the head, guessing (without a reference) that
      * the contained reference id must be the one the thread has
      * recently finished processing.
      *
      * Since every access loads a resource id, this structure doubles
      * as a reference count.
      */
    var resourceIds = scala.collection.mutable.Stack.empty[Long]


    override def toString() = {
      val b = new StringBuilder()
      b ++= "TrackedThread("
      b ++= "resourceIds:"
      b append resourceIds
      b ++= ")"
      b.result()
    }
  }

  /** Track by resource id.
    */
  private final class Resource()
  {
    var actionCount: Int = 0
    val parked = scala.collection.mutable.Stack.empty[ParkedThreadData]

    var isParking = false


    override def toString() = {
      val b = new StringBuilder()
      b ++= "Resource("
      b ++= "actionCount: "
      b append actionCount
      b ++= ", isParking: "
      b append isParking
      b ++= ", parked: "
      b append parked
      b ++= ")"
      b.result()
    }
  }


  /** Track and store parked threads.
   */
  private final class ParkedThreadData(
    val th: Thread,
    val isWrite: Boolean
  )
  {
    override def toString() = {
      val b = new StringBuilder()
      b ++= "ParkedThreadData("
      b ++= "id:"
      b append th.getId()
      b ++= ", isWrite: "
      b append isWrite
      b ++= ")"
      b.result()
    }
  }

  /** Lock for this class.
    *
    * (not incoming threads, they are parked into
    * ParkedThreadData instances)
    */
  private final var door: ReentrantLock  = new ReentrantLock()

  /** Overall lock.
    *
    */
  // NB: Only switches on and off, so if it's volatile/visible that's ok.
  @volatile
  private var outerLock = false

  /** Information about each thread in the lock.
    *
    * The thread information is maintained while a job is both parked
    * and active, and is removed only when a thread exits.
    *
    * ThreadId -> (ResourceId, isWriter)
    */
  private val threadIdMap = scala.collection.mutable.Map[Long, TrackedThread]()

  /** Track progresss by resource id.
    */
  private val resourceMap = scala.collection.mutable.Map[Long, Resource]()




  private def getOrCreateResource(id: Long)
      : Resource =
  {
    val resourceO = resourceMap.get(id)

    if (resourceO != None) resourceO.get
    else {
      val newResource = new Resource()
      resourceMap += (id -> newResource)
      newResource
    }
  }

  private def getOrCreateTrackedThread(threadId: Long)
      : TrackedThread =
  {
    val tThreadO = threadIdMap.get(threadId)

    if (tThreadO != None) tThreadO.get
    else {
      val newTThread = new TrackedThread()
      threadIdMap += (threadId -> newTThread)
      newTThread
    }
  }


  /** Parks a thread.
    */
  private def park(th: Thread) {
    var id = th.getId()
    var cont = false
    while(!cont) {
      LockSupport.park(th)
      // If it's interrupted, park
      cont = !Thread.interrupted()
    }
  }

  /** Empties approriate parked threads from a resource queue.
    *
    * If the method finds a writer, only that is released (and only if
    * no readers are current). Otherwise, it unparks readers until if
    * finds the stack end, or a writer.
    *
    * The method also sets `isParking` to `false`, if parking is
    * exhausted.
    *
    * Must be called in lock. The resource park is protected against
    * empty.
    */
  private def unpark(resource: Resource)
  {
    val parked = resource.parked
    elWarningIf(
      parked.isEmpty,
      s"Unpark called on empty resource $resource",
      "core.util.GranularReadWriteLock"
    )
    if (!parked.isEmpty) {

      var pt = resource.parked.head
      if (pt.isWrite) {
        // only go if no live actions
        // NB: more readers will come along to trigger the
        // write release, if some are live
        if((resource.actionCount - parked.size) < 1) {
          resource.parked.pop()
          LockSupport.unpark(pt.th)
          // continue parking...
        }
      }
      else {
        // Must have a reader on top.
        // Release that and any following too
        var i = 0
        val sz = parked.size
        while (i < sz && !pt.isWrite) {
          resource.parked.pop()
          LockSupport.unpark(pt.th)
          i += 1
        }
        // if empty, no need to park
        resource.isParking = (i != sz)
      }
    }
  }

  /** Traverses parked threads, releasing initial access requests.
    *
    * Most code can function without calling this method, but it may
    * be of interest for rescue and shutdown operations.
    *
    * Must be called in lock.
    */
  def provoke() {
    // flush first
    // check for thread data vs resource
    // unpark top layer, see if that does it...
    resourceMap.foreach { case(resourceId, resource) =>
      unpark(resource)
    }
  }


  /** Blocks until activity dies.
    *
    * Very soft, polling every 1/8 sec until clear.
    *
    * @param timeout millisecs (x*1000 for a sec) to wait until giving up.
    * @return true if activity died, else false.
    */
  def blockUntilDie(timeout: Int)
      : Boolean =
  {
    var i = 0
    var isEmpty = false
    val limit =
      if (timeout > 125) timeout/125
      else 125

    while(!isEmpty && i < limit) {
      door.lock()
      isEmpty = (None == resourceMap.find { case(resourceId, resource) =>
        (resource.actionCount - resource.parked.size) < 1
      })
      door.unlock()
      // 1/8 sec
      Thread.sleep(125)
      i += 1
    }

    elWarningIf(
      !isEmpty,
      s"blockUntilDie timeout: ${timeout/1000}ms",
      "util.GranularReadWriteLock"
    )

    isEmpty
  }

  /** Blocks until activity dies.
    *
    * Very soft, polling every 1/8 sec until clear.
    *
    * @return true if activity died, else false.
    */
  def blockUntilDie()
      : Boolean =
  {
    blockUntilDie(Int.MaxValue)
  }

  /** Enter this lock, enuring no other thread has access.
    *
    * Will block until current activity ceases.
    */
  def enterForAll() {
    outerLock = true
    if(!blockUntilDie()) {
      error("failed to gain overall lock")
    }
  }

  /** Enter this lock, enuring no other thread has access.
    *
    * Will block until current activity ceases.
    */
  def exitForAll() {
    outerLock = false
    provoke()
  }

  /** Enter this lock intending to read.
    *
    * The thread will possibly be queued, depending on the conditions
    * of queueing on the resourceId. Reader threads will queue only
    * when a writer is present on the given resourceId.
    */
  def enterForRead(resourceId: Long) {
    //errorIf((rCount > maxReaders), "Maximum lock count exceeded")

    val th: Thread = Thread.currentThread()
    val threadId = th.getId()

    //log(s"enterForRead resourceId:${resourceId} threadId:${threadId}")
    door.lock()

    val tThread : TrackedThread = getOrCreateTrackedThread(threadId)
    tThread.resourceIds.push(resourceId)

    val resource = getOrCreateResource(resourceId)
    resource.actionCount += 1

    val doPark = (resource.isParking || outerLock)
    if (doPark) {
      resource.parked.push(new ParkedThreadData(th, isWrite = false))
    }

    door.unlock()

    // No disaster if this races,
    // "unpark - its next call to park is guaranteed not to block."
    if (doPark) park(th)
  }

  /** Enter this lock intending to write.
    *
    * The thread will possibly be queued, depending on the conditions
    * of queueing on the resourceId. Writer threads will queue unless
    * no readers or writers are present on the given resourceId.
    */
  def enterForWrite(resourceId: Long) = {
    //errorIf((wCount > maxWriters), "Maximum lock count exceeded")

    val th: Thread = Thread.currentThread()
    val threadId = th.getId()

    //log(s"enterForWrite resourceId:${resourceId} threadId:${threadId }")
    door.lock()

    val resource = getOrCreateResource(resourceId)
    resource.actionCount += 1
    // NB: new resources do not park
    val doPark = (resource.isParking || outerLock)
    if (doPark) {
      resource.parked.push(new ParkedThreadData(th, isWrite = true))
    }
    // writes always cause subsequent parking
    resource.isParking = true

    val tThread : TrackedThread = getOrCreateTrackedThread(threadId)
    tThread.resourceIds.push(resourceId)

    door.unlock()
    // No disaster if this races,
    // "unpark - its next call to park is guaranteed not to block."
    if (doPark) park(th)
  }


  /** Exit the lock.
    *
    * Possibly triggers other threads to enter, depending on the
    * conditions of access request queueing. May also allow the
    * calling thread to progress, if the thread has made re-entrant
    * calls on the lock.
    */
  def exit() {
    val th : Thread = Thread.currentThread()
    val threadId = th.getId()

    door.lock()
    // remove from the thread map...
    // TODO: The following should always work, we put
    // it there. However... interrupts?
    //println(s"threadId $threadId exits")

    //log(s"exit  th id:$threadId")
    elWarningIf(
      !threadIdMap.contains(threadId),
      s"Unrecognised threadId:$threadId in\n$threadIdMap",
      "core.util.GranularReadWriteLock"
    )
    val tThread : TrackedThread = threadIdMap(threadId)
    // adjust reference count
    val resourceId = tThread.resourceIds.pop()

    // if empty, remove the thread data
    if (tThread.resourceIds.isEmpty) {
      threadIdMap.remove(threadId)
    }

    val resource = resourceMap(resourceId)
    // adjust reference count
    resource.actionCount -= 1

    // if empty, remove the resource, else provoke the resource to unpark
    // more threads
    if (resource.actionCount < 1) {
      resourceMap.remove(resourceId)
    }
    else  unpark(resource)

    door.unlock()
  }

  /** Returns a representation of the state of the lock.
    *
    * Not locked, so not for use in production, but may be helpful.
    */
  override def toString()
      : String =
  {
    val b = new StringBuilder()
    b ++= "GranularReadWriteLock(maxReaders: "
    b append maxReaders
    b ++= ", maxWriters: "
    b append maxWriters
    b ++= ", maxWriters: "
    b append maxWriters
    b ++= ")"
    b.result()
  }

  /** Returns internal data.
    *
    * Locked, and mainly for debugging.
    */
  def dataToString()
      : String =
  {
    val b = new StringBuilder()

    door.lock()
    b ++= "main lock queue count:"
    b append door.getQueueLength()
    b ++= "\nresourceMap:\n"
    b append resourceMap
    b ++= "\nthreadIdMap:\n"
    b append threadIdMap
    door.unlock()

    b.result()
  }

}//GranularReadWriteLock



object GranularReadWriteLock {

  /** Returns a lock with max threads set to available processors.
    *
    * JVMs reported available processors + 1, which is an appropriate quick compromise.
    */
  def machine()
      : GranularReadWriteLock =
  {
    val sz = Runtime.getRuntime().availableProcessors() + 1
    new GranularReadWriteLock(sz, sz)
  }

  /** Returns a lock with max threads set to tomcat.
    *
    * at the time of writing, 200 threads.
    */
  def tomcat()
      : GranularReadWriteLock =
  {
    new GranularReadWriteLock(200, 200)
  }

  /** Returns a lock with max threads set to something high.
    *
    * at the time of writing, 10,000 threads. This will not finish off most JVMs, but is orders higher than most Java gear uses.
    */
  def big()
      : GranularReadWriteLock =
  {
    new GranularReadWriteLock(10000, 10000)
  }

  def apply(
    maxReaders : Int,
    maxWriters: Int
  )
      : GranularReadWriteLock =
  {
    new GranularReadWriteLock(maxReaders, maxWriters)
  }

  // Remove Lockpark before use.
  def test() {
    val l = new GranularReadWriteLock(maxReaders = 8, maxWriters = 8)
    l.enterForRead(22)
    l.enterForRead(22)
    l.enterForRead(47)
    l.enterForRead(54)
    l.enterForRead(22)
    l.enterForWrite(22)
    l.enterForRead(22)
    println(l.dataToString())

    println("\n\nAfterDrain:")
    l.enterForRead(22)
    //l.provoke()
    println(l.dataToString())

  }

}//GranularReadWriteLock



/*
 ssc -class freight.core.util.GranularReadWriteLockTest2 run
 */
object GranularReadWriteLockTest {
  import java.util.concurrent._

  class WorkerThread(
    lock: GranularReadWriteLock,
    isWriter: Boolean,
    sleep: Int
  )
      extends Runnable
  {
    def run() {
      println(s"started(id:${Thread.currentThread().getId()}, isWriter:$isWriter, sleep (secs): $sleep)")

      if(isWriter) lock.enterForWrite(22)
      else lock.enterForRead(22)
      Thread.sleep(sleep * 1000)
      lock.exit()

      // Can get in the way of the display?
      //println(s"ended(id:${Thread.currentThread().getId()})")
    }
  }

  def main(args: Array[String]) {
    println("Hello, world!")

    val lock = new GranularReadWriteLock(20, 20)
    val tp = Executors.newFixedThreadPool(8, Executors.defaultThreadFactory)

    // Make one a reader, then several
    //make one a writer, then several (should park the readers)
    // make one a writer, then readers (should unpark the readers in one)
    // make several writers (should unpark one by one)
    // (end map should always be empty)

    val thread1 = new WorkerThread(lock, isWriter = true, sleep = 2)
    val thread2 = new WorkerThread(lock, isWriter = false, sleep = 2)
    val thread3 = new WorkerThread(lock, isWriter = true, sleep = 1)

    // Play round with these
    tp.execute(thread1)
    // Ensure launch order
    Thread.sleep(2)
    tp.execute(thread2)
    // Ensure launch order
    Thread.sleep(2)
    tp.execute(thread3)

    Thread.sleep(500)
    println("\nrunning lock state...")
    println(lock.dataToString())
    println("\n")

    //lock.flush()
    //Thread.sleep(200)

    // Shutdown
    tp.shutdown()
    while (!tp.isTerminated()) {}

    // Final lock state
    println("\nfinal lock state...")
    println(lock.dataToString())
    println("\n")

    println("done")
  }//main

}//GranularReadWriteLockTest

