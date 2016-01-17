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
  * == Dependancies ==
  * From the Apache foundation,
  *  - commons-codec
  *
  * Many of the general builders in this package are simple methods on
  * objects. This is slightly awkward to use, with more parameters
  * than reciever-notation classes, but is so stringbuilders can be
  * passed through them and HTML built up in a flexible manner.
  *
  * 
  * == Interesting packages ==
  *
  *  - [[servlet]], especially [[servlet.Connection]], provides a few
  *    connections which can be used in Java container servers such as
  *    GlassFish, Tomcat, etc.
  *  - [[contentBuilder]] provides builders for structuring visual content 
  *  - [[pageBuilder]] provides builders to page-wrap content on a
  *    visual-based website.
  */
package object html {

  // Could be used in DB, for one?
  /** Provides a path from a string.
    */
  //implicit def stringToPath(v: String): Path = new File(v).toPath


}//package
