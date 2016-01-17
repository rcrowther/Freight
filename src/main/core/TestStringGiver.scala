package freight
package core

import java.util.Locale 

/*
e.g.
import freight._
import freight.objects.Paper
val g = new core.TestStringGiver
val p = Paper(g)
= ?



import freight.core.objects.StringObject
val sp = StringObject[Paper](g)

val b = freight.builder.XML()
sp.give(b)
b.result
*/

class TestStringGiver
//extends StringGiver
extends GiverForString
 {
  def booleanStr() : String = "false"
  def shortStr() : String = "6"
  def intStr() : String = "7"
  def longStr() : String = "9"
  def floatStr() : String = "7.8"
  def doubleStr() : String = "9.8"
  def stringStr() : String = "Mirror"
  def binaryStr(): String = ???
  def textStr(): String = "no bananas"
  def timeStr(): String = ???
  def timestampStr(): String = ???
  def localeStr() : String = Locale.ITALY.toString()
}
