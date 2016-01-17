package freight
package canspeak
package builder

import core.objects.mutable.GenStringBuilder


/** Builds JSON output.
  *
  * @param incrementalId id to be used if this is an insert.
  * @param forceInsert ask the builder to generate JSON with
  *  an id from `incrementalId`.
  */
//TODO: Only for one offs. JSON needs wrapping,
// not figured separators (yet)
class JSON()
    extends GenStringBuilder[String]
{
  private val b = new StringBuilder
  private var first = true


  b += '{'

  protected def plain(fieldName: String, v: String) {
    if (first) first = false
    else b += ','
    b += '"'
    b append fieldName
    b += '"'
    b += ':'
    b ++= v
  }

  protected def quoted(fieldName: String, v: String)  {
    if (first) first = false
    else b += ','
    b += '"'
    b append fieldName
    b += '"'
    b += ':'
    b += '"'
    b ++= v
    b += '"'
  }

  //TODO: JSON = "true" "false"
  def booleanStr(fieldName: String, v: String) {
    plain(fieldName, v)
    
  }

  def shortStr(fieldName: String, v: String) {
    plain(fieldName, v)
    
  }

  def intStr(fieldName: String, v: String) {
    plain(fieldName, v)
    
  }

  def longStr(fieldName: String, v: String) {
    plain(fieldName, v)
  }

  def floatStr(fieldName: String, v: String) {
    plain(fieldName, v)
    
  }

  def doubleStr(fieldName: String, v: String) {
    plain(fieldName, v)
    
  }

  def stringStr(fieldName: String, v: String) {
    quoted(fieldName, v)
    
  }

  def binaryStr(fieldName: String, v: String) {
    quoted(fieldName, v)
    
  }

  def textStr(fieldName: String, v: String) {
    quoted(fieldName, v)
    
  }

  def timeStr(fieldName: String, v: String) {
    plain(fieldName, v)
    
  }

  def timestampStr(fieldName: String, v: String) {
    plain(fieldName, v)
    
  }

  def localeStr(fieldName: String, v: String) {
    plain(fieldName, v)
    
  }

  def clear() = b.clear()

  def result(): String = {
    b += '}'
    b.result()
  }
}


