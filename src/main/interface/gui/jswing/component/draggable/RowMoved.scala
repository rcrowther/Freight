package freight
package interface
package gui
package jswing
package component
package draggable

import swing._
import event._

object RowMoved {
  def unapply(a: RowMoved): Option[(Int, Int, Int)] = Some((a.from, a.to, a.x))
}

class RowMoved(val from: Int, val to: Int, val x: Int) extends Event
