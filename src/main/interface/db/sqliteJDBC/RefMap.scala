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

import collection.mutable.ArrayBuffer


/*
 import freight._
 import freight.interface.db.sqliteJDBC._
 import freight.objects.Paper

 //import freight.canspeak.XML
 val db = SyncConnection.testDB
 val r = db.refMap("poems_term_parents")
 val r = db.refMap("poems_term_refs")
 val r = db.refMap("f_label_array_refs")


 */

/** An SQLite RefMap.
  *
  * @define coll sqlite RefMap
  */
//TODO: Much could go to JDBCRefMap
// Single threaded versions ccan use some class val?
//https://sqlite.org/autoinc.html
final class RefMap(
//TODO: Case for single val multi key bulk insert.
  val db: Connection,
  val collectionName: String
)
    extends generic.JDBCRefMap
{


  // Ok - AUTO_INCREMENT is AUTOINCREMENT
  // and can only be set on integers.
  val isNew: Boolean =
    if (!tableExists(db, collectionName) ) {
      //log(s"creating db RefMap: $collectionName")

      //CREATE TABLE poems ( id integer PRIMARY KEY, language varchar(255) NOT NULL, title varchar(255) NOT NULL, description varchar(255) NOT NULL, body blob NOT NULL)
      val st: Statement = db.createStatement()
      try {
        // NB: Cann't use WITHOUT ROWID or  PRIMARY KEY, it makes unique, which is not wanted.
        st.execute(s"CREATE TABLE $collectionName (k long, v long)")
        st.execute(s"CREATE INDEX ${collectionName}_kidx ON $collectionName (k)")
        st.execute(s"CREATE INDEX ${collectionName}_vidx ON $collectionName (v)")
        true
      }
      catch {
        case e: Exception => {
          println(e)
          error(s"unable to create refmap table: $collectionName")

          false
        }
      }
    }
    else false

  //(id int NOT NULL AUTO_INCREMENT,name varchar(30),PRIMARY KEY(id)

  //TODO: What are these coloums if the id is missing?
  // cached statements
  private val readSQL: PreparedStatement = db.prepareStatement(s"SELECT v FROM $collectionName WHERE k = ?")

  private val byValReadSQL: PreparedStatement = db.prepareStatement(s"SELECT k FROM $collectionName WHERE v = ?")

  //INSERT INTO poems VALUES(null, ?, ?, ?, ?)
  private val insertSQL: PreparedStatement = db.prepareStatement(s"INSERT INTO $collectionName VALUES(?, ?)")

  //UPDATE poems SET language=?,title=?,description=?,body=? WHERE id = ?
  private val updateSQL: PreparedStatement = db.prepareStatement(s"UPDATE $collectionName SET k=?,v=? WHERE 1 = ?")

  //println("update:"+schema.updateResult())

  private val deleteSQL: PreparedStatement = db.prepareStatement(s"DELETE FROM $collectionName WHERE k = ?")

  private val byValDeleteSQL: PreparedStatement = db.prepareStatement(s"DELETE FROM $collectionName WHERE v = ?")

  private val deleteSQLKV: PreparedStatement = db.prepareStatement(s"DELETE FROM $collectionName WHERE k = ? AND v = ?")


  def ^(k: Long, v: Long)
      : Boolean =
  {
    insertSQL.setLong(1, k)
    insertSQL.setLong(2, v)
    //log(s"RefMap insert k:$k vals:$v")

    var rs: ResultSet = null
    try {
      (insertSQL.executeUpdate() > 0)
    }
    catch {
      case e: Exception => {
        log(s"unable to append to database: $collectionName k: $k")
        false
      }
    }
  }

  def ^(k: Long, v: TraversableOnce[Long])
      : Boolean =
  {
    println(s"RefMap mass insert k:$k vals:$v")
    val b = new StringBuilder("INSERT INTO ")
    b ++= collectionName
    b ++= "(k,v) VALUES"
    val keyAsString = k.toString
    //val sl = s"INSERT INTO $collectionName VALUES(?, ?)"
    var first = true
    v.foreach { v =>
      if (first) first = false
      else b += ','
      b += '('
      b ++= keyAsString
      b += ','
      b append v
      b += ')'
    }
    log(s"mass insert k:$k vals:$v")

val stmt = b.result

    val st: Statement = db.createStatement()
    try {
      (st.executeUpdate(stmt) > 0)
    }
    catch {
      case e: Exception => {
        println(e)
        log(s"unable to bulk append to database: $collectionName k:$k v:$v")
        false
      }
    }
  }


  def apply(id: Long)
      : ArrayBuffer[Long] =
  {
    readSQL.setLong(1, id)
    val b = ArrayBuffer[Long]()
    println(s"RefMap apply id:$id")

    var rs: ResultSet = null
    try {
      rs = readSQL.executeQuery()
      while(rs.next()) {
        b += rs.getLong(1)
      }
      b.result()
    }
    catch {
      case e: Exception => {
        log(s"unable to read database: $collectionName id: $id")
        ArrayBuffer.empty
      }
    }
    finally {
      rs.close()
    }
  }

  /** Select keys by value in this $coll, by id.
    *
    */
  def keysByVal(v: Long)
      : ArrayBuffer[Long] =
  {
    byValReadSQL.setLong(1, v)
    val b = ArrayBuffer[Long]()
    //log(s"by val: val:$v")

    var rs: ResultSet = null
    try {
      rs = byValReadSQL.executeQuery()
      while(rs.next()) {
        b += rs.getLong(1)
      }
      b.result()
    }
    catch {
      case e: Exception => {
        log(s"unable to read database: $collectionName v: $v")
        ArrayBuffer.empty
      }
    }
    finally {
      rs.close()
    }
  }

def distinctKeys(f: (Long) ⇒ Unit) {
    val st: Statement = db.createStatement()
    var rs: ResultSet = null
    //log(s"foreach in $collectionName")
    try {
      rs = st.executeQuery(s"SELECT DISTINCT k FROM $collectionName")
      while(rs.next()) {
        f(rs.getLong(1))
      }

    }
    catch {
      case e: Exception => {
        log(s"unable to keys in database: $collectionName")
      }

    }
    finally {
      rs.close()
    }
}

  def foreach(f: ((Long, Long)) ⇒ Unit) {
    val st: Statement = db.createStatement()
    var rs: ResultSet = null
    //log(s"foreach in $collectionName")
    try {
      rs = st.executeQuery(s"SELECT * FROM $collectionName")
      while(rs.next()) {
        f(rs.getLong(1), rs.getLong(2))
      }

    }
    catch {
      case e: Exception => {
        log(s"unable to foreach in database: $collectionName")
      }

    }
    finally {
      rs.close()
    }
  }

  def -(id: Long)
      : Boolean =
  {
    deleteSQL.setLong(1, id)
    println(s"RefMap delete in $collectionName key: $id")
    try {
      deleteSQL.executeUpdate()
      true
    }
    catch {
      case e: Exception => {
        log(s"unable to delete from database: $collectionName id: $id")
        false
      }

    }
  }

  def -(k: TraversableOnce[Long])
      : Boolean =
  {
    val st: Statement = db.createStatement()
    //log(s"bulk delete in $collectionName keys:$k")
    try {
      ( st.executeUpdate(s"DELETE FROM $collectionName WHERE 1 IN (${k.mkString(",")})") > 0)
    }
    catch {
      case e: Exception => {
        log(s"unable to bulk delete from database: $collectionName")
        false
      }

    }
  }

/*
  def -(k: TraversableOnce[Long], v: Long)
 : Boolean =
  {
    deleteSQLKV.setLong(1, k)
    deleteSQLKV.setLong(2, v)
    log(s"delete in $collectionName k: $k v: $v")
    try {
      (deleteSQL.executeUpdate() > 0)
    }
    catch {
      case e: Exception => {
        log(s"unable to kv delete from database: $collectionName k: $k v:$v")
        false
      }

    }
  }
*/

  def removeByVal(v: Long)
      : Boolean =
  {
    byValDeleteSQL.setLong(1, v)
    //log(s"by val delete val:$v")
    try {
      (byValDeleteSQL.executeUpdate() > 0)
    }
    catch {
      case e: Exception => {
        log(s"unable to delete from database: $collectionName v: $v")
        false
      }

    }
  }

  def size()
      : Long =
  {
    val st: Statement = db.createStatement()
    var rs: ResultSet = null
    try {
      // Running a count on the first column (usually an indexed id field)
      // is likely fastest. See various articles on StackOverflow.
      rs = st.executeQuery(s"SELECT Count(1) FROM $collectionName")
      //TODO...
      rs.getInt(1).toLong
    }
    catch {
      case e: Exception => {
        log(s"unable to count rows in database: $collectionName")
        -1
      }
    }
    finally {
      rs.close()
    }
  }

  def size(id: Long)
      : Long =
  {
    val st: Statement = db.createStatement()
    var rs: ResultSet = null
    try {
      // Running a count on the first column (an always indexed id field)
      // is likely fastest. See various articles on StackOverflow.
      rs = st.executeQuery(s"SELECT Count(1) FROM $collectionName WHERE 1=$id")
      //TODO...
      rs.getInt(1).toLong
    }
    catch {
      case e: Exception => {
        log(s"unable to count rows in database: $collectionName id:$id")
        -1
      }
    }
    finally {
      rs.close()
    }
  }


  def genreString: String = "SQLiteJDBC"
  def klassString: String = "RefMap"

  def addString(b: StringBuilder) : StringBuilder =
  {
    b ++= "collectionName: "
    b ++= collectionName
b
  }


}//RefMap



object RefMap {

  /*
   import freight._
   import freight.interface.db.sqliteJDBC._
   RefMap.test()
   */

  def test() {
    val db = SyncConnection.testDB
    val r = db.refMap("poems_test")
    try {
      println(" Ensure empty")
      r.foreach(println)

      println("^ one")
      r^(1,2)
      r.foreach(println)

      println("- one")
      r-(1)
      r.foreach(println)

      println("^ append not merge")
      r^(1,2)
      r^(1,2)
      r.foreach(println)
      r-(1)


      println("^ many")
      r^(2, Seq[Long](2, 3, 5, 6))

      r.foreach(println)

      println("apply k=2")
      println(r(2))

      println("keysByVal k=5")
      println(r.keysByVal(5))


      println("- many")
      r^(1,2)
      r^(2, Seq[Long](2, 5, 6))
      r-(Seq[Long](1, 2))
      r.foreach(println)

      println("removeByVal v=3")
      r^(2, Seq[Long](2, 3, 6))
      r.removeByVal(3)
      r.foreach(println)
    }
    catch {
      case e: Exception => println(e)
    }
    finally{
      r.clean()
      db.close()
    }
  }


def apply(
//TODO: Case for single val multi key bulk insert.
  connection: Connection,
  collectionName: String
)
: RefMap =
{

new RefMap(
//TODO: Case for single val multi key bulk insert.
  connection,
  collectionName
)
}
}//RefMap
