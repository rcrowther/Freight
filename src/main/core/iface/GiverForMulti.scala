package freight
package core
package iface



/** Templates methods for returning Freight types.
 *
  * This trait implements the string giver methods for a multi-based givers.
  *
  */
trait GiverForMulti
extends Giver
 {

  def booleanStr() : String = if (boolean()) "t" else "f"
  def shortStr() : String = short().toString
  def intStr() : String =  int().toString
  def longStr() : String = long().toString
  def floatStr() : String = float().toString
  def doubleStr() : String = double().toString
  def stringStr() : String = string()
  def textStr() : String = text()
  def binaryStr() : String = ""
  def timeStr() : String = time().toString
  def timestampStr() : String = timestamp().toString
  def localeStr() : String = {
    val b = new StringBuilder()
   val l = locale()
    b ++= l.getLanguage()
    b += '_'
    b ++= l.getCountry()
    b += '_'
    b ++= l.getVariant()
    b.result()
  }
}
