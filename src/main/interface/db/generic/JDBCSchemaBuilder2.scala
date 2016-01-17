package freight
package interface
package db
package generic



/** Generates partial sql statements suitable for use with JDBC.
  *
  * The statements are fragments representing fields for an
  * object. They can be used to building full SQL statements for
  * querying.
  */
class JDBCSchemaBuilder2()
    extends DescriptiveTaker
{

  private val createB = new StringBuilder()
  private val fieldB = new StringBuilder()
  private val titledFieldB = new StringBuilder()

  // Tracks update statement parameters
  private var first = true
  private var count = 0

  /** Counts the fields called on the builder.
    */
  def elementCount: Int = count


  private def appendElement(fieldName: String, typStr: String) {
    if (first) {
      first = false

      // NB: this needed for JDBCSQLite auto-numbering
      createB ++= "id integer PRIMARY KEY"
    }
    else  {
      createB += ','
      fieldB += ','
      titledFieldB += ','

    // TOCONSIDER: NOT NULL makes sense for Freight, but others?
    createB ++= fieldName
    createB += ' '
    createB ++= typStr
    createB ++= " NOT NULL"
    }




    fieldB ++= "?"

    titledFieldB ++= fieldName
    titledFieldB ++= "="
    titledFieldB ++= "?"

    count += 1
  }



  def boolean(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "boolean")
  }

  def short(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "smallint")
  }

  def int(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "integer")
  }

  def long(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "bigint")
  }

  def float(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "real")

  }

  def double(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "double")
  }

  def string(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "varchar(255)")
  }

  def binary(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "blob")
  }

  def text(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "clob")
  }

  def time(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "long")
  }

  def timestamp(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "long")
  }

  def locale(fieldName: String, v: String, typ: freight.TypeMark) {
    appendElement(fieldName, "varchar(32)")
  }

  def clear() = error("clear() called on schema builder!")

  /** Returns a JDBC sql title, typed and NOT NULL statement.
    *
    *  e.g. 
    *  {{{
    * language varchar(255) NOT NULL, title
    * varchar(255) NOT NULL, description varchar(255) NOT NULL, body
    * blob NOT NULL
    *  }}}
    */
  def createResult(): String = {
    createB.result()
  }

  /** Returns a JDBC sql field statement.
    *
    *  e.g. 
    *  {{{
    * ?, ?, ?, ?, ?
    *  }}}
    */
  def fieldResult(): String = {
    fieldB.result()
  }

  /** Returns a JDBC sql titled field statement.
    *
    *  e.g. 
    *  {{{ 
    * language=?,title=?,description=?,body=?
    *  }}}
    */
  def titledFieldResult(): String = {
    titledFieldB.result()
  }


}//JDBCSchemaBuilder



object JDBCSchemaBuilder2 {

  def testSchema()
      :JDBCSchemaBuilder2 =
  {
    import freight.objects.Paper
    
    val s = new JDBCSchemaBuilder2()

    Paper.descriptiveGiver(s)

    s
  }

  /*
   import freight._
   import freight.interface.db.generic._
   JDBCSchemaBuilder2.test()
   */
  def test() {
    val s = testSchema()

    println("createResult:")
    println(s.createResult)
    println("fieldResult:")
    println(s.fieldResult)
    println("titledFieldResult:")
    println(s.titledFieldResult)
    println("elementCount:")
    println(s.elementCount)
  }

}//JDBCSchemaBuilder
