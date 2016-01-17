package freight
package interface
package file
package filebase


import java.nio.file.{Path, Files}
import java.nio.file.{OpenOption, StandardOpenOption}
import java.nio.channels.{
  ReadableByteChannel,
  WritableByteChannel,
  FileChannel,
  Channels
}

import java.io.IOException
import java.io.{BufferedWriter, BufferedReader, FileInputStream, InputStream, OutputStream}
import java.util.concurrent.atomic.AtomicBoolean


import freight.core.util._
import common._

import core.objects.mutable.WritingStringBuilder

/*
 import freight._
 import freight.interface.file._
 import freight.interface.file.binaryBase._

 val f = SyncBinaryCollection.test

 // Merge
 f<(Paper.testNewPaper)

 // Read
 val b = ?
 val o = f("1", (g: StringGiver) => {Paper.stringFieldBridge(b, g)})
 b.result

 // foreach
 f.foreach((g) => {Paper.stringFieldBridge(b, g)})

 // delete
 f-(0)
 */

/** A collection stored as files in a directory.
  * 
  * Treats a local file directory as a collection of records (or like a
  * single table in a database, with each file as a record).
  * Each entry file is named with a number. The filename number is the
  * same as the freight id of the object the file contains.
  *
  * The binary base is not a standard templated Freight collection.
  * The class methods handle only one type, binary data. The binary
  * data is expressed as streams. However, the class has a Freight-like
  * API, and several overloaded methods for handling
  * Java binary data stream APIs.
  *
  * Files in a file base can be edited by hand/text editor, and
  * substituted and appended using a file browser. However, `repairNS`
  * should be run afterwards, or other code will receive incorrect
  * returns from the methods `size`, `byteSize`, and `nextId` (for
  * auto-appending).
  *
  * @define coll binaryBase
  *
  * @param p the path to use for this file SyncBinaryCollection. Note that the
  *  package will implicitly wrap strings to produce a path.
  */
//TODO: UnPar.. variant
// TODO: Consider a cache! But whose implementation? What?
// TODO: consider a buffer, not a journally, but for buffering of
// file access.
// TODO: Add a defragger/compactor
//TODO: Obviously, better with an Index on disk. Hashtable, likely.
class SyncBinaryCollection(
  val p: Path,
  val collectionName: String
)
 extends BinaryTakable
with common.IdCollection
 with Namespace
{
/*

  def ~(id: Long,g: (java.nio.channels.ReadableByteChannel, Long)): Long = ???
  def ^(id: Long,g: (java.nio.channels.ReadableByteChannel, Long)): Boolean = ???
  def apply(id: Long,f: ((java.nio.channels.ReadableByteChannel, Long)) => Unit): Boolean = ???
  def foreach(f: ((java.nio.channels.ReadableByteChannel, Long)) => Unit): Boolean = ???

*/
  // Auto raise
  try {
    Files.createDirectories(p)
  }
  catch {
    case e: Exception =>
      error("Unable to create directories for path $p")
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

  val details = new SyncNamespaceHeader(nsBuff, 0)



  // Only one read write at a time.
  // 	SocketChannel(SelectorProvider provider)
  // writable
  //java.nio.channels.Pipe.SinkChannel
  // readable
  //java.nio.channels.Pipe.SourceChannel
  //java.nio.channels.DatagramChannel
  //html respose off3ers OutputStream PrintWriter
  //  OutputStream - The servlet container does not encode the binary data.


  /** Applies a function to an element in this collection.
    *
    * The method opens and closes the filechannel used as source.
    *
    * @param id the id of an element providing binary data.
    * @param f a function applying a channel from the element.
    */
  def apply(
    id: Long,
    f: ((ReadableByteChannel, Long)) => Unit
  )
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )

    if (shutdown.get) false
    else {

      val path = p.resolve(id.toString)
      var rc: FileChannel = null
      val o = new java.util.HashSet[OpenOption]()
      o.add(StandardOpenOption.READ)


      try {

        lock.enterForRead(id)
        // Ultimately this is a FileChannel on my JVM, via StreamDecoder -
        // that's probably ok without using Filechannel itself.


        rc = FileChannel.open(path, o)
        f((rc, rc.size()))

        lock.exit()
        true
      }
      catch {
        case e: IOException => {
          warning(
"binaryBase.SyncBinaryCollection",
s"SyncBinaryCollection: Failure reading entry at $path"
)
          false
        }
      }
      finally {
        try {
          rc.close()
        }
        catch {
          case e: Exception => 
          warning(
"binaryBase.SyncBinaryCollection",
s"failed to close write file $path"
)
        }
      }
    }
  }


  /** Applies file data to a writable byte channel.
    *
    * A writable byte channel is at the root of many NIO writable
    * channels; SocketChannel, FileChannel, Pipe.SinkChannel, and
    * DatagramChannel.  This method accepts all of these to write
    * to. The channel must be opened before being passed to this
    * method, and is not closed when leaving.
    *
    * The method opens and closes the filechannel used as source.
    *
    * @param id the id of an element providing binary data.
    * @param c an writable byte channel to stream to.
    */
/*
  def apply(
    id: Long,
    c: WritableByteChannel
  )
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )
    elErrorIf(!c.isOpen(), s"supplied channel is not open")

    if (shutdown.get) false
    else {

      val path = p.resolve(id.toString)
      var rc: FileChannel = null
      val o = new java.util.HashSet[OpenOption]()
      o.add(StandardOpenOption.READ)


      try {

        lock.enterForRead(id)

        // Ultimately this is a FileChannel on my JVM, via StreamDecoder -
        // that's probably ok without using Filechannel itself.
        rc = FileChannel.open(path, o)
        rc.transferTo(0, rc.size(), c)

        lock.exit()
        true
      }
      catch {
        case e: IOException => {
          warning(
"binaryBase.SyncBinaryCollection",
s"SyncBinaryCollection: Failure reading entry at $path"
)
          false
        }
      }
      finally {
        try {
          rc.close()
        }
        catch {
          case e: Exception => 
warning(
"binaryBase.SyncBinaryCollection",
 s"failed to close write file $path"
)
        }
      }
    }
  }
*/

  /** Inserts or updates an element.
    * 
    * The action depends on the builder, and it's configuration,
    * supplied.
    */
  private def insertUpdate (
    givenId: Long,
    rc: ReadableByteChannel,
    byteCount: Long
  )
      : Long =
  {
    elErrorIf(!rc.isOpen(), s"supplied channel is not open")



    if (shutdown.get) NullID
    else {

      val isNew = (givenId == NullID)

      val pathId =
        if(isNew) nextId
        else givenId

      // Whatever id found for filename, make the path
      val path = p.resolve(pathId.toString)

      // Not right, but should be near :)
      // TODO: file.getChannel().size()
      val prevSize =
        if(Files.isReadable(path)) Files.size(path)
        else 0

      var wc: FileChannel = null
      val o = new java.util.HashSet[OpenOption]()
// NB: Y'know, having to state both of these is either not spec or the documentation has done a vague one.
// but it don't go otherwise on the Linux box. R. C
      o.add(StandardOpenOption.WRITE)
      o.add(StandardOpenOption.CREATE)


      try {

        lock.enterForWrite(pathId)

        // By default the method creates a new file or overwrites an
        // existing file. Fine.
        wc = FileChannel.open(path, o)
        wc.transferFrom(rc, 0, byteCount)


        lock.exit()

        val newSize =
          if(Files.isReadable(path)) Files.size(path)
          else 0

        // update header
        if(isNew) {
          details.addRecords(1, newSize.toInt)
        }
        else details.byteSizeInc(newSize - prevSize)

        pathId
      }
      catch {
        case e: IOException => {
          warning(
"binaryBase.SyncBinaryCollection",
s"Writing entry at $path"
)
          NullID
        }
      }
      finally {
        try {
          wc.close()
        }
        catch {
          case e: Exception => 
warning(
"binaryBase.SyncBinaryCollection",
 s"failed to close write file $path"
)
        }
      }
    }
  }


  def ^(
    id: Long,
    p: (ReadableByteChannel, Long)
  )
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )

    val ret = insertUpdate (
      givenId = id,
      p._1,
      p._2
    )
    (ret != NullID)
  }


  def +(
    p: (ReadableByteChannel, Long)
  )
      : Long =
  {
    insertUpdate (
      givenId = 0,
      p._1,
      p._2
    )
  }

  def ~(
    id: Long,
    p: (ReadableByteChannel, Long)
  )
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )

    val ret = insertUpdate (
      givenId = id,
      p._1,
      p._2
    )
    (ret != NullID)
  }

  def foreach(f: ((ReadableByteChannel, Long)) => Unit)
 : Boolean = 
{
    // TODO: Should reuse it's filechannel, maybe?
    def eachElem(p: Path) {
      val fnS = p.getFileName().toString
      if (!fnS.endsWith("ns")) {
        apply(fnS.toLong, f)
      }
    }

    File.dirForEach(p, eachElem _)
    true
  }









// -----------------------
// -- Path based methods
// ------------------------
// NB: Good additions for any binary collction deriving
// data externally R.C.
/*
def apply(
    id: Long
  )
      : Option[Path] =
  {
val path = p.resolve(id.toString)
if(p.toFile.exists()) Some(path)
else None
}
*/

/*
   override def ^(
    id: Long,
    p: Path
  )
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )

    val c = new FileInputStream(p.toFile()).getChannel()
    this.^(id, c)
  }



   override def +(
    p: Path
  )
      : Long =
  {
//log(s"Src Path is ${p}")
    val c = new FileInputStream(p.toFile()).getChannel()
    this.+(c)
  }
*/

  val genreString: String = "BinaryBase"
  val klassString: String = "SyncBinaryCollection"

  def addString(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "path: "
    b append p
    b ++= ", collectionName:"
    b append collectionName
  }

}//SyncBinaryCollection



object SyncBinaryCollection {

  def testColl():  (SyncConnection, BinaryTakable) = {
val db = SyncConnection.testDB
    val coll = db.binaryCollection(
      "binaryBase"
    )
(db, coll)
  }


  def apply(
    p: Path,
   collectionName: String
  )
      : SyncBinaryCollection =
  {
    new SyncBinaryCollection(p, collectionName)
  }


}//SyncBinaryCollection
