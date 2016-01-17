package freight
package util

/*
 import freight._
 import freight.util._
 ObjectTest(200000)
 */
object ObjectTest {

  class Basic(val a: Int, b: Int, c: Int, val d: Int, e: Int)

  class Method(val a: Int, b: Int, c: Int, val d: Int, e: Int) {
    def as(): String = a.toString
    def bs(): String = b.toString
    def cs(): String = c.toString
    def ds(): String = d.toString
    def es(): String = e.toString
  }

  def apply(multiples: Int) {
    StopWatch.test("plain", multiples) {
      var i = 1
      val c =  new Basic(i, i, i, i, i)
      c.d.toString
      i += 1
    }

    StopWatch.test("method", multiples) {
      var i = 1
      val c =  new Method(i, i, i, i, i)
      c.ds
      i += 1
    }
    StopWatch.test("plain", multiples) {
      var i = 1
      val c =  new Basic(i, i, i, i, i)
      c.d.toString
      i += 1
    }

    StopWatch.test("method", multiples) {
      var i = 1
      val c =  new Method(i, i, i, i, i)
      c.ds
      i += 1
    }
  }

}//
