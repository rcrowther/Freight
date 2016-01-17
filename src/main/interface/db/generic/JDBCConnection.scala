package freight
package interface
package db
package generic


import java.sql.{Connection, DriverManager}


/*
 import freight._
 import freight.interface.db._


 val c = JDBCConnection.testDB
 import freight.objects.Paper
 val c = db.collection("poems", Paper.multiTransformer)



 // Merge
 c<(Paper.testNewPaper)

 // Read
 val o = c("0")

 // delete
 c-(0)
 */

/** Base for JDBC connections.
  *
* @define conn jdbc connection
  */
// TOCONSIDER: this currently is empty and looks useless?
// See sqliteJDBC.SyncConnection for common methods
trait JDBCConnection
extends core.collection.Connection
 {

  //def driver: String

  //def username: String,
  //def  password: String

  //println(s"java classpath: ${System.getProperty("java.class.path")}")

  //Class.forName(driver)
  //classOf[driver]
  //DriverManager.registerDriver(new classOf[driver])
  //DriverManager.registerDriver(new com.mysql.jdbc.Driver)
  //DriverManager.registerDriver(new org.sqlite.JDBC)
  //DriverManager.registerDriver(driver)



/*
  def collection(
    colName: String,
    meta: CompanionMeta
  ) : GiverTakable


  def close() : Boolean

  def toString() : String
*/

}//Lite


/*
 object JDBCConnection {
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

 def testDB:  JDBCConnection = {
 //import freight.objects.Paper

 new JDBCConnection(
 url = "jdbc:mysql://localhost:8889/mysql",
 driver = "com.mysql.jdbc.Driver",
 username = "root",
 password = "root"
 )
 }

 }
 */
