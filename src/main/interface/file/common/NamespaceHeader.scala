package freight
package interface
package file
package common

import java.nio.ByteBuffer

/** Structures details about a particular base.
  *
  */
// TODO: Base virtual 'Name'? And Freight type?
// defo: Canspeak!?
final class NamespaceHeader(
  buff: ByteBuffer,
  ofs: Int
)
{

  def setVersion(v: Short) = buff.putShort(ofs, v)
  def version: Short = buff.getShort(ofs)
  val versionPos = 0


  def recordCount: Long = buff.getLong(ofs + 2)
  val recordCountPos = 2
  def setRecordCount(v: Long) = buff.putLong(ofs + 2, v)
  def recordCountInc(v: Long) = buff.putLong(ofs + 2, recordCount + v)
  def recordCountDec(v: Long) = buff.putLong(ofs + 2, recordCount - v)

  def topId: Long = buff.getLong(ofs + 10)
  val topIdPos = 10
  def setTopId(v: Long) = buff.putLong(ofs + 10, v)
  def topIdDec(v: Long) = buff.putLong(ofs + 10, topId - v)
  def topIdInc(v: Long) = buff.putLong(ofs + 10, topId + v)

  def byteSize: Long = buff.getLong(ofs + 18)
  val byteSizePos = 18
  def setByteSize(v: Long) = buff.putLong(ofs + 18, v)
  def byteSizeDec(v: Long) = buff.putLong(ofs + 18, byteSize - v)
  def byteSizeInc(v: Long) = buff.putLong(ofs + 18, byteSize + v)


  def reset() {
    setVersion(1)
    setRecordCount(0)
    setTopId(InitialID)
    setByteSize(0)
  }

  override def toString()
      : String =
  {
    val b = new StringBuilder()
    b ++= "NamespaceDetails(version: "
    b append version
    b ++= ", recordCount: "
    b append recordCount
    b ++= ", topId: "
    b append topId
    b ++= ", byteSize: "
    b append byteSize
    b ++= ")"
    b.result()
  }

}//NamespaceHeader



object NamespaceHeader{

  val byteSize = 26

}//NamespaceHeader
