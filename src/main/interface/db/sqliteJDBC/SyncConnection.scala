package freight
package interface
package db
package sqliteJDBC


//  import java.sql.{SyncConnection, DriverManager, ResultSet};
import java.sql.{Connection, DriverManager}

import java.nio.file.{Path, Files}
import java.io.File

import generic._

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

/** An SQLite connection.
  *
  * @define conn sqlite
  *
  * @param p path to the file holding sqlite database data
  * @param inMemory forms a database in memory. Overrides path.
  */
// Much prefer,
// http://almworks.com/sqlite4java/javadoc/index.html
// But not working
final class SyncConnection(
  p: Path,
  inMemory: Boolean
)
    extends JDBCConnection
{
  Class.forName("org.sqlite.JDBC")
  //val c = classOf[org.sqlite.JDBC]
  //"jdbc:sqlite:/home/leo/work/mydatabase.db"
  private val url: String =
    if(inMemory) "jdbc:sqlite::memory:"
    else {
      //errorIf(!Files.exists(p), "file could not be found")
      //TODO: mkDirs
      "jdbc:sqlite:" + p.toString
    }

  log(s"opening SqliteJDBC connection: $url")
  private val db: Connection = DriverManager.getConnection(url)

  if (db == null) error("failed to open FileBase SyncConnection")

  def collection(
    collectionName: String,
    meta: CompanionMeta
  )
      : Generator =
  {
    log(s"opening SqliteJDBC collection $collectionName")
    new Collection(db, collectionName, meta)
  }



  override def refMap(
    collectionName: String
  )
      : RefMap =
  {
    log(s"opening SqliteJDBC refmap $collectionName")
    new RefMap(db, collectionName)
  }

  override def fieldQueryCollection(
    collectionName: String,
    meta: CompanionMeta
  )
      : FieldQueryable =
  {
    log(s"opening SqliteJDBC fieldQueryCollection $collectionName")
    FieldQueryCollection(
      db,
      collectionName,
      meta
    )
  }



import java.sql.Statement

  def -(
    collectionName: String
) 
: Boolean =
{
    log(s"deleting SqliteJDBC collection collectionName:$collectionName")
    val clean: Statement = db.createStatement()
    try {
      clean.execute(s"DROP TABLE $collectionName")
      true
    }
    catch {
      case e: Exception => {
        log(s"unable to delete SqliteJDBC collection: $collectionName")
        false
      }
    }
}

  def close()
      : Boolean =
  {
    log(s"closing SqliteJDBC connection: $url")
    try {
      db.close()
      true
    }
    catch {
      case e: Exception =>
        severe(s"unable to close SQLite connection $p")
        false
    }
  }



val genreString = "SQLiteJDBC"
val klassString = "SyncConnection"

  def addString(b: StringBuilder)
 : StringBuilder =
{
    if (inMemory) {
      b ++= "inMemory"
    }
    else {
      b ++= "p: "
      b append p
    }
}


}//SyncConnection



object SyncConnection {
  /*
   def charge =  SQLite.setLibraryPath("/home/rob/Code/scala/freight/lib")

   def apply(
   p: Path
   )
   : JDBCConnection =
   {
   new JDBCConnection(p)
   }
   */

  def testDB: SyncConnection = {

    new SyncConnection(
      new File("/home/rob/Code/scala/freight/src/test/interface/db/sqlite.db").toPath,
      false
    )
  }



  def testCollection
: Generator = 
{
    import freight.objects.Paper
    val db = new SyncConnection(
      new File("/home/rob/Code/scala/freight/src/test/interface/db/sqlite.db").toPath,
      false
    )

    db.collection("poems", Paper)
  }

  def apply (
    p: Path,
    inMemory: Boolean
  )
      : SyncConnection =
  {
    new SyncConnection(p, inMemory)
  }

}//SyncConnection

