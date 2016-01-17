package freight
package interface
package file
package common

import java.nio.channels.FileChannel
import java.nio.MappedByteBuffer
import java.nio.file.{OpenOption, StandardOpenOption, Path, Files}


/** Handle the creation and opening of a datafile for a base.
  */
// note that writes should be backed...
// see NamespaceDetailsRSV1MetaData
// RecordStoreMetaDataStock
// for setting and getting. USed in RecordStore, but that is less interesting.
// see CatalogEntry for init
object NamespaceFile {

  def create(p: Path, len: Long)
      : Option[MappedByteBuffer] =
  {
    log4(s"mappedfile create $p")


    try {

      // Assert Directories (annoying, but necessary for defence)
      //TODO: This ignores that the method can return true/false
      p.toFile.getParentFile().mkdirs()


      // heck. R.C.
      val o = new java.util.HashSet[OpenOption]()
      o.add(StandardOpenOption.READ)
      o.add(StandardOpenOption.WRITE)
      o.add(StandardOpenOption.CREATE_NEW)
      o.add(StandardOpenOption.DSYNC)

      val c = FileChannel.open(p, o)

      // NB: this seems to create the correct length of file, though
      // no note in docs that this is how to do it.
      Some(
        c.map(
          FileChannel.MapMode.READ_WRITE,
          // position
          0,
          // size
          len
        )
      )
    }
    catch {
      case e: Exception => None
    }
  }

  def isReadable(p: Path) : Boolean = {
    Files.isReadable(p.resolve("base.ns"))
  }

  def open(p: Path)
      : Option[MappedByteBuffer] =
  {
    log4(s"mappedfile open $p")


    try {

      // heck. R.C.
      val o = new java.util.HashSet[OpenOption]()
      o.add(StandardOpenOption.READ)
      o.add(StandardOpenOption.WRITE)
      o.add(StandardOpenOption.DSYNC)

      val c = FileChannel.open(p, o)
      val sz = c.size()

      Some(
        c.map(
          FileChannel.MapMode.READ_WRITE,
          // position
          0,
          // size
          sz.toInt
        )
      )
    }
    catch {
      case e : Exception => None
    }
  }

  def openOrCreate(p: Path)
      : Option[MappedByteBuffer] =
  {
    val path = p.resolve("base.ns")
    val buff = open(path)
    if (buff != None) Some(buff.get)
    else {
      val nbuff = create(path, NamespaceHeader.byteSize)
      if (nbuff == None) {
        log4(s"unable to create ns file in: $p")
        None
      }
      else {
        //goes through  getDur().createdFile(pathString, setLen)
        val buff = nbuff.get
        // TEMP from here
        val ns = new NamespaceHeader(buff, 0)
        ns.reset()
        Some(buff)
      }
    }
  }

}//NamespaceFile
