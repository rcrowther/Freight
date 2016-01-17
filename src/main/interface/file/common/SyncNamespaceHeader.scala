package freight
package interface
package file
package common

import java.util.concurrent.locks.ReentrantReadWriteLock

import java.nio.ByteBuffer



/** Synchronised version of a namesapce header.
*
* See [[NamespaceHeader]]
  *
  */
// TODO: Base virtual 'Name'? And Freight type?
// defo: Canspeak!?
final class SyncNamespaceHeader(
  buff: ByteBuffer,
  ofs: Int
)
{
  val l = new ReentrantReadWriteLock()
  val header = new NamespaceHeader(buff, 0)



  // Readers //

  def recordCount : Long = {
    l.readLock.lock()
    val v = header.recordCount
    l.readLock.unlock()
    v
  }

  def topId: Long = {
    l.readLock.lock()
    val v = header.topId
    l.readLock.unlock()
    v
  }

  def byteSize : Long = {
    l.readLock.lock()
    val v = header.byteSize
    l.readLock.unlock()
    v
  }



// Helpers //

  def addRecords(n: Int, size: Int) {
    l.writeLock.lock()
    header.recordCountInc(n)
    header.byteSizeInc(size)

    header.topIdInc(n)
    l.writeLock.unlock()
  }

/**
* @param removedId if equal to topId, will decrement topID by n. Can be set to NullId to never decrement.
*/
  def removeRecords(n: Int, size: Long, removedId: Long)  {
    l.writeLock.lock()
    header.recordCountDec(n)
    header.byteSizeDec(size)

          // If the highest id in the base was deleted, decrement
          // the top id, otherwise, leave the field alone.
          if(removedId == header.topId) {
            header.topIdDec(n)
          }
    l.writeLock.unlock()
  }

  def byteSizeInc(size: Long) {
    l.writeLock.lock()
    header.byteSizeInc(size)
    l.writeLock.unlock()
  }


  def reset(
    recordCount: Long,
    topId: Long,
    byteSize: Long
  )
  {
    l.writeLock.lock()
    header.reset()
    header.setRecordCount(recordCount)

    header.setTopId(topId)
    header.setByteSize(byteSize)
    l.writeLock.unlock()
  }

  def reset()
  {
    l.writeLock.lock()
    header.reset()
    l.writeLock.unlock()
  }

  override def toString()
      : String =
  {
    l.readLock.lock()
    val v = header.toString
    l.readLock.unlock()
    v
  }

}//SyncNamespaceHeader

