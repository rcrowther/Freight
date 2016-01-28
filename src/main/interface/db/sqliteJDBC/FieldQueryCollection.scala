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

import generic.JDBCSchemaBuilder2

/*
 import freight._
 import freight.interface.db.sqliteJDBC._

 val db = SyncConnection.testDB
 val c = FieldQueryCollection.testFieldQueryCollection(db)
// or...
 import core.objects.WeighedTitle
 val c = db.humanCollection("poems_terms", WeighedTitle)

 // Read
 c.get(1, Some(2))

 c.get(Seq(2,4,7))

 // find
 c.find("")
 val b = new interface.html.ObjectBuilder()
 val b = interface.html.ObjectAnchoredFieldBuilder("article", 2, "article")
 c.foreach(Seq(0,1,2), Seq(2), b)
// for example...
 c.foreach(Seq(0,2), (g: Giver) => {println(g.long); println(g.string)})
 */

/** An SQLite human collection.
  *
  * See [[core.iface.FieldQueryCollection]]
  * 
  * @define coll sqlite human
  *
  * @param collName name name of the collection 
  * @param titleCollName name of the column to use for title text
  * @param descriptionCollName name of the column to use for descriptive text
  */
//TODO: Threads, whatever.
// Single threaded versions ccan use some class val?
//https://sqlite.org/autoinc.html
final class FieldQueryCollection(
  val db: Connection,
  val collectionName: String,
  val meta: CompanionMeta
)
    extends FieldQueryable
    with generic.JDBCCollectionGeneric
{



  def variantFieldSelector = core.objects.generic.VariantMultiFieldSelector

  // cached statements

  // NB: Prepared statements columnames don't work on the SQLite
  // database.
  private val sortedIndicesSQL: String =
    s"SELECT id FROM $collectionName ORDER BY "

///////////////////////////


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


 

//////////////////////////////////
  private def executeSeqPS(
    ps: PreparedStatement,
    f: (Giver) => Unit
  )
      : Boolean =
  {
    var rs: ResultSet = null
    val bk = new generic.JDBCMultiBreaker(rs)

    try {
      rs = ps.executeQuery()

      while (rs.next()) {
        bk.reload(rs)
        f(bk)
      }
true
    }
    catch {
      case e: Exception => {
        log(s"unable to foreach|find|field-select database: $collectionName" + e)
        false
      }
    }
    finally {
      // Calling the method close on a ResultSet object that is
      // already closed is a no-op
      rs.close()
    }
  }

   def apply(
    fieldIdxs: Seq[Int],
    id: Long,
    f: (Giver) => Unit
  ) 
: Boolean =
  {
    elErrorIf(
      (fieldIdxs.find((x) => { ( x >= meta.columnMap.size || x < 0)}) != None),
       s"fieldIdxs index out of range collectionName: $collectionName fieldIdxs: $fieldIdxs"
     )
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )




    val fieldNames = fieldIdxs.map(meta.fieldNames(_))
    val sql = fieldNames.mkString("SELECT ", ",", s" FROM $collectionName WHERE id = $id")

    log(s"SQLite apply sql: $sql")

    // Prepare (very likely to be repetitious)
    val ps: PreparedStatement = db.prepareStatement(sql)
    var rs: ResultSet = null

    try {
      rs = ps.executeQuery()

      if (rs.next()) {
        f(new generic.JDBCMultiBreaker(rs))
        true
      }
      else false


    }
    catch {
      case e: Exception => {
        log(s"unable to field-select database: $collectionName" + e)
       false
      }
    }
    finally {
      // Calling the method close on a ResultSet object that is
      // already closed is a no-op
      rs.close()
    }
  }


  //NB: Terrible method to implement in JDBC, but needed for display
  //lists.
  //http://stackoverflow.com/questions/178479/preparedstatement-in-clause-alternatives
  override def apply(
    fieldIdxs: Seq[Int],
    idxs: Seq[Long],
    f: (Giver) => Unit
  )
      : Boolean =
  {
    elErrorIf(
      (fieldIdxs.find((x) => { ( x >= meta.columnMap.size || x < 0)}) != None),
       s"fieldIdxs index out of range collectionName: $collectionName fieldIdxs: $fieldIdxs"
     )
    elErrorIf(
      (idxs.find((x) => { (x < InitialID) }) != None),
       s"idxs index out of range collectionName: $collectionName idxs: $idxs"
     )

    log(s"get: collectionName:$collectionName idxs:$idxs")

    val whereSQL = idxs.mkString("(", ",", ")")
    val fieldNames = fieldIdxs.map(meta.fieldNames(_))
    val sql = fieldNames.mkString("SELECT ", ",", s" FROM $collectionName WHERE id IN $whereSQL")

    val ps = db.prepareStatement(sql)

    executeSeqPS(ps, f)
  }

  def findString(
    fieldIdxs: Seq[Int],
    searchFieldIdx: Int,
    searchData: String,
    f: (Giver) => Unit
  )
      : Boolean =
  {
    elErrorIf(
      (fieldIdxs.find((x) => { ( x >= meta.columnMap.size || x < 0)}) != None),
       s"fieldIdxs index out of range collectionName: $collectionName fieldIdxs: $fieldIdxs"
     )
    elErrorIf(
      (searchFieldIdx >= meta.columnMap.size || searchFieldIdx < 0),
      s"Field index out of range collectionName: $collectionName searchFieldIdx: $searchFieldIdx"
    )


    val fieldNames = fieldIdxs.map(meta.fieldNames(_))
    val whereFieldName = meta.fieldNames(searchFieldIdx)
    val sql = fieldNames.mkString("SELECT ", ",", s" FROM $collectionName WHERE $whereFieldName=?")



    val ps = db.prepareStatement(sql)
    ps.setString(1, searchData)

    log(s"find: collectionName: $collectionName fieldIdxs: $fieldIdxs searchFieldIdx:$searchFieldIdx searchData:$searchData")

    executeSeqPS(ps, f)
  }

  def findLong(
    fieldIdxs: Seq[Int],
    searchFieldIdx: Int,
    searchData: Long,
    f: (Giver) => Unit
  )
      : Boolean =
  {
    elErrorIf(
      (fieldIdxs.find((x) => { ( x >= meta.columnMap.size || x < 0)}) != None),
       s"fieldIdxs index out of range collectionName: $collectionName fieldIdxs: $fieldIdxs"
     )
    elErrorIf(
      (searchFieldIdx >= meta.columnMap.size || searchFieldIdx < 0),
      s"Field index out of range collectionName: $collectionName searchFieldIdx: $searchFieldIdx"
    )


    val fieldNames = fieldIdxs.map(meta.fieldNames(_))
    val whereFieldName = meta.fieldNames(searchFieldIdx)
    val sql = fieldNames.mkString("SELECT ", ",", s" FROM $collectionName WHERE $whereFieldName=?")



    val ps = db.prepareStatement(sql)
    ps.setLong(1, searchData)

    log(s"find: collectionName: $collectionName fieldIdxs: $fieldIdxs searchFieldIdx:$searchFieldIdx searchData:$searchData")

    executeSeqPS(ps, f)
  }

  def foreach(
    fieldIdxs: Seq[Int],
    sortFieldIdxs: Seq[Int],
    f: (Giver) ⇒ Unit
  )
      : Boolean =
  {
    elErrorIf(
      (fieldIdxs.find((x) => { ( x >= meta.columnMap.size || x < 0)}) != None),
       s"fieldIdxs index out of range collectionName: $collectionName fieldIdxs: $fieldIdxs"
     )
    elErrorIf(
      (sortFieldIdxs.find((x) => { ( x >= meta.columnMap.size || x < 0)}) != None),
       s"sortFieldIdxs index out of range collectionName: $collectionName sortFieldIdxs: $sortFieldIdxs"
     )
    // Prepare this - it repeats. However, it needs SQL stringbuilding
    // NB: For prepared statement - nulls don't work, column
    // definitions don't work, not on SQLite JDBC.


    val fieldNames = fieldIdxs.map(meta.fieldNames(_))
    val prefixSql = fieldNames.mkString("SELECT ", ",", s" FROM $collectionName")

    val sql =
      if(!sortFieldIdxs.isEmpty) {
        val sortFieldNames = sortFieldIdxs.map(meta.fieldNames(_))
        sortFieldNames.mkString(s"$prefixSql ORDER BY ", ",", "")
      }
      else prefixSql

    //log(s"meta.columnTypeMap: ${meta.columnTypeMap}")
    log(s"SQLite foreach sql: $sql")
    val ps = db.prepareStatement(sql)

    executeSeqPS(ps, f)
  }


  override def sortedIndices(sortFieldIdx: Int)
      : Option[Seq[Long]] =
  {
    elErrorIf(
      (sortFieldIdx >= meta.columnMap.size || sortFieldIdx < 0),
      s"Field index out of range collectionName: $collectionName sortFieldIdx: $sortFieldIdx"
    )

    val b = Seq.newBuilder[Long]

    // NB: See note on sortedIndicesSQL
    val sql = sortedIndicesSQL + meta.columnMap(sortFieldIdx)._2
    //log(s"sortedIndices sql: $sql")

    // Don't prepare this - it's numbers and unlikely to repeat.
    val st: Statement = db.createStatement()
    var rs: ResultSet = null
    try {
      rs = st.executeQuery(sql)
      while(rs.next()) {
        val ret = rs.getLong(1)
        b += ret
      }
      Some(b.result())
    }
    catch {
      case e: Exception => {
        log(s"unable to read sorted indices database: $collectionName" + e)
        None
      }
    }
    finally {
      // Calling the method close on a ResultSet object that is
      // already closed is a no-op
      rs.close()
    }
  }

  /*
   val c = db.humanCollection("f_label_array", WeighedTitle)

   c.updateInt(3, Map((1L -> 8)))

   */
  private def executeBatchPS(ps: PreparedStatement)
      : Boolean =
  {

    try {
      ps.executeBatch()
      true
    }
    catch {
      case e: Exception => {
        log(s"unable to foreach database: $collectionName\n" + e)
        false
      }
    }
  }


  def updateInt(
    fieldIdx: Int,
    data: Seq[(Long, Int)]
  )
      : Boolean =
  {
    elErrorIf(
      (fieldIdx >= meta.columnMap.size || fieldIdx < 0),
      "Field index out of range collectionName: $collectionName fieldIdx: $fieldIdx"
    )

    // Prepare - it's a batch.
    val stFieldName = meta.fieldNames(fieldIdx)
    // SQLite diver doesn't like inserted columnames in an update?
    log(s"data : $data")
    log(s"sql: UPDATE $collectionName SET $stFieldName=? WHERE id=?")
    val updatePS = db.prepareStatement(s"UPDATE $collectionName SET $stFieldName=? WHERE id=?")

    data.foreach{ case(id, v) =>
      updatePS.setInt(1, v)
      updatePS.setLong(2, id)
      updatePS.addBatch()
    }

    log(s"updateColumnInt: collectionName: $collectionName fieldIdx: $fieldIdx")

    executeBatchPS(updatePS)
  }

  def updateString(
    fieldIdx: Int,
    data: Seq[(Long, String)]
  )
      : Boolean =
  {
    elErrorIf(
      (fieldIdx >= meta.columnMap.size || fieldIdx < 0),
      "Field index out of range collectionName: $collectionName fieldIdx: $fieldIdx"
    )

    // Prepare - it's a batch with strings.
    val stFieldName = meta.fieldNames(fieldIdx)
    // SQLite diver doesn't like inserted columnames in an update?
    val updatePS = db.prepareStatement(s"UPDATE $collectionName SET $stFieldName=? WHERE id=?")

    data.foreach{ case(id, v) =>
      updatePS.setString(1, v)
      updatePS.setLong(2, id)
      updatePS.addBatch()
    }

    log(s"updateColumnInt: collectionName: $collectionName fieldIdx: $fieldIdx")

    executeBatchPS(updatePS)
  }


  def genreString: String = "SQLiteJDBC"
  def klassString: String = "FieldQueryCollection"

  def addString(b: StringBuilder) : StringBuilder =
  {
    b ++= "collectionName: "
    b ++= collectionName
b
  }

}//FieldQueryCollection



object FieldQueryCollection {


  def testFieldQueryCollection(db: SyncConnection)
      : FieldQueryable =
  {
    import freight.objects.Paper
    db.fieldQueryCollection("poems", Paper)
  }


  def apply(
    db: Connection,
    collectionName: String,
    meta: CompanionMeta
  )
      : FieldQueryable =
  {
    new FieldQueryCollection(
      db,
      collectionName,
      meta
    )
  }

}//FieldQueryCollection
