package freight
package canspeak
package builder

import core.objects.mutable.GenStringBuilder


/** Builds Ini output.
  *
  * @param incrementalId id to be used if this is an insert.
  * @param forceInsert ask the builder to generate HTML with
  *  an id from `incrementalId`.
  */
class Ini()
    extends GenStringBuilder[String]
{
  private val b = new StringBuilder


  protected def tag(
    fieldName: String,
    v: String
  )
  {
    b ++= fieldName
    b ++= " = "
    b ++= v
    b ++= "\n"
  }

  private def quotedTag(
    fieldName: String,
    v: String
  )
  {
    b ++= fieldName
    b ++= " = \""
    b ++= v
    b ++= "\"\n"
  }

  def booleanStr(fieldName: String, v: String) {
    tag(fieldName, v)
  }

  def shortStr(fieldName: String, v: String) {
    tag(fieldName, v)
  }

  def intStr(fieldName: String, v: String) {
    tag(fieldName, v)
  }

  def longStr(fieldName: String, v: String) {
    tag(fieldName, v)
  }

  def floatStr(fieldName: String, v: String) {
    tag(fieldName, v)
  }

  def doubleStr(fieldName: String, v: String) {
    tag(fieldName, v)
  }

  def stringStr(fieldName: String, v: String) {
    quotedTag(fieldName, v)
  }

  def binaryStr(fieldName: String, v: String) {
    tag(fieldName, "binary")
  }

  def textStr(fieldName: String, v: String) {
    quotedTag(fieldName, v)
  }

  def timeStr(fieldName: String, v: String) {
    tag(fieldName, v)
  }

  def timestampStr(fieldName: String, v: String) {
    tag(fieldName, v)
  }

  def localeStr(fieldName: String, v: String) {
    quotedTag(fieldName, v)
  }

  def clear() = b.clear()

  def result(): String = b.result()
}



