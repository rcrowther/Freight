package freight
package interface
package gui
package jswing
package event


import swing._
import event._

object SelectedIdChanged {
  def unapply(k: SelectedIdChanged): Option[Option[Long]] = Some(k.id)
}

/** Event used in views when a component changes selection.
*
* @param id the id is optional, so deselection can be also expressed (e.g. when a slected row is removed). 
*/
class SelectedIdChanged(val id: Option[Long]) extends Event
