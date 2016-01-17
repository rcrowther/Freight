package freight
package core
package iface

import java.util.Locale 



/** Templates methods for receiving Freight types.
 *  
  * This trait implements the multi methods for string-based givers.
 */
//was GenGatherer
trait TakerForString
extends Taker
{

  def boolean(fieldName: String, v: Boolean) = booleanStr(fieldName, if (v) "t" else "f")
  def short(fieldName: String, v: Short) = shortStr(fieldName, v.toString)
  def int(fieldName: String, v: Int) = intStr(fieldName, v.toString)
  def long(fieldName: String, v: Long) = longStr(fieldName, v.toString)
  def float(fieldName: String, v: Float) = floatStr(fieldName, v.toString)
  def double(fieldName: String, v: Double) = doubleStr(fieldName, v.toString)
  def string(fieldName: String, v: String) = stringStr(fieldName, v)
  def text(fieldName: String, v: String) = textStr(fieldName, v)
  def binary(fieldName: String, v: Array[Byte]) = binaryStr(fieldName, "")
  def time(fieldName: String, v: Long) = timeStr(fieldName, v.toString)
  def timestamp(fieldName: String, v: Long) = timestampStr(fieldName, v.toString)
  def locale(fieldName: String, v: Locale) = {
    val b = new StringBuilder()
    b ++= v.getLanguage()
    b += '_'
    b ++= v.getCountry()
    b += '_'
    b ++= v.getVariant()
    localeStr(fieldName, b.result())
  }

}
