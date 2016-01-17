package freight
package core

import java.util.Locale 

//import freight.core.iface.MultiGiver

/*
e.g.
import freight._
import freight.objects.Paper
val g = new freight.core.TestGiver
Paper(g)
= ?
*/

class TestMultiGiver
//extends MultiGiver
extends GiverForMulti
 {
  def boolean() : Boolean = false
  def short() : Short = 6
  def int() : Int = 7
  def long() : Long = 9
  def float() : Float = 7.8f
  def double() : Double = 9.8
  def string() : String = "Mirror"
  def binary(): Array[Byte] = ???
  //def id(): Long = 6239L
  def text(): String = "no bananas"
  def time(): Long = ???
  def timestamp(): Long = ???
  def locale() : Locale = Locale.ITALY
}
