package freight
package interface
package gui
package jswing
package event


import swing._
import event._



/** Simple event.
*/
object RefMapModified {
  def unapply(a: RefMapModified) : Option[Int] = Some(a.code)
}

class RefMapModified(val code: Int) extends Event
