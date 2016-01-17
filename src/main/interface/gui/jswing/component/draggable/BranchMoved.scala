package freight
package interface
package gui
package jswing
package component
package draggable

import swing._
import event._

object BranchMoved {
  def unapply(a: BranchMoved): Option[(Long, Long, Seq[Long])] = Some((a.oldParent, a.newParent, a.childList))
}

class BranchMoved(
val oldParent: Long, 
val newParent: Long,
val childList: Seq[Long]
) extends Event
