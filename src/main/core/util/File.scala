package freight
package core
package util

import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileVisitOption
import java.io.IOException

/*
import freight.core.util.File
 import freight.interface.file._
File.countIdFilenames("/home/rob/Code/scala/freight/src/test/interface/file/base")
File.dirByteSize("/home/rob/Code/scala/freight/src/test/interface/file/base",  """\d{1,19}""")
*/

/** Utility methods for file handling.
  *
  */
// TODO: Move to core. It's used there, and now bigger than simply interface support.
object File{

  /** Delete a folder and all contents.
    *
    * Flat, will not recurse into directories.
*
* Can throw exceptions.
    */
  def deleteAllForcefully(p: Path) {
    class Deleter
        extends SimpleFileVisitor[Path] {
      override def visitFile(p: Path, attrs: BasicFileAttributes) = {
        val pStr = p.toString
        Files.delete(p)

        FileVisitResult.CONTINUE
      }
    }


    // Empty = not follow links
    val o = new java.util.HashSet[FileVisitOption]()
    val c = new Deleter
    Files.walkFileTree(p, o, 1, c)
    Files.delete(p)
  }


  /** Delete entries from a directory.
*
    * Only descends one level. Ignores directory and symlink files (as
    * detected by Java). Also ignores files beginning with '.' or
    * ending in '~' (hidden files on many systems).
    */
  //TODO: Use Files.isHidden, or not?
  def deleteAll(p: Path) {
    class Deleter
        extends SimpleFileVisitor[Path] {
      override def visitFile(p: Path, attrs: BasicFileAttributes) = {
        val pStr = p.toString
        if(
          Files.isRegularFile(p)
            && !(pStr(pStr.size - 1) == '~' || pStr(0) == '.')
        ) { Files.delete(p)}

        FileVisitResult.CONTINUE
      }
    }


    // Empty = not follow links
    val o = new java.util.HashSet[FileVisitOption]()
    val c = new Deleter
    Files.walkFileTree(p, o, 1, c)
  }

  /** Count files in a directory.
    *
    * Only descends one level. Ignores directory and symlink files (as
    * detected by Java). Also ignores files beginning with '.' or
    * ending in '~' (hidden files on many systems).
    */
  //TODO: Use Files.isHidden, or not?
//TODO: Should be optional and error ctching, see dirByteSize
  def count(p: Path)
      : Long =
  {
    class Counter
        extends SimpleFileVisitor[Path]
    {
      var i = 0
      override def visitFile(p: Path, attrs: BasicFileAttributes) = {
        val pStr = p.toString
        if(
          Files.isRegularFile(p) &&
            !(pStr(pStr.size - 1) == '~' || pStr(0) == '.')
        ) {i += 1}

        FileVisitResult.CONTINUE
      }
    }


    // Empty = not follow links
    val o = new java.util.HashSet[FileVisitOption]()
    val c = new Counter
    Files.walkFileTree(p, o, 1, c)
    c.i
  }

  /** Count files in a directory by matching a regex.
    *
    * Only descends one level. Ignores directory and symlink files (as
    * detected by Java).
*
* @param fileNameMatch string regex to match against filenames.
    */
  //TODO: Use Files.isHidden, or not?
//TODO: Should be optional and error ctching, see dirByteSize
//scala.util.matching.Regex
  def count(p: Path, fileNameMatch: String)
      : Long =
  {
    class Counter
        extends SimpleFileVisitor[Path]
    {
      var i = 0
      override def visitFile(p: Path, attrs: BasicFileAttributes) = {
        val pStr = p.toString
        if(
          Files.isRegularFile(p) &&
p.getFileName().toString.matches(fileNameMatch)
        ) {i += 1}

        FileVisitResult.CONTINUE
      }
    }


    // Empty = not follow links
    val o = new java.util.HashSet[FileVisitOption]()
    val c = new Counter
    Files.walkFileTree(p, o, 1, c)
    c.i
  }

  /** Count files named by digital numbers, in a directory.
    *
* Something of Freight speciality need.
*
    * Only descends one level. Ignores directory and symlink files (as
    * detected by Java).
*
    * @return tuple of the highest count found, and total number of matching files.
    */
  def countIdFilenames(p: Path)
      : (Long, Long) =
  {


    class Counter
        extends SimpleFileVisitor[Path]
    {
      var maxCount: Long = 0
      var i = 0

      override def visitFile(p: Path, attrs: BasicFileAttributes) = {
        val pStr = p.toString
val filenameStr = p.getFileName().toString
        if(
          Files.isRegularFile(p) &&
filenameStr.matches("""\d{1,19}""")
        )
 {
maxCount = Math.max(filenameStr.toLong, maxCount)
i += 1
}

        FileVisitResult.CONTINUE
      }
    }


    // Empty = not follow links
    val o = new java.util.HashSet[FileVisitOption]()
    val c = new Counter
    Files.walkFileTree(p, o, 1, c)
    (c.maxCount, c.i)
  }


  /** Count of file sizes in a directory.
    *
    * Only descends one level. Ignores directory and symlink files (as
    * detected by Java). Also ignores files beginning with '.' or
    * ending in '~' (hidden files on many systems).
*
* @param fileNameMatch string regex to match against filenames.
    */
  def dirByteSize(p: Path, fileNameMatch: String)
      : Option[Long] =
  {
    errorIf(!p.toFile.isDirectory(), "Not a directory: $p")

    class Sizer
        extends SimpleFileVisitor[Path]
    {
      var size = 0L

      override def visitFile(p: Path, attrs: BasicFileAttributes)
          : FileVisitResult =
      {
        val pStr = p.toString
        if(
          Files.isRegularFile(p) &&
p.getFileName().toString.matches(fileNameMatch)
        ) {size += attrs.size}
        FileVisitResult.CONTINUE
      }

    }

    val o = new java.util.HashSet[FileVisitOption]()
      val s = new Sizer

    try {
      Files.walkFileTree(p,o, 1, s)
      Some(s.size)
    }
    catch {
      case e: Exception => None
    }
  }


  /** Count of file sizes in a directory.
    *
    * Only descends one level. Ignores directory and symlink files (as
    * detected by Java). Also ignores files beginning with '.' or
    * ending in '~' (hidden files on many systems).
    */
  def dirForEach(p: Path, f: (Path) => Unit)
  {
    errorIf(!p.toFile.isDirectory(), "Not a directory: $p")

    class Paths
        extends SimpleFileVisitor[Path]
    {
      var size = 0L

      override def visitFile(p: Path, attrs: BasicFileAttributes)
          : FileVisitResult =
      {
        val pStr = p.toString
        if(
          Files.isRegularFile(p) &&
            !(pStr(pStr.size - 1) == '~' || pStr(0) == '.')
        ) { f(p) }
        FileVisitResult.CONTINUE
      }

    }

    val o = new java.util.HashSet[FileVisitOption]()
      val s = new Paths

    try {
      Files.walkFileTree(p,o, 1, s)
    }
    catch {
      case e: Exception => None
    }
  }

  /** Count of file sizes in a directory.
    *
    * Only descends one level. Ignores directory and symlink files (as
    * detected by Java). Also ignores files beginning with '.' or
    * ending in '~' (hidden files on many systems).
    */
  def dirForEachIdFilename(p: Path, f: (Path, String) => Unit)
  {
    errorIf(!p.toFile.isDirectory(), "Not a directory: $p")

    class Paths
        extends SimpleFileVisitor[Path]
    {
      var size = 0L

      override def visitFile(p: Path, attrs: BasicFileAttributes)
          : FileVisitResult =
      {
        val pStr = p.toString
val filenameStr = p.getFileName().toString
        if(
          Files.isRegularFile(p) &&
filenameStr.matches("""\d{1,19}""")
        )
 { f(p, filenameStr) }
        FileVisitResult.CONTINUE
      }

    }

    val o = new java.util.HashSet[FileVisitOption]()
      val s = new Paths

    try {
      Files.walkFileTree(p,o, 1, s)
    }
    catch {
      case e: Exception => None
    }
  }
}//File
