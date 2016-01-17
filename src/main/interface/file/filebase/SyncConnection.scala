package freight
package interface
package file
package filebase



import java.nio.file.{Path, Files}
import java.io.File

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

import generic._
import core.iface.StringCanSpeak


/*
 import freight._
 import freight.interface.db.sqliteJDBC._
 import freight.objects.Paper

 //val db = DBCollection.testCollection
 val db = SyncConnection.testDB

 import freight.objects.Paper
 val c = db.collection("poems", Paper)

 import freight.core.objects.Title
 val c = db.collection("f_set_marks", Title.multiTransformer)

 // Merge
 c<(new core.objects.generic.StringToMultiGiver(Paper.testGiver))

 // Read
 val b = freight.canspeak.JSON.to
 c(1, new core.objects.generic.StringToMultiTaker(b))
 b.result

 // delete
 c-(0)

 // RefMap
 val c = db.refMap("poems_map", Paper)
 */

/** A file base connection.
  *
  * @define conn filebase
  *
  * @param p path to the folder holding $obj data
  */
// Much prefer,
// http://almworks.com/sqlite4java/javadoc/index.html
// But not working
final class SyncConnection(
  p: Path,
  charset: Charset,
  speak: StringCanSpeak
)
extends core.collection.Connection
{

  val f = p.toFile

  log(s"opening FileBase connection: $f")

  if (!f.isDirectory()) error("failed to open FileBase SyncConnection")

  def collection(
    collectionName: String,
    meta: CompanionMeta
  )
      : Generator =
  {
    log(s"opening FileBase collection $collectionName")
    val path = p.resolve(collectionName)
    try {
      new SyncCollection(path, collectionName, charset, meta, speak)
    }
    catch {
      case e: Exception =>
        error("Unable to create collection")
    }

  }

  override def binaryCollection(
    collectionName: String
  ) 
: BinaryTakable =
  {
    val path = p.resolve(collectionName)
   SyncBinaryCollection(path, collectionName)
  }

  def -(
    collectionName: String
  )
      : Boolean =
  {
    log(s"deleting FileBase collection collectionName:$collectionName")
    val path = p.resolve(collectionName)
    if (!f.isDirectory()) {
      log(s"unable to delete FileBase: not a directory? collection: $collectionName")
      false
    }
    else {

      try {
        freight.core.util.File.deleteAllForcefully(p)
        true
      }
      catch {
        case e: Exception => {
          log(s"unable to delete FileBase collection: $collectionName")
          false
        }
      }
    }
  }

  def close()
      : Boolean =
  {
    // NB: Nothing to do here (currently - namespaces cant be closed...)
    true
  }

val genreString: String = "FileBase"

val klassString: String = "SyncConnection"

  def addString(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "p: "
    b append p
    b ++= ", charset: "
    b append charset
    b ++= "speak: "
    b append speak
  }

}//SyncConnection



object SyncConnection {

  def testDB: SyncConnection = {
    SyncConnection.utf8(
      "/home/rob/Code/scala/freight/src/test/interface/file",
      freight.canspeak.JSON
    )
  }


  def utf8(
    p: Path,
    speak: StringCanSpeak
  )
      : SyncConnection =
  {
    new SyncConnection(p, StandardCharsets.UTF_8, speak)
  }

  def apply (
    p: Path,
    speak: StringCanSpeak
  )
      : SyncConnection =
  {
    new SyncConnection(p, Charset.defaultCharset(), speak)
  }

  def apply (
    p: Path,
    charset: Charset,
    speak: StringCanSpeak
  )
      : SyncConnection =
  {
    new SyncConnection(p, charset, speak)
  }

}//SyncConnection

