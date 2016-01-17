package freight
package interface
package gui
package jswing
package component

import swing._

import java.awt.event.MouseAdapter
import java.awt.Color



/** A list row with selection mechanism and label.
  *
  * The row handles highlighting of the selection label. The component
  * emits `SelectionChanged` on positive highlights *only* (not
  * deselections).
  *
  * For use with a [[LineList]].
  */
trait ListRow[T]
    extends Publisher
{
  self: T =>

  protected def onUserSelect : (T) => Unit

/** Sets the graphics of this row to selected.
*/
  def select() {
    selectLabel.foreground = Color.BLUE
  }

/** Sets the graphics of this row to unselected.
*/
  def unSelect() {
    selectLabel.foreground = Color.BLACK
  }

  protected val selectLabel = stockLabel("Select")



  selectLabel.peer.addMouseListener(new MouseAdapter() {    
    override def mouseClicked(e: java.awt.event.MouseEvent) {
      onUserSelect(self)
    }
  })

}
