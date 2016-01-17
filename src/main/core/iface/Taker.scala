package freight
package core
package iface

import java.util.Locale 



/** Templates methods for receiving Freight types.
 *  
 * Implementations of this class are given descriptive information and
 * typed data from freight fields.
 *
 */
trait Taker
extends Any
{

  def boolean(fieldName: String, v: Boolean)
  def short(fieldName: String, v: Short)
  def int(fieldName: String, v: Int)
  def long(fieldName: String, v: Long)
  def float(fieldName: String, v: Float)
  def double(fieldName: String, v: Double)
  def string(fieldName: String, v: String)
  def text(fieldName: String, v: String)
  def binary(fieldName: String, v: Array[Byte])
  def time(fieldName: String, v: Long)
  def timestamp(fieldName: String, v: Long)
  def locale(fieldName: String, v: Locale)

  def booleanStr(fieldName: String, v: String)
  def shortStr(fieldName: String, v: String)
  def intStr(fieldName: String, v: String)
  def longStr(fieldName: String, v: String)
  def floatStr(fieldName: String, v: String)
  def doubleStr(fieldName: String, v: String)
  def stringStr(fieldName: String, v: String)
  def textStr(fieldName: String, v: String)
  def binaryStr(fieldName: String, v: String)
  def timeStr(fieldName: String, v: String)
  def timestampStr(fieldName: String, v: String)
  def localeStr(fieldName: String, v: String)
}
