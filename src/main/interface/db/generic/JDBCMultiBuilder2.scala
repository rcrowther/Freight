package freight
package interface
package db
package generic

import java.util.Locale
import java.sql.PreparedStatement
import javax.sql.rowset.serial.SerialBlob



/** Base methods for a JDBC SQL builder for writing.
  * 
  * The prepared statement is assumed to contain wildcards for all
  * fields.
  *
  * @param ps the `PreparedStatement` used for writing.
  */
// TODO: Blob not sorted at all
// TODO: implement clear()
class JDBCMultiBuilder2(
  ps: PreparedStatement
)
    extends GenMultiBuilder[PreparedStatement]
{

  /** The `PreparedStatement` used for writing.
    */
  protected var s : PreparedStatement = ps

  private var firstLong = true

  /** A counter for fields in an object.
    */
  protected var i = 0

  private var id = 0L
  def givenId: Long = id

  //TODO: JSON = "true" "false"
  def boolean(fieldName: String, v: Boolean) {
    i += 1
    s.setBoolean(i, v)
  }

  def short(fieldName: String, v: Short) {
    i += 1
    s.setShort(i, v)
  }

  def int(fieldName: String, v: Int) {
    i += 1
    s.setInt(i, v)
  }

  // TODO: What about ids?
  def long(fieldName: String, v: Long) {
    if(firstLong) {
      id = v
      firstLong = false
    }
    i += 1
    s.setLong(i, v)
  }

  def float(fieldName: String, v: Float) {
    i += 1
    s.setFloat(i, v)
  }

  def double(fieldName: String, v: Double) {
    i += 1
    s.setDouble(i, v)
  }

  def string(fieldName: String, v: String) {
    i += 1
    s.setString(i, v)
  }

  def binary(fieldName: String, v: Array[Byte]) {
    i += 1
    s.setBlob(i, new SerialBlob(v))
  }

  def text(fieldName: String, v: String) {
    i += 1
    log(s"$fieldName i= $i")
    s.setString(i, v)
  }

  def time(fieldName: String, v: Long) {
    i += 1
    s.setLong(i, v)
  }

  def timestamp(fieldName: String, v: Long){
    i += 1
    s.setLong(i, v)
  }

  def locale(fieldName: String, v: Locale) {
    i += 1
    s.setString(i, v.toString)
  }

  def clear() = error("dont call")

  def result(): PreparedStatement = {
    s
  }

}//JDBCMultiBuilder


