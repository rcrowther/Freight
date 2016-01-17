package freight
package interface
package db
package generic

import java.util.Locale
import java.sql.PreparedStatement
import javax.sql.rowset.serial.SerialBlob



/** Base methods for a JDBC SQL builder for writing.
  * 
  * The returned statement is picked from between two prepared
  * statements for insert and update.
  *
  * The trait needs a `long` method implementing (`long` may substitute
  * ids, detect auto-incrementing, etc.)
  */
// TODO: Blob not sorted at all
// TODO: implement clear()
trait JDBCMultiBuilder
    extends GenMultiBuilder[PreparedStatement]
{

  /** The `PreparedStatement` used for writing.
    *
    * When implemented, will be an 'INSERT' or 'UPDATE'.
    */
  protected var s : PreparedStatement = null

  /** A counter for fields in an object.
    */
  protected var i = 0


  /** Reports if insert or an update.
    *
    * Unless overridden, decided from the first long retrieved.
    */
  def wasNew : Boolean

  // Next two variable left public
  //as useful for method returns

  /** The id used for the action.
    * 
    * This may differ from the requested id e.g, if the id is
    * overridden, or autonumbering is used.
    */
  def requestedId: Long

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
  def long(fieldName: String, v: Long)

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
    if (!wasNew) {
      // Bind the update WHERE xxx.id = ? expression
      i += 1
      s.setLong(i, requestedId.toLong)
    }
    s
  }

}//JDBCMultiBuilder


