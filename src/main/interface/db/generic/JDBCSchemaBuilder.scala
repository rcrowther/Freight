package freight
package interface
package db
package generic



/** Generates sql statements suitable for use with JDBC.
  *
  * These statements vary somewhat. See the methods `createResult`,
  * `insertResult`, and `updateResult`.
  *  
  * @param colName name of this collection as a string
  * @param createPrefix prefix to the create staement. Must
  *  include the id key, e.g. "CREATE TABLE &lt;colName&gt; ( id integer PRIMARY KEY"
  */
// recordCount may be deprecated
// perhaps only need useAutoincrement ?
// https://www.sqlite.org/datatype3.html
// https://db.apache.org/ojb/docu/guides/jdbc-types.html
class JDBCSchemaBuilder(
  colName : String,
  createPrefix: String
)
    extends DescriptiveTaker
{

  private val createB = new StringBuilder(createPrefix)
  private val insertB = new StringBuilder(s"INSERT INTO $colName VALUES(")
  private val updateB = new StringBuilder(s"UPDATE $colName SET ")

  // Tracks update statement parameters
  private var first = true
  private var firstUpdate = true

  // Both statements need parameters listed after the id parameter
  // create gets id params from createPrefix, insert only needs a '?',
  // provided by the logic in long()
  //If no ROWID is specified on the insert, or if the specified ROWID has a value of NULL
  // Make a noId insert...
  private def createInsert(fieldName: String, typStr: String) {
    insertB ++= ", ?"
    createB += ','
    createB ++= fieldName
    createB += ' '
    createB ++= typStr
    createB ++= " NOT NULL"
  }

  private def updateAppend(fieldName: String) {
    // Comma if necessary
    if (firstUpdate) firstUpdate = false
    else  updateB += ','

    updateB ++= fieldName
    updateB ++= "=?"
  }

  def boolean(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "boolean")
    updateAppend(fieldName)
  }

  def short(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "smallint")
    updateAppend(fieldName)
  }

  def int(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "integer")
    updateAppend(fieldName)
  }

  def long(fieldName: String, v: String, typ: freight.TypeMark) {

    if (first) {
      // Id field

      // for create, ignore. The parameter should be in 'createPrefix'
      // For insert, add a '?'
      insertB += '?'
      // For update, do nothing. The id will go in the WHERE syntax

      first = false
    }
    else {
      createInsert(fieldName, "bigint")
      updateAppend(fieldName)
    }
  }

  def float(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "real")
    updateAppend(fieldName)
  }

  def double(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "double")
    updateAppend(fieldName)
  }

  def string(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "varchar(255)")
    updateAppend(fieldName)
  }

  def binary(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "blob")
    updateAppend(fieldName)
  }

  def text(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "clob")
    updateAppend(fieldName)
  }

  def time(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "long")
    updateAppend(fieldName)
  }

  def timestamp(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "long")
    updateAppend(fieldName)
  }

  def locale(fieldName: String, v: String, typ: freight.TypeMark) {
    createInsert(fieldName, "varchar(32)")
    updateAppend(fieldName)
  }

  def clear() = error("clear() called on schema builder!")

  /** Returns a JDBC sql create statement.
    *
    *  e.g. 
    *  {{{
    * <createPrefix> language varchar(255) NOT NULL, title
    * varchar(255) NOT NULL, description varchar(255) NOT NULL, body
    * blob NOT NULL)
    *  }}}
    */
  def createResult(): String = {
    createB += ')'
    createB.result()
  }

  /** Returns a JDBC sql insert statement.
    *
    *  e.g. 
    *  {{{
    * INSERT INTO <colName> VALUES(?, ?, ?, ?, ?)
    *  }}}
    */
  def insertResult(): String = {
    insertB += ')'
    insertB.result()
  }

  /** Returns a JDBC sql update statement.
    *
    *  e.g. 
    *  {{{ 
    * UPDATE <colName> SET language=?,title=?,description=?,body=?
    * WHERE id = ?
    *  }}}
    */
  def updateResult(): String = {
    updateB ++= " WHERE id = ?"
    updateB.result()
  }


}//JDBCSchemaBuilder



object JDBCSchemaBuilder {

  def testSchema()
      :JDBCSchemaBuilder =
  {
    import freight.objects.Paper
    
    val s = new JDBCSchemaBuilder(
      "poems",
      "CREATE TABLE poems (id integer PRIMARY KEY"
    )

    Paper.descriptiveGiver(s)

    s
  }

  /*
   import freight._
   import freight.interface.db.generic._
   JDBCSchemaBuilder.test()
   */
  def test() {
    val s = testSchema()

    println("createResult:")
    println(s.createResult)
    println("insertResult:")
    println(s.insertResult)
    println("updateResult:")
    println(s.updateResult)

  }

}//JDBCSchemaBuilder
