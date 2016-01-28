package freight
package interface
package db
package sqliteJDBC



import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.PreparedStatement
import java.sql.DatabaseMetaData
import java.sql.Types

//import core.objects.generic.StringToMultiGiver
//import core.iface.AnyValCollection
import generic.JDBCSchemaBuilder2


/*
 import freight._
 import freight.interface.db.sqliteJDBC._
 import freight.objects.Paper
 {
 import freight.interface.refCollection.vocabulary.Term
 val db = SyncConnection.testDB
 val c = db.collection("poems_terms", Term)
 c(0)(Term.apply)
 val t = Term(1, "short", "less than 30 lines", 0, 0)
 c<(t)
 c(1)(Term.apply)

 }
 //import freight.canspeak.XML
 val db = SyncConnection.testDB
 val c = Collection.testCollection(db)

 // Read
 val b = freight.canspeak.JSON.to("99", true, false)
 val o = c(1, (g: Giver) => {Paper.stringFieldBridge(b, g)})
 b.result

 // foreach
 c.foreach((g) => {Paper.stringFieldBridge(b, g)})

 // foreach to a base
 import freight.interface.file._
 val f = Base.testBase
 c.stringForeach( (g) => {f+(g)} )
 */

/** An SQLite collection.
  *
  * @define coll sqlite collection
  */
//TODO: Threads, whatever.
// Single threaded versions ccan use some class val?
//https://sqlite.org/autoinc.html
class Collection(
  val db: Connection,
  val collectionName: String,
  val meta: CompanionMeta
)
    extends generic.JDBCCollectionGeneric
    with Generator
{


  def giverTakerBridge: FieldBridge = meta.multiFieldBridge
  //protected def giverConverter(g: StringGiver) = new StringToMultiGiver(g)

  // generate sql for prepared statements
  private val schema = new JDBCSchemaBuilder2()
  meta.descriptiveGiver(schema)

  // JDBC fields indexed 1, 2, 3...
  // so need + 1
  val lastPSFieldIdx = schema.elementCount + 1

  // Ok - AUTO_INCREMENT is AUTOINCREMENT
  // and can only be set on integers.
// 
  val isNew: Boolean =
    if (!tableExists(db, collectionName) ) {
      log(s"creating db collection: $collectionName")
      val create: Statement = db.createStatement()
      //CREATE TABLE poems ( id integer PRIMARY KEY, language varchar(255) NOT NULL, title varchar(255) NOT NULL, description varchar(255) NOT NULL, body blob NOT NULL)
      try {
val createSQL = schema.createResult()
        println("create:"+ s"CREATE TABLE $collectionName ( $createSQL )")
        create.executeUpdate(s"CREATE TABLE $collectionName ( $createSQL )")
        true
      }
      catch {
        case e: Exception => {
          error(s"unable to create database: $collectionName")
          false
        }
      }
    }
    else false



  // cached statements
// NB: Cached statements are run on the DB AT INITIALIZATION TIME
// - do not move before create statements.
val fieldSQL = schema.fieldResult()
  //INSERT INTO poems VALUES(?, ?, ?...)
        //println("insert:"+ s"INSERT INTO $collectionName VALUES( $fieldSQL )")
  private val insertPS: PreparedStatement =
    db.prepareStatement(s"INSERT INTO $collectionName VALUES( $fieldSQL )")

  //INSERT INTO poems VALUES(?, ?, ?...)
s"INSERT INTO $collectionName VALUES( $fieldSQL)"

        //println("append:"+ s"INSERT INTO $collectionName VALUES( $fieldSQL )")
  private val appendPS: PreparedStatement =
    db.prepareStatement(s"INSERT INTO $collectionName VALUES( $fieldSQL )")

  //SELECT * FROM poems WHERE id = ?
  private val readPS: PreparedStatement =
    db.prepareStatement(s"SELECT * FROM $collectionName WHERE id = ?")



  //UPDATE poems SET id=?, language=?,title=?,description=?,body=? WHERE id = ?
  private val updatePS: PreparedStatement =
    db.prepareStatement(s"UPDATE $collectionName SET ${schema.titledFieldResult()} WHERE id = ?")


  // DELETE FROM poems WHERE id = ?
  private val deletePS: PreparedStatement =
    db.prepareStatement(s"DELETE FROM $collectionName WHERE id = ?")

  private val foreachPS: PreparedStatement =
    db.prepareStatement(s"SELECT * FROM $collectionName")

  // Running a count on the first column (in JDBCSQLite always
  // indexed id field) is likely fastest. See various articles on StackOverflow.
  private val countPS: PreparedStatement =
    db.prepareStatement(s"SELECT Count(1) FROM $collectionName")




  /** Inserts or updates the database.
    * 
    * The action depends on the builder, and it's configuration, as supplied. 
    */
  private def upsert(
    ps: PreparedStatement
  )
      : Boolean =
  {


    log(s"SQLite createUpdate")

    try {
      val rs = ps.executeUpdate()


      // look for the int return containing 0
      (rs != 0)

    }
    catch {
      case e: Exception => {
        log(s"unable to upsert to database: $collectionName")
        false
      }
    }
  }


  def ^(
    id: Long,
    g: Giver
  )
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )

    val b = new generic.JDBCMultiBuilder2(
      insertPS
    )
    log(s"sql insert giver id: $id")
    giverTakerBridge(b, g)
    val ps = b.result()
    ps.setLong(1, id)
    upsert(ps)
  }

  def +(
    g: Giver
  )
      : Long =
  {

    val b = new generic.JDBCMultiBuilder2(
      appendPS
    )

    giverTakerBridge(b, g)
    val ps = b.result()
    ps.setNull(1, Types.BIGINT)

    log(s"sql append ")

    try {
      val rs = ps.executeUpdate()


      // look for the generated key
      val retIdRS = ps.getGeneratedKeys()
      if (!retIdRS.next()) {
        error("append to db, no ID returned by database collectionName: $collectionName")
        NullID
      }
      else retIdRS.getLong(1)

    }
    catch {
      case e: Exception => {
log(e)
        log(s"unable to append to database collectionName: $collectionName")
        NullID
      }
    }
  }
  
  def apply(
id: Long,
 f: (Giver) ⇒ Unit
)
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )

    log(s"long apply: collectionName:$collectionName id:$id")
    var rs: ResultSet = null
    readPS.setLong(1, id)
    try {
      rs = readPS.executeQuery()
      if (rs.next()) {
        f(new generic.JDBCMultiBreaker(rs))
        true
      }
      else false
    }
    catch {
      case e: Exception => {
        log(s"unable to read database: $collectionName id: $id:\n" + e)
        false
      }
    }
    finally {
      // NB: No-op if closed
      rs.close()
    }
  }

  def ~(id: Long, g: Giver)
      : Boolean =
  {

    val b = new generic.JDBCMultiBuilder2(
      updatePS
    )
    giverTakerBridge(b, g)
    val ps = b.result()
//val id = b.givenId
    ps.setLong(lastPSFieldIdx, id)
   upsert(ps)
  }

//TODO: is this generic?
  def -(id: Long)
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )

    deletePS.setLong(1, id)
    log(s"SQLite delete id: $id")
    try {
      deletePS.execute()
      true
    }
    catch {
      case e: Exception => {
        log(s"unable to delete from database: $collectionName id: $id")
        false
      }

    }
  }

  def foreach(f: (Giver) ⇒ Unit)
      : Boolean =
{
    val st: Statement = db.createStatement()
    var rs: ResultSet = null
    try {
      // Running a count on the first column (an always indexed id field)
      // is likely fastest. See various articles on StackOverflow.
      rs = foreachPS.executeQuery()
      while (rs.next()) {
        // TODO: Can reuse
        f(new generic.JDBCMultiBreaker(rs))
      }
      true
    }
    catch {
      case e: Exception => {
        log(s"unable to foreach database: $collectionName\n" + e)
        false
      }
    }
    finally {
      // NB: No-op if closed
      rs.close()
    }
  }



  //--------------
  // -- Objects --
  // -------------

  def insert[A](
    id: Long,
    o: A
  )(implicit ms: MultiTakerFieldSelect[A])
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )

    val b = new generic.JDBCMultiBuilder2(
      insertPS
    )
    log(s"sql insert giver id: $id")

    ms(b, o)

    val ps = b.result()
    ps.setLong(1, id)
    upsert(ps)
  }

  def append[A](
    o: A
  )(implicit ms: MultiTakerFieldSelect[A])
      : Long =
  {

    val b = new generic.JDBCMultiBuilder2(
      appendPS
    )

    ms(b, o)

    val ps = b.result()
    ps.setNull(1, Types.BIGINT)

    log(s"sql append giver")

    try {
      val rs = ps.executeUpdate()


      // look for the generated key
      val retIdRS = ps.getGeneratedKeys()
      if (!retIdRS.next()) {
        error("append to db, no ID returned: database: $collectionName")
        NullID
      }
      else retIdRS.getLong(1)

    }
    catch {
      case e: Exception => {
        log(s"unable to append to database: $collectionName")
        NullID
      }
    }
  }
  


  def update[A](
 id: Long,
    o: A
  )(implicit ms: MultiTakerFieldSelect[A])
      : Long =
  {
    val b = new generic.JDBCMultiBuilder2(
      updatePS
    )

    ms(b, o)

    val ps = b.result()
//val id = b.givenId
    ps.setLong(lastPSFieldIdx, id)
    if(upsert(ps)) id
    else NullID
  }



  //--------------
  // -- General --
  // -------------

  def size()
      : Long =
  {
    var rs: ResultSet = null
    try {
      // Running a count on the first column (an always indexed id field)
      // is likely fastest. See various articles on StackOverflow.
      rs = countPS.executeQuery()
      rs.getInt(1).toLong
    }
    catch {
      case e: Exception => {
        log(s"unable to count rows in database: $collectionName")
        -1
      }
    }
    finally {
      // NB: No-op if closed
      rs.close()
    }
  }


  def genreString: String = "SQLiteJDBC"
  def klassString: String = "Collection"


  def addString(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "collectionName: "
    b append collectionName

  }


}//Collection



object Collection {

  def testCollection(db: SyncConnection)
 : Generator = 
{
    import freight.objects.Paper
    db.collection("poems", Paper)
  }

}
