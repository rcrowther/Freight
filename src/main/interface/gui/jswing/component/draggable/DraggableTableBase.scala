package freight
package interface
package gui
package jswing
package component
package draggable


import swing._
import event._

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
  * @param allowHorizontalDrag if false, returns a simple vertical
  * table, if true, renders and handles a tree.
  */
abstract class DraggableTableBase
extends LineListWithId[TableRow]
{
// Unless otherwise
  xLayoutAlignment = 0.0

  val dragIcon: javax.swing.Icon 


  private val rowBuff = ArrayBuffer.empty[Component]


  /** Appends a new row to this table.
    * 
    */
  protected def append(id: Long, title: String, indent: Int)
  {
   // log("TableBase append row id: $id title: $title")
    val row = new TableRow(
      dragIcon,
      id,
      title,
      indent,
      indexOf,
      emitRowMoved,
      onUserSelect
    )
    contents += row
  }



  protected def moveRow(from: Int, to: Int, x: Int)


  private def emitRowMoved(from: Int, to: Int, x: Int) {
    publish(new RowMoved(from, to, x))
  }




  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------
  

  listenTo(this)
  reactions += {
    // Test
 /*
     case BranchMoved(oldParent, newParent, newSiblings) => {
     log(s"BranchMove : oldParent:$oldParent newParent:$newParent newSiblings:$newSiblings")
     }
 

     case SelectionChanged(id) => {
     log(s"SelectionChanged : id:$id unselect: $currentSelectionId")
     findRow(currentSelectionId).unSelect()
     currentSelectionId = id
     findRow(currentSelectionId).select()
     }
*/

    case RowMoved(from, to, x) => {
      log(s"\nview row moved! from:$from to:$to x:$x")
      moveRow(from, to, x)
    }
  }

}//DraggableTableBase


