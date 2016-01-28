package freight
package core
package collection


import java.nio.file.Path

import java.nio.channels.{
  ReadableByteChannel,
  WritableByteChannel,
  FileChannel,
  Channels
}
import java.nio.ByteBuffer

import java.io.{FileInputStream, FileOutputStream, InputStream, OutputStream}



/** Templates a collection for binary data.
  *
* Multiplies the methods to polymorphicly handle `FileChannel`, `Path` and streams.
  *
  *
  * @define coll collection
  */
trait BinaryTakable
extends Takable[(ReadableByteChannel, Long), WritableByteChannel]
{

/*
/** As seen from class BinaryTakable, the missing signatures are as follows.
 *  For convenience, these are usable as stub implementations.
 */
  def -(id: Long): Boolean = ???
  def +(g: (java.nio.channels.ReadableByteChannel, Long)): Long = ???
  def ~(id: Long,g: (java.nio.channels.ReadableByteChannel, Long)): Boolean = ???
  def ^(id: Long,g: (java.nio.channels.ReadableByteChannel, Long)): Boolean = ???
  def addString(b: StringBuilder): StringBuilder = ???
  def apply(id: Long,f: ((java.nio.channels.ReadableByteChannel, Long)) => Unit): Boolean = ???
  def clear(): Boolean = ???
  def foreach(f: ((java.nio.channels.ReadableByteChannel, Long)) => Unit): Boolean = ???
  def genreName: String = ???

*/


  /** Inserts element data from an input stream.
    *
    * The stream must be opened before being passed to this method,
    * and is not closed when leaving.
    *
    * @param id the id of an element providing binary data.
    * @param s an input stream to read from.
    * @param byteCount the number of bytes to read.
    */
  def ^(
    id: Long,
    s: InputStream,
    byteCount: Long
  )
      : Boolean =
  {
    val c = Channels.newChannel(s)
    ^(id, (c, byteCount))
  }

  /** Inserts element data from a file channel.
    *
    * The channel must be opened before being passed to this method.
    * It is closed when leaving.
    *
    * The method opens and closes the filechannel used as source.
    *
    * @param id the id of an element providing binary data.
    * @param s a file channel to read from.
    */
  def ^(
    id: Long,
    c: FileChannel
  )
      : Boolean =
  {
    val rc = c.asInstanceOf[ReadableByteChannel]
    val r = this.^(id, (rc, c.size()))
    c.close()
    r
  }

  /** Inserts element data from a file path.
    *
    * @param p a file path to read from.
    */
  def ^(
    id: Long,
    p: Path
  )
      : Boolean =
  {
    var c: FileChannel = null
    try {
      c = new FileInputStream(p.toFile).getChannel()

      ^(id, c)
    }
    catch {
      case e: Exception => false
    }
    finally {

      try {
        c.close()
      }
      catch {
        case e: Exception =>
          warning(
            "iface.BinaryCollection",
            s"failed to close write file $p"
          )
      }
    }
  }


  /** Appends element data from an input stream.
    *
    * The stream must be opened before being passed to this method,
    * and is not closed when leaving.
    *
    * @param s an input stream to read from.
    * @param byteCount the number of bytes to read.
    */
  def +(
    s: InputStream,
    byteCount: Long
  )
      : Long =
  {
    val c = Channels.newChannel(s)
    this.+((c, byteCount))
  }

  /** Appends element data from an file channel.
    *
    * The channel must be opened before being passed to this method.
    * It is closed when leaving.
    *
    * @param s a file channel to read from.
    */
  def +(
    c: FileChannel
  )
      : Long  =
  {
    val bc = c.asInstanceOf[ReadableByteChannel]
    val r = this.+((bc, c.size()))
    c.close()
    r
  }

/*
  /** Appends element data from a file path.
    *
    * @param p a file path to read from.
    */
  def +(
    p: Path
  )
      : Long =
  {
    var c: FileChannel = null
    try {
      c = new FileInputStream(p.toFile).getChannel()

      this.+(c)
    }
    catch {
      case e: Exception => NullID
    }
    finally {
      try {
        c.close()
      }
      catch {
        case e: Exception =>
          warning(
            "iface.BinaryCollection",
            s"failed to close write file $p"
          )
      }
    }
  }
*/
//TOCONSIDER: Utility, place elsewhere?
/*
private def channelWrite(src: ReadableByteChannel, dst: WritableByteChannel)
: Boolean =
 {
  val buffer = ByteBuffer.allocateDirect(16 * 1024)
try {
    while (src.read(buffer) != -1) {
      // prepare the buffer to be drained
      buffer.flip()
      // write to the channel, may block
      dst.write(buffer)
      // If partial transfer, shift remainder down
      // If buffer is empty, same as doing clear()
      buffer.compact()
    }
    // EOF will leave buffer in fill state
    buffer.flip()
    // make sure the buffer is fully drained.
    while (buffer.hasRemaining()) {
      dst.write(buffer)
    }
true
}
catch {
case e: Exception => false
}
}
*/

val giverTakerBridge: (WritableByteChannel, (ReadableByteChannel, Long)) => Unit = channelWrite

def channelWrite(
dst: WritableByteChannel,
src: (ReadableByteChannel, Long)
)
 {
// The bytesize element in the tuple can be ignored.
// While it is useful for unified methodology,
// channel read/writing must work from EOF signals,
// and the readable should only ever be one complete object
// in size.  
val srcBC = src._1
  val buffer = ByteBuffer.allocateDirect(16 * 1024)
try {
    while (srcBC.read(buffer) != -1) {
      // prepare the buffer to be drained
      buffer.flip()
      // write to the channel, may block
      dst.write(buffer)
      // If partial transfer, shift remainder down
      // If buffer is empty, same as doing clear()
      buffer.compact()
    }
    // EOF will leave buffer in fill state
    buffer.flip()
    // make sure the buffer is fully drained.
    while (buffer.hasRemaining()) {
      dst.write(buffer)
    }
}
catch {
case e: Exception => error("failed to transfer binary data to a WritableByteChannel")
}
}



  /** Applies a function to an element in this collection.
    *
    * The method opens and closes the byte channel used as source.
    *
    * @param id the id of an element providing binary data.
    * @param f a function to apply to an element, supplied with a
    * channel and the element data length.
    */
// NB: Tupled input caused by type erasure ... not happy ...
/*
  def apply(
    g: (Long, (InputStream) => Unit)
  )
      : Boolean =
  {
    apply(g._1, (params: Tuple2[ReadableByteChannel, Long])  => {
g._2(Channels.newInputStream(params._1))})
  }
*/

  /** Applies element data to an output stream.
    *
    * The stream must be opened before being passed to this method,
    * and is not closed when leaving.
    *
    * The method opens and closes the filechannel used as source.
    *
    * @param id the id of an element providing binary data.
    * @param s an output stream to write to.
    */
  def apply(
    id: Long,
    s: OutputStream
  )
      : Boolean =
  {
    val wc = Channels.newChannel(s)
    apply(id, wc)
  }

  def apply(
    id: Long,
    c: FileChannel
  )
      : Boolean =
  {
    val wc = c.asInstanceOf[WritableByteChannel]
    val r = this(id, wc)
    c.close()
    r
  }

  /** Updates element data from a file path.
    *
    * @param p a file path to read from.
    */
  def apply(
    id: Long,
    p: Path
  )
      : Boolean =
{
    var c: FileChannel = null
    try {
      c = new FileOutputStream(p.toFile).getChannel()
      this(id, c)
    }
    catch {
      case e: Exception => false
    }
    finally {

      try {
        c.close()
      }
      catch {
        case e: Exception =>
          warning(
            "iface.BinaryCollection",
            s"failed to close write file $p"
          )
      }
    }
}

  /** Updates element data from an input stream.
    *
    * The stream must be opened before being passed to this method,
    * and is not closed when leaving.
    *
    * @param id the id of an element providing binary data.
    * @param s an input stream to read from.
    * @param byteCount the number of bytes to read.
    */
  def ~(
    id: Long,
    s: InputStream,
    byteCount: Long
  )
      : Boolean =
  {
    val c = Channels.newChannel(s)
    this.~(id, (c, byteCount))
  }

  /** Updates element data from a file channel.
    *
    * The channel must be opened before being passed to this method.
    * It is closed when leaving.
    *
    * The method opens and closes the filechannel used as source.
    *
    * @param id the id of an element providing binary data.
    * @param s a file channel to read from.
    */
  def ~(
    id: Long,
    c: FileChannel
  )
      : Boolean =
  {
    val rc = c.asInstanceOf[ReadableByteChannel]
    val r = this.~(id, (rc, c.size()))
    c.close()
    r
  }

  /** Updates element data from a file path.
    *
    * @param p a file path to read from.
    */
  def ~(
    id: Long,
    p: Path
  )
      : Boolean =
  {
    var c: FileChannel = null
    try {
      c = new FileInputStream(p.toFile).getChannel()

      this.~(id, c)
    }
    catch {
      case e: Exception => false
    }
    finally {

      try {
        c.close()
      }
      catch {
        case e: Exception =>
          warning(
            "iface.BinaryCollection",
            s"failed to close write file $p"
          )
      }
    }
  }
/*
  def apply(id: Long, t: WritableByteChannel)
: Boolean = 
{
var ok = true
this(
id,
     (params: Tuple2[ReadableByteChannel, Long]) =>  { ok = channelWrite(params._1, t) }
)
ok

}
*/

/*
//TODO: bit slow? Not optomised... tired...

  def foreach(t: java.nio.channels.WritableByteChannel): Boolean = {
var ok = true
foreach(
     (params: Tuple2[ReadableByteChannel, Long]) =>  { ok &= channelWrite(params._1, t) }
)
ok
}
*/

}//BinaryTakable
