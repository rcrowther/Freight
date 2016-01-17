package freight
package interface
package db
package generic

import java.sql.ResultSet
import java.sql.DatabaseMetaData
import java.sql.Statement
import java.sql.Connection



/** Realises generic methods for use in JMDB collection interfaces.
*
* @define coll jmbc collection
*/
//TODO: Threads, whatever.
//TODO: clean and tableExists should be shared with Connection?
//https://sqlite.org/autoinc.html
trait JDBCCollectionGeneric
{
  def db: Connection
  def collectionName: String


  protected def tableExists(db: Connection, collectionName: String) : Boolean = {
    val m : DatabaseMetaData = db.getMetaData()
    // check if the table is there
    var rs : ResultSet  = null
    try {
      rs = m.getTables(null, null, collectionName, null)
      rs.next()
    }
    catch {
      case e: Exception => {
        log("unable to determine if database exists. Trying subsequent code anyway...")
        false
      }
    }
    finally {
      rs.close()
    }
  }

  def clear()
      : Boolean =
  {
    log2(s"clearing db collection: $collectionName")
    val clear: Statement = db.createStatement()
    try {
      clear.execute(s"DELETE FROM $collectionName")
      true
    }
    catch {
      case e: Exception => {
        log(s"unable to clear database: $collectionName")
        false
      }
    }
  }

  def clean()
      : Boolean =
  {
    log4(s"dropping db collection: $collectionName")
    val clean: Statement = db.createStatement()
    try {
      clean.execute(s"DROP TABLE $collectionName")
      true
    }
    catch {
      case e: Exception => {
        log(s"unable to drop database: $collectionName")
        false
      }
    }
  }


}//JDBCCollectionGeneric

