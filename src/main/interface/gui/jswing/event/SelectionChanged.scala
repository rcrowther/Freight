package freight
package interface
package gui
package jswing
package event


import swing._
import event._

/** Event for selection changing in components.
*
* Used internally to trigger GUI changes. Allows a null selections.
*/
object SelectionChanged {
  def unapply(a: SelectionChanged): Option[Option[AnyRef]] = Some(a.klass)
}

class SelectionChanged(val klass: Option[AnyRef]) extends Event
