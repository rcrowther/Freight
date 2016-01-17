package freight
package interface

//import java.nio.file.{Path, Files, FileSystems, FileVisitOption, LinkOption}
import java.nio.file.{Path, Files}
import java.io.File
import scala.language.implicitConversions
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import scala.annotation._, elidable._



/**
*/
package object file {

  // Could be used in DB, for one?
  /** Provides a path from a string.
    */
  implicit def stringToPath(v: String): Path = new File(v).toPath


  /** Provides a charset from a string.
    * Throws error if not supported
    */
  implicit def charsetStringToCharset(charsetStr: String): Charset = {
    errorIf(Charset.isSupported(charsetStr), s"Charset '$charsetStr' is not supported on this platform.")
    Charset.forName(charsetStr)
  }

  type StandardCharsets = java.nio.charset.StandardCharsets

  // Common types
  //type SyncCollection = base.SyncCollection
  //val SyncCollection = base.SyncCollection

}//package
