package freight
package core
package iface

import java.util.Locale 



/** Templates methods for receiving Freight types.
 *  
  * This trait implements the string taker methods for a multi-based givers.
 */
//was GenGatherer
trait TakerForMulti
extends Taker
{

  def booleanStr(fieldName: String, v: String) = boolean(fieldName, v == "t")
  def shortStr(fieldName: String, v: String) = short(fieldName, v.toShort)
  def intStr(fieldName: String, v: String) = int(fieldName, v.toInt)
  def longStr(fieldName: String, v: String) = long(fieldName, v.toLong)
  def floatStr(fieldName: String, v: String) = float(fieldName, v.toFloat)
  def doubleStr(fieldName: String, v: String) = double(fieldName, v.toDouble)
  def stringStr(fieldName: String, v: String) = string(fieldName, v)
  def textStr(fieldName: String, v: String) = text(fieldName, v)
  def binaryStr(fieldName: String, v: String) = binary(fieldName, Array.empty[Byte])
  def timeStr(fieldName: String, v: String) = time(fieldName, v.toLong)
  def timestampStr(fieldName: String, v: String) = timestamp(fieldName, v.toLong)
  def localeStr(fieldName: String, v: String) = {
    val s = v.split('_')
   locale(fieldName, new Locale(s(0), s(1), s(2)))
  }
}
