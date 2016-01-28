package freight
package interface
package file
package filebase


import java.nio.file.{Path, Files}
import java.nio.charset.Charset

import java.io.IOException
import java.io.{BufferedWriter, BufferedReader}
import java.util.concurrent.atomic.AtomicBoolean


import freight.core.util._
import common._
import core.iface.StringCanSpeak

import core.objects.mutable.WritingStringBuilder

/*
 import freight._
 import freight.interface.file._
 import freight.interface.file.base._
 import java.nio.charset.StandardCharsets
 import freight.objects.Paper

 //import freight.canspeak.XML

 val f = SyncCollection.test

 // Append
 f.append(Paper.testNewPaper)

 // Read
f.get[Paper](3)
 val b = freight.canspeak.JSON.to("99", false, false)
 val o = f(3, b)
 b.result

 // foreach
 f.foreachObject((p: Paper) => {println(p)})

 // delete
 f-(?)
 */

/** A collection stored as files in a directory.
  * 
  * Treats a local file directory as a collection of records (or like a
  * single table in a database, with each file as a record).
  * Each entry file is named with a number. The filename number is the
  * same as the Freight id of the object the file contains.
  *
  * Files in a file base can be edited by hand/text editor, and
  * substituted and appended using a file browser. However, `repairNS`
  * should be run afterwards, or other code will receive incorrect
  * returns from the methods `size`, `byteSize`, and `nextId` (for
  * auto-appending).
  *
  * @define coll filebase
  *
  * @param p the path to use for this file SyncCollection. Note that the
  *  package will implicitly wrap strings to produce a path.
  * @param charset the charset of the underlying files. Note that
  *  the package will wrap strings to guess a charset, and has an type
  * alias for StandardCharsets e.g. StandardCharsets.UTF_8 will work.
  * @param meta collection of methods selecting fields.
  * @param speak The format of files in this file SyncCollection, e.g. JSON
  */
//TODO: UnPar.. variant
// TODO: Check Granular lock, file slurping?, consider journalling,
// consider transactional rollback.
// TODO: Consider a cache! But whose implementation? What?
// TODO: consider a buffer, not a journally, but for buffering of
// file access.
// TODO: Add a defragger/compactor
//TODO: Obviously, better with an Index on disk. Hashtable, likely.
//TODO: At least, a list of ids would be handy ('index of the id coloum')
class SyncCollection(
  protected val p: Path,
  val collectionName: String,
  private val charset: Charset,
  val meta: CompanionMeta,
  private val speak: StringCanSpeak
)
    extends Generator
    with Namespace
{

  def giverTakerBridge: FieldBridge = meta.stringFieldBridge


  // Auto raise
  try {
    Files.createDirectories(p)
  }
  catch {
    case e: Exception =>
      error("Unable to create directories at path $p")
  }

  // assert presence of directory
  elErrorIf(!Files.isDirectory(p), s"Path $p does not point to a directory")

  //TODO: Very ugly, testing the file for existence...
  // But how else to know this is new? Carry data in namespace, like (is first?)
  val isNew: Boolean = Files.exists(p.resolve("base.ns"))

  // Open a namespace file
  private val nsBuff = NamespaceFile.openOrCreate(p).getOrElse {
error("could not open namespace file at path $p")
}

  //TODO: Should be protected
  val details = new SyncNamespaceHeader(nsBuff, 0)

  // Lock for full shutdown or drop
// TODO: use the Granularlock?
  private val shutdown = new AtomicBoolean(false)

  //the lock
  private val lock = GranularReadWriteLock.big()


  /** Inserts or updates the dataSyncCollection.
    * 
    * The action depends on the builder, and it's configuration,
    * supplied.
    */
  private def write(b: WritingStringBuilder)
      : Long =
  {
    if (shutdown.get) NullID
    else {
      var w: BufferedWriter = null

      val writtenIdStr = b.generatedId
      val writtenId = writtenIdStr.toLong

      // Whatever id found for filename, make the path
      val path = p.resolve(writtenIdStr)

      // Not right, but should be near :)
      // TODO: file.getChannel().size()
      val prevSize =
        if(Files.isReadable(path)) Files.size(path)
        else 0

      val s = b.result
      //log(s"Base: File write")
      val sz = s.length()

      try {
        //OutputStreamWriter
        // FileChannel
        w = Files.newBufferedWriter(path, charset)

        // TODO: Security exception...
        //val asBytes = s.getBytes(charset)


        lock.enterForWrite(writtenId)
        // By default the method creates a new file or overwrites an
        // existing file. Fine.
        w.write(s, 0, sz)
        lock.exit()

        // update header
        if(b.wasNew) {
          details.addRecords(1, sz)
        }
        else details.byteSizeInc(sz - prevSize)

        writtenId
      }
      catch {
        case e: IOException => {
          log4(s"Writing entry at $path")
          NullID
        }
      }
      finally {
        try {
          w.close()
        }
        catch {
          case e: Exception => {
            log4(s"failed to close write file $path")
          }
        }
      }
    }
  }

  def upsert(b: WritingStringBuilder)
      : Boolean =
  {
    write(b) != NullID
  }

  def ^(
    id: Long,
    g: Giver
  )
      : Boolean =
  {
    val b = speak.to(
      Some(id.toString)
    )
    giverTakerBridge(b, g)
    upsert(b)
  }

  def +(
    g: Giver
  )
      : Long =
  {
    val b = speak.to(
      Some(nextId().toString)
    )
    giverTakerBridge(b, g)
    write(b)
  }

  def apply(
    id: Long,
    f: (Giver) ⇒ Unit
  )
      : Boolean =
  {
    if (shutdown.get) false
    else {

      val path = p.resolve(id.toString)

      try {

        lock.enterForRead(id)
        // Ultimately this is a FileChannel on my JVM, via StreamDecoder -
        // that's probably ok without using Filechannel itself.
        val str = new String(Files.readAllBytes(path), charset)
        lock.exit()

        f(speak.from(str))
        true
      }
      catch {
        case e: IOException => {
          log4(s"SyncCollection: Failure reading entry at $path")
          false
        }
      }
    }
  }

  def  ~(id: Long, g: Giver)
      : Boolean =
  {
    val b = speak.to(
     None
    )
    giverTakerBridge(b, g)
    (write(b) != NullID)
  }


  def -(id: Long)
      : Boolean =
  {

    //elErrorIf(id == NullIDStr, s"Attempting to delete id = Null")
    val idStr = id.toString

    if (shutdown.get) false
    else  {

      var ret = false
      val path = p.resolve(idStr)
      var prevSize: Long = 0

      try {
        lock.enterForWrite(id.toLong)

        // not racey operations, we're in the lock.
        if(Files.exists(path)) {

          // Not right, but should be near :)
          prevSize = Files.size(path)

          Files.delete(path)
          ret = true
        }

        lock.exit()


        // update header
        if(ret) {
          details.removeRecords(1, prevSize, id)
        }

        ret
      }
      catch {
        case e: IOException => error(s"Removing entry at $path")
          // Return a null id
          false
      }
    }
  }


  def foreach(f: (Giver) => Unit) 
      : Boolean =
{
var ret = true
    def eachElem(p: Path, fileName: String) {
     ret &= apply(fileName.toLong, f)
    }

    File.dirForEachIdFilename(p, eachElem _)
ret
  }


  //--------------
  // -- Objects --
  // -------------

  def insert[A](
    id: Long,
    o: A
  )(implicit ms: MultiTakerFieldSelect[A])
      : Boolean =
  {
    val b = speak.to(
      Some(id.toString)
    )
    ms(b, o)
    upsert(b)
  }

  def append[A](
    o: A
  )(implicit ms: MultiTakerFieldSelect[A])
      : Long =
  {
    val b = speak.to(
      Some(nextId().toString)
    )
    ms(b, o)
    write(b)
  }

  def update[A](
id: Long,
    o: A
  )(implicit ms: MultiTakerFieldSelect[A])
      : Long =
  {
    val b = speak.to(
      None
    )
    ms(b, o)
    write(b)
  }


  //--------------
  // -- General --
  // -------------

  def clear()
      : Boolean =
  {
    var ret = false

    shutdown.set(true)

    // If anything needs a work unit, this
    if(lock.blockUntilDie(2000)) {
      File.deleteAll(p)
      details.reset()
      ret = true
    }
    else {
      error("SyncCollection clear() failed")
    }
    shutdown.set(false)
    ret
  }

  def clean()
      : Boolean =
  {
    shutdown.set(true)
    if(lock.blockUntilDie(2000)) {
      // If anything needs a work unit, this - db/commands/dropindexes
      File.deleteAllForcefully(p)
      true
    }
    else {
      severe("SyncCollection clean() failed")
      false
    }
  }

  // NB: At the moment, this does nothing, especially as the mapped
  // namespace file can not be closed.
  def close() : Boolean = {
    // clear cache?
    true
  }

def genreString: String = "FileBase"
def klassString: String = "SyncCollection"

  def addString(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "path: "
    b append p
    b ++= ", collectionName:"
    b append collectionName
    b ++= ", charset:"
    b append charset
    b ++= ", meta:"
    b append meta
    b ++= ", speak:"
    b append speak
  }


}//SyncCollection



object SyncCollection {


  def test:  SyncCollection = {
    import java.nio.charset.StandardCharsets
    import freight.objects.Paper

    SyncCollection(
      "/home/rob/Code/scala/freight/src/test/interface/file",
"base",
      StandardCharsets.UTF_8,
      Paper,
      freight.canspeak.JSON
    )
  }


  def apply(
    p: Path,
   collectionName: String,
    meta: CompanionMeta,
    speak: StringCanSpeak
  )
      : SyncCollection =
  {
    new SyncCollection(p, collectionName, Charset.defaultCharset(), meta, speak)
  }

  def apply(
    p: Path,
   collectionName: String,
    charset: Charset,
    meta: CompanionMeta,
    speak: StringCanSpeak
  )
      : SyncCollection =
  {
    new SyncCollection(p, collectionName, charset, meta, speak)
  }


}//SyncCollection
