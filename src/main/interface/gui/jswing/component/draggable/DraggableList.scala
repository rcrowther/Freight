package freight
package interface
package gui
package jswing
package component
package draggable


import swing._
import event._
import javax.swing.{Icon, ImageIcon}

import collection.mutable.ArrayBuffer


/** Row-based row-draggable table.
  *
  * Freight specific - no model.
  * 
  * The table handles rows line by line. The table is intended to
  * create, delete, and order lines of data.
  * 
  * The lines of the table can hold an id.
  * 
  * 
  * The table can be switched from a simple vertical line dragging
  * view, to a complex tree-based view.
  * 
  * To connect to the vertical view use the sgnal `RowMoved`, or get
  * resulting data from the method `ordering`. To connect to the tree view
  * use the signal `BranchMove`, or use the method `data`. For selections use `SelectionChanged` or the method ``.
  *
  */
//TODO: Something about the icon source
// TODO: This can manage a row delete (tree can not)
class DraggableList()
    extends DraggableTableBase
{

  val dragIcon: javax.swing.Icon = icon("images/draggable_v.png")


  def append(id: Long, title: String)
  {
super.append(id: Long, title: String, 0)
}

  /** Gets data from the table.
    * 
    * @return a seq of ids.
    */
//TODO: Deprecated - use append?

  def data
      : Seq[Long] =
  {
     contents.map(_.asInstanceOf[TableRow].id).toSeq
  }


  /** Sets new contents on the table.
    * 
    * @parem d a seq of tuple[id, title]
    */
//TODO: Deprecated - use append?
/*
  def data_=(d: Seq[(Long, String)]) {
    elWarningIf(d.isEmpty, "Empty model provided  --- will not render", "DraggableList")

    contents.clear()
    d.foreach { case(id, title) =>
      append(id, title)
    }
  }
*/

  private def moveRowNoHorizontal(from: Int, to: Int) {
    contents.insert(to, contents.remove(from))
    revalidate()
  }

  protected def moveRow(from: Int, to: Int, x: Int) {
    elErrorIf(
      (from > contents.size || from < 0),
      s"Row move out of range: $from from to:$to"
    )

    moveRowNoHorizontal(from, to)
  }

}//DraggableList



object DraggableList {

/*
import interface.gui.jswing.component.draggable.DraggableList
*/
    def test()
      : DraggableList =
  {
    
    val t = new DraggableList()
    t.append(4, "Index")
    t.append(7, "About")
    t.append(12, "Foreword")
    t.append(13, "Guide")
    t.append(22, "Data")
    t.append(23, "Guidelines")
    t.append(24, "Terms")
    t.append(25, "Shop")
    t
  }

  def apply()
      : DraggableList =
  {
    new DraggableList()
  }
  

}//DraggableList

