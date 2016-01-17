package freight
package interface
package db
package generic

import java.util.Locale
import java.sql.{Types, PreparedStatement}
import javax.sql.rowset.serial.SerialBlob

//TEMP:
import core.util.PreparedStatementPrintable


/** Binds a merge statement when given to a takeable.
  * 
  * The returned statement is picked from between two prepared
  * statements for insert and update.
  *
  * The returned SQL is usually an auto incrementing merge, i.e. if the id
  * is `NullID` then auto insert, else update. However, if
  * 'asInsert' is true, the merge action is overridden. In
  * that case, the SQL returned is either an insert or an update,
  * depending on the value of 'preserveId'
  *
  * The id supplied through any calls to this builder can be
  * overriden, in all circumstances, by the 'overrideId' parameter.
  *
  * @param insert a prepared statement to be populated by
  *  this builder
  * @param update a prepared statement to be populated by
  *  this builder
  * @param overrideId override the id supplied
  * @param asInsert request an insert or update.
  * @param preserveId if true, update, else insert. 
  */
//* @param forceInsert force the builder to generate an sql insert statement
//* @param mkNull Make the results for the null id.
// TODO: Blob not sorted at all
class JDBCWritingMultiBuilder(
  insert: PreparedStatement,
  update: PreparedStatement,
  asInsert : Boolean,
  preserveId : Boolean
)
    extends JDBCMultiBuilder
{

  private var firstLong = true



  // Next two variable left public
  //as useful for method returns
  var wasNew = false

  // Requested id, either the first long, or an override
  var requestedId = NullID


  def long(fieldName: String, v: Long) {

    // first long is the id
    if (firstLong) {
      firstLong = false

      requestedId = 
if(asInsert && !preserveId) NullID
else v

      if(!asInsert && (requestedId > NullID)) {
        // Update. Nothing bound immediately
        s = update
        // wasNew at default
      }
      else {
        // Insert
        s = insert
        wasNew = true

        // bind immediately
        i += 1

        // null auto-insert or manual insert?
        if  (requestedId == NullID) {
          // set a proper null for auto-incrementing
          s.setNull(i, Types.BIGINT)
        }
        else  {
          // set the id to whatever requested or found in the object.
          s.setLong(i, requestedId)
        }
      }
    }
    else  {
      i += 1
      s.setLong(i, v)
    }

  }

}//JDBCWritingMultiBuilder



object JDBCWritingMultiBuilder {



  /*
   import freight._
   import freight.interface.db.generic._
   JDBCWritingMultiBuilder.test()
   */

  //TODO: Full rework required
/*
  def test() {
    import freight.objects.Paper
    import freight.interface.db.sqliteJDBC._

    // Make two prepared statements
    val db = SyncConnection.testDB
    val c = Collection.testCollection(db)

    val s = JDBCSchemaBuilder.testSchema()

    val iR = s.insertResult()
    val uR = s.updateResult()

    val psI = c.db.prepareStatement(iR)
    val psU = c.db.prepareStatement(uR)



    println("\n\nPositive id : 1")

    val b1 = new JDBCWritingMultiBuilder(
      insert =  new PreparedStatementPrintable(psI, iR),
      update =  new PreparedStatementPrintable(psU, uR),
      asInsert = false,
      preserveId = false
    )

    Paper.multiTakerFieldSelect(b1, Paper.testPaper)

    println
    println("asInsert = false, preserveId = false:")
    println(b1.result)



    val b2 = new JDBCWritingMultiBuilder(
      insert =  new PreparedStatementPrintable(psI, iR),
      update =  new PreparedStatementPrintable(psU, uR),
      asInsert = true,
      preserveId = false
    )

    Paper.multiTakerFieldSelect(b2, Paper.testPaper)

    println
    println("asInsert = true, preserveId = false:")
    println(b2.result)



    val b3 = new JDBCWritingMultiBuilder(
      insert =  new PreparedStatementPrintable(psI, iR),
      update =  new PreparedStatementPrintable(psU, uR),
      asInsert = true,
      preserveId = true
    )

    Paper.multiTakerFieldSelect(b3, Paper.testPaper)

    println
    println("asInsert = true, preserveId = true:")
    println(b3.result)




    println("\n\n Auto-updating id : 0")

    val b01 = new JDBCWritingMultiBuilder(
      insert =  new PreparedStatementPrintable(psI, iR),
      update =  new PreparedStatementPrintable(psU, uR),
      asInsert = false,
      preserveId = false
    )

    Paper.multiTakerFieldSelect(b01, Paper.testNewPaper)

    println
    println("asInsert = false, preserveId = false:")
    println(b01.result)



    val b02 = new JDBCWritingMultiBuilder(
      insert =  new PreparedStatementPrintable(psI, iR),
      update =  new PreparedStatementPrintable(psU, uR),
      asInsert = true,
      preserveId = false
    )

    Paper.multiTakerFieldSelect(b02, Paper.testNewPaper)

    println
    println("asInsert = true, preserveId = false:")
    println(b02.result)



    val b03 = new JDBCWritingMultiBuilder(
      insert =  new PreparedStatementPrintable(psI, iR),
      update =  new PreparedStatementPrintable(psU, uR),
      asInsert = true,
      preserveId = true
    )

    Paper.multiTakerFieldSelect(b03, Paper.testNewPaper)

    println
    println("asInsert = true, preserveId = true:")
    println(b03.result)



    println("\n\n Overriding id to 0")

    val b11 = new JDBCWritingMultiBuilder(
      insert =  new PreparedStatementPrintable(psI, iR),
      update =  new PreparedStatementPrintable(psU, uR),
      asInsert = true,
      preserveId = false
    )

    Paper.multiTakerFieldSelect(b11, Paper.testNewPaper)


    println
    println("asInsert = false, preserveId = false:")
    println(b11.result)



    val b12 = new JDBCWritingMultiBuilder(
      insert =  new PreparedStatementPrintable(psI, iR),
      update =  new PreparedStatementPrintable(psU, uR),
      asInsert = true,
      preserveId = false
    )

    Paper.multiTakerFieldSelect(b12, Paper.testNewPaper)

    println
    println("asInsert = true, preserveId = false:")
    println(b12.result)



    val b13 = new JDBCWritingMultiBuilder(
      insert =  new PreparedStatementPrintable(psI, iR),
      update =  new PreparedStatementPrintable(psU, uR),
      asInsert = true,
      preserveId = true
    )

    Paper.multiTakerFieldSelect(b13, Paper.testNewPaper)

    println
    println("asInsert = true, preserveId = true:")
    println(b13.result)



  }
*/


}//JDBCWritingMultiBuilder
