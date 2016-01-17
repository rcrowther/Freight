package freight
package core
package util

import java.sql.PreparedStatement
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement



/** Replace a prepared statement and view the final string.
  */
// TODO: A non- prep returning version.
final class PreparedStatementPrintable (
  stmt: PreparedStatement,
  sql: String
)
    extends java.sql.PreparedStatement
{

  /** As seen from class PreparedStatementPrintable, the missing signatures are as follows.
    *  For convenience, these are usable as stub implementations.
    */
  // Members declared in java.sql.PreparedStatement
  def addBatch(): Unit = ???
  def clearParameters(): Unit = ???
  def getMetaData(): java.sql.ResultSetMetaData = ???
  def getParameterMetaData(): java.sql.ParameterMetaData = ???
  def setArray(x$1: Int,x$2: java.sql.Array): Unit = ???
  def setAsciiStream(x$1: Int,x$2: java.io.InputStream): Unit = ???
  def setAsciiStream(x$1: Int,x$2: java.io.InputStream,x$3: Long): Unit = ???
  def setAsciiStream(x$1: Int,x$2: java.io.InputStream,x$3: Int): Unit = ???
  def setBigDecimal(x$1: Int,x$2: java.math.BigDecimal): Unit = ???
  def setBinaryStream(x$1: Int,x$2: java.io.InputStream): Unit = ???
  def setBinaryStream(x$1: Int,x$2: java.io.InputStream,x$3: Long): Unit = ???
  def setBinaryStream(x$1: Int,x$2: java.io.InputStream,x$3: Int): Unit = ???
  def setBlob(x$1: Int,x$2: java.io.InputStream): Unit = ???
  def setBlob(x$1: Int,x$2: java.io.InputStream,x$3: Long): Unit = ???
  def setBlob(x$1: Int,x$2: java.sql.Blob): Unit = ???
  def setBoolean(x$1: Int,x$2: Boolean): Unit = ???
  def setByte(x$1: Int,x$2: Byte): Unit = ???
  def setBytes(x$1: Int,x$2: Array[Byte]): Unit = ???
  def setCharacterStream(x$1: Int,x$2: java.io.Reader): Unit = ???
  def setCharacterStream(x$1: Int,x$2: java.io.Reader,x$3: Long): Unit = ???
  def setCharacterStream(x$1: Int,x$2: java.io.Reader,x$3: Int): Unit = ???
  def setClob(x$1: Int,x$2: java.io.Reader): Unit = ???
  def setClob(x$1: Int,x$2: java.io.Reader,x$3: Long): Unit = ???
  def setClob(x$1: Int,x$2: java.sql.Clob): Unit = ???
  def setDate(x$1: Int,x$2: java.sql.Date,x$3: java.util.Calendar): Unit = ???
  def setDate(x$1: Int,x$2: java.sql.Date): Unit = ???
  def setDouble(x$1: Int,x$2: Double): Unit = ???
  def setFloat(x$1: Int,x$2: Float): Unit = ???
  def setNCharacterStream(x$1: Int,x$2: java.io.Reader): Unit = ???
  def setNCharacterStream(x$1: Int,x$2: java.io.Reader,x$3: Long): Unit = ???
  def setNClob(x$1: Int,x$2: java.io.Reader): Unit = ???
  def setNClob(x$1: Int,x$2: java.io.Reader,x$3: Long): Unit = ???
  def setNClob(x$1: Int,x$2: java.sql.NClob): Unit = ???
  def setNString(x$1: Int,x$2: String): Unit = ???
  def setNull(x$1: Int,x$2: Int,x$3: String): Unit = ???
  def setObject(x$1: Int,x$2: Any,x$3: Int,x$4: Int): Unit = ???
  def setObject(x$1: Int,x$2: Any): Unit = ???
  def setObject(x$1: Int,x$2: Any,x$3: Int): Unit = ???
  def setRef(x$1: Int,x$2: java.sql.Ref): Unit = ???
  def setRowId(x$1: Int,x$2: java.sql.RowId): Unit = ???
  def setSQLXML(x$1: Int,x$2: java.sql.SQLXML): Unit = ???
  def setShort(x$1: Int,x$2: Short): Unit = ???
  def setTime(x$1: Int,x$2: java.sql.Time,x$3: java.util.Calendar): Unit = ???
  def setTime(x$1: Int,x$2: java.sql.Time): Unit = ???
  def setTimestamp(x$1: Int,x$2: java.sql.Timestamp,x$3: java.util.Calendar): Unit = ???
  def setTimestamp(x$1: Int,x$2: java.sql.Timestamp): Unit = ???
  def setURL(x$1: Int,x$2: java.net.URL): Unit = ???
  def setUnicodeStream(x$1: Int,x$2: java.io.InputStream,x$3: Int): Unit = ???

  // Members declared in java.sql.Statement
  def addBatch(x$1: String): Unit = ???
  def cancel(): Unit = ???
  def clearBatch(): Unit = ???
  def clearWarnings(): Unit = ???
  def close(): Unit = ???
  def closeOnCompletion(): Unit = ???
  def execute(x$1: String,x$2: Array[String]): Boolean = ???
  def execute(x$1: String,x$2: Array[Int]): Boolean = ???
  def execute(x$1: String,x$2: Int): Boolean = ???
  def execute(x$1: String): Boolean = ???
  def executeBatch(): Array[Int] = ???
  def executeQuery(x$1: String): java.sql.ResultSet = ???
  def executeUpdate(x$1: String,x$2: Array[String]): Int = ???
  def executeUpdate(x$1: String,x$2: Array[Int]): Int = ???
  def executeUpdate(x$1: String,x$2: Int): Int = ???
  def executeUpdate(x$1: String): Int = ???
  def getConnection(): java.sql.Connection = ???
  def getFetchDirection(): Int = ???
  def getFetchSize(): Int = ???
  def getGeneratedKeys(): java.sql.ResultSet = ???
  def getMaxFieldSize(): Int = ???
  def getMaxRows(): Int = ???
  def getMoreResults(x$1: Int): Boolean = ???
  def getMoreResults(): Boolean = ???
  def getQueryTimeout(): Int = ???
  def getResultSet(): java.sql.ResultSet = ???
  def getResultSetConcurrency(): Int = ???
  def getResultSetHoldability(): Int = ???
  def getResultSetType(): Int = ???
  def getUpdateCount(): Int = ???
  def getWarnings(): java.sql.SQLWarning = ???
  def isCloseOnCompletion(): Boolean = ???
  def isClosed(): Boolean = ???
  def isPoolable(): Boolean = ???
  def setCursorName(x$1: String): Unit = ???
  def setEscapeProcessing(x$1: Boolean): Unit = ???
  def setFetchDirection(x$1: Int): Unit = ???
  def setFetchSize(x$1: Int): Unit = ???
  def setMaxFieldSize(x$1: Int): Unit = ???
  def setMaxRows(x$1: Int): Unit = ???
  def setPoolable(x$1: Boolean): Unit = ???
  def setQueryTimeout(x$1: Int): Unit = ???

  // Members declared in java.sql.Wrapper
  def isWrapperFor(x$1: Class[_]): Boolean = ???
  def unwrap[T](x$1: Class[T]): T = ???


  // Errm, this is what we added...

  //was for dates
  //  private SqlFormatter formatter




  // [... filter out '?' in statement that are not bind variables]
  // Adding spaces ensures splittting not on ends.
  val filteredSql = ' ' + sql + ' '

  //  Find out how many variables are present in statement
  val bindVarCount = filteredSql.count('?' ==)


  /** Seq of bind variables
    */
  
  private val bindVars = new Array[Any](bindVarCount)

  private var errorMessage = ""
  private var varBinds = 0

  private def saveBind(idx: Int, x: Any) {
    // -1 because the indexing works from 1 (this doesn't)
    varBinds += 1
    if(varBinds > bindVarCount) errorMessage = s"Too many bindings $varBinds for found variable count $bindVarCount"
    else {
      bindVars(idx - 1) = x
    }
  }

  def setInt(idx: Int, x: Int) {
    saveBind(idx, x)
    stmt.setInt(idx, x)
  }

  def setLong(idx: Int, x: Long) {
    saveBind(idx, x)
    stmt.setLong(idx, x)
  }

  def setString(idx: Int, x: String) {
    saveBind(idx, x)
    stmt.setString(idx, x)
  }

  def setNull(idx: Int, sqlType: Int) {
    saveBind(idx, "<null>")
    stmt.setNull(idx, sqlType)
  }

  override def toString
      : String =
  {



    if(varBinds < bindVarCount) errorMessage = s"Too few bindings $varBinds for found variable count $bindVarCount"

    if (!errorMessage.isEmpty()) errorMessage
    else {

      //println(s"number of bind vars: $bindVarCount")

      val b = new StringBuilder()
      var i = 0

      var fragments = filteredSql.split('?')

      bindVars.foreach{ v =>
        b ++= fragments(i)
        b ++= v.toString
        i += 1
      }

      // last fragment
      b ++= fragments(i)

      b.result
    }
  }

  def executeQuery(): ResultSet = stmt.executeQuery()

  def executeUpdate(): Int = stmt.executeUpdate()

  def execute(): Boolean = stmt.execute()

}//PreparedStatementPrintable



object PreparedStatementPrintable {

  def apply(
    conn: Connection,
    sql: String
  )
      :  PreparedStatementPrintable =
  {
    val stmt: PreparedStatement  = conn.prepareStatement(sql)
    new PreparedStatementPrintable(stmt, sql)
  }

  def apply(
    stmt: PreparedStatement,
    sql: String
  )
      :  PreparedStatementPrintable =
  {
    new PreparedStatementPrintable(stmt, sql)
  }

}//PreparedStatementPrintable
