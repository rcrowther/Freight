package freight
package interface
package file

import freight.core.interfaces.ReadWriteOne

import java.nio.file.{Path, Files}
import java.nio.charset.Charset

import java.io.IOException
import java.io.{BufferedWriter, BufferedReader}

//import freight.core.objects.mutable.TakeableMultiStringBreaker
import core.iface.StringCanSpeak

/*
 import freight._
 import freight.interface.file._
 import java.nio.charset.StandardCharsets
 import freight.objects.Paper
 //import freight.canspeak.XML

 val f = Entry.testEntry


 // Merge
 f < Paper.testGiver

 // read
 val b = freight.canspeak.JSON.to("99", false)
 val o = f((g) => {Paper.stringFieldBridge(b, g)})
 b.result

 // delete
 f-()
 */

/** Actions on a single entry file.
  *
  * Intended for one off readwrites to arbitary filenames. For a
  * collection, see [[freight.interface.file.base]].
  *
  * Note that reading an object returns data depending on a builder.
  * and the object may carry id data which, if the writing was
  * unknown, should be regarded as undefined.
  *
  * @define obj file
  *
  * @param path A java path. Should point to a non-dir/non-symbolic
  * file. Note that the package will implicitly wrap strings to
  * produce a path.
  * @param charset A java charset. Note that the package will
  * wrap strings to guess a charset, and has an type alias for
  * StandardCharsets e.g. StandardCharsets.UTF_8 will work.
  */
//TODO: Use file locks to integrate with the OS and other JVMs?
//http://www.onjava.com/pub/a/onjava/2002/10/02/javanio.html?page=1
//TODO: Some lock so freight does not internally lock?
//TOCONSIDER: Maintaining the buffers for multipe object writes?
//TOCONSIDER: Blocks on the close flush
//TOCONSIDER: Is locked
//TOCONSIDER: Since length is impossible to determine in advance, there is no way to do this without buffering. Best could be done would be to read bigger batches than lines
//TODO: Mayshould use meta for compatibility? Still, apply() is not compatible.
// TODO: Needs ierud revision
final class Entry(
  private val p: Path,
  private val charset: Charset,
  private val stringFieldBridge: StringFieldBridge,
  private val speak: StringCanSpeak
)
//extends ReadWriteOne[StringObject]
{

  // If it exists and is not regular...
  elErrorIf(
    Files.isSymbolicLink(p) || Files.isDirectory(p),
    s"Path $p exists and is not a regular file"
  )

  /** Reads the $obj.
    */
  // uses BufferedReader and InputStreamReader, via Files
  // BufferedReader are locked.
  def apply[A](
    gm: (Giver) => A
  )
      : Option[A] =
  {
    elErrorIf(
      !Files.isReadable(p),
      s"Path $p is not a readable file"
    )

    //var r:  BufferedReader  = null
    //var res: TakeableMultiStringBreaker = null
    try {
      //r = Files.newBufferedReader(p, charset)

      // Ultimately this is a FileChannel on my JVM, via StreamDecoder -
      // that's probably ok without using Filechannel itself.
      val str = new String(Files.readAllBytes(p), charset)

      Some(gm(speak.from(str)))
    }
    catch {
      case e: Exception => error(s"Reading entry at $p", e)
        null
    }

  }

  private def assertDirs(p: Path): Boolean = {
    val parent = p.getParent
    if(!Files.isDirectory(parent)) {
      parent.toFile.mkdirs()
    }
    else true

  }

  /** Merge with the $obj.
    * Overwrites if the file exists.
    *
    * If needed, creates the object.
    */
  //TODO: Should overwrite?
  // Uses a BufferedWriter and OutputStreamWriter(newOutputStream(path, options)
  def <(g: Giver)
      : String =
  {
    elErrorIf(
      !assertDirs(p),
      s"Path $p unable to create parent directories"
    )

    var w: BufferedWriter = null
// TODO: Not good enough. Is is creeated, updated, what is the id, etc.
val preserveId = true

    // can be zero, will not be used? logical?
    val b = speak.to(None)
    //g.give(b)
    stringFieldBridge(b, g)

    val s = b.result

    try {
      w = Files.newBufferedWriter(p, charset)
      w.write(s, 0, s.length())
      b.generatedId
    }
    catch {
      case e: IOException => error(s"Writing entry at $p")
          NullIDStr
    }
    finally {
      try {
      w.close()
      }
      catch {
        case e: Exception => error(s"failed to close write file $p")
      }

    }
  }

  /** Deletes the $obj.
    * Is silent if the file does not exist.
    */
  def -()
      : Unit =
  {
    Files.deleteIfExists(p)
  }

  def size() : Long =
{
Files.size(p)
}

}//Entry



object Entry {
  def testEntry:  Entry = {
    import java.nio.charset.StandardCharsets
    import freight.objects.Paper

    Entry(
"/home/rob/Code/scala/freight/src/test/interface/file/file/bloop.xml",
      StandardCharsets.UTF_8,
      Paper.stringFieldBridge,
      freight.canspeak.JSON
    )
  }
  
  def apply(
    p: Path,
    transform: StringFieldBridge,
    speak: StringCanSpeak
  )
      : Entry =
  {
    new Entry(p, Charset.defaultCharset(), transform, speak)
  }

  def apply(
    p: Path,
    charset: Charset,
    transform: StringFieldBridge,
    speak: StringCanSpeak
  )
      : Entry =
  {
    new Entry( p, charset, transform, speak)
  }
  

}//Entry
