package freight
package canspeak
package builder

import core.objects.mutable.GenStringBuilder


/** Builds XML output.
  *
  * @param incrementalId id to be used if this is an insert.
  * @param forceInsert ask the builder to generate xml with
  *  an id from `incrementalId`.
  */
class XML()
    extends GenStringBuilder[String]
{
  private val b = new StringBuilder


  private  var usedId = NullIDStr

  var isUpdate = true
  def writtenId = usedId

  protected def tag(
    fieldName: String,
    typ: String,
    v: String
  )
  {
    b += '<'
    b append fieldName
    b ++= " type="
    b ++= typ
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def booleanStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=boolean"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def shortStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=short"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def intStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=int"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def longStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=long"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
  }

  def floatStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=float"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def doubleStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=double"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def stringStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=string"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def binaryStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=binary"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def textStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=text"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def timeStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=time"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def timestampStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=timestamp"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def localeStr(fieldName: String, v: String) {
    b += '<'
    b append fieldName
    b ++= " type=locale"
    b += '>'
    b ++= v
    b ++= "</"
    b append fieldName
    b += '>'
    
  }

  def clear() = b.clear()

  def result(): String = b.result()
}



