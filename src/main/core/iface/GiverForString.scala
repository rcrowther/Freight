package freight
package core
package iface

import java.util.Locale 

/** Templates methods for returning Freight types.
 *
  * This trait implements the multi methods for string-based givers.
  *
  */
trait GiverForString
extends Giver
 {
  def boolean() : Boolean = (booleanStr() == "t")
  def short() : Short = shortStr().toShort
  def int() : Int =  intStr().toInt
  def long() : Long = longStr().toLong
  def float() : Float = floatStr().toFloat
  def double() : Double = doubleStr().toDouble
  def string() : String = stringStr()
  def text() : String = textStr()
  def binary() : Array[Byte] = Array.empty[Byte]
  def time() : Long = timeStr().toLong
  def timestamp() : Long = timestampStr().toLong
  def locale() : Locale = {
    val s = localeStr().split('_')
   new Locale(s(0), s(1), s(2))
  }
}
