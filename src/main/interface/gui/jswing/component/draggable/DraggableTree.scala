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
  */
//TODO: icon source in ssc
// TODO: Needs rebuilding
class DraggableTree()
    extends DraggableTableBase
{

  val dragIcon: javax.swing.Icon =
     Swing.Icon("/home/rob/Code/scala/freight/src/main/interface/gui/jswing/component/draggableTable/draggable.png")



  /** Gets data from the table.
    * 
    * @return a seq of tuple[id, indent].
    */
  def data
      : Seq[(Long, Int)] =
  {
    val b = Seq.newBuilder[(Long, Int)]
    contents.foreach{ c =>
      b += c.asInstanceOf[TableRow].data()
    }
    b.result()
  }

  /** Sets new contents on the table.
    * 
    * @parem d a seq of tuple[id, title, indent]
    */
  def data_=(d: Seq[(Long, String, Int)]) {
  elWarningIf(d.isEmpty, "Empty model provided  --- will not render", "DraggableTree")

    contents.clear()
    d.foreach { case(id, title, indent) =>
      append(id, title, indent)
    }
  }




  /** Returns the sibling and parent ids of this row.
    *
    * @return the parent id (0) siblings ids (containing self)
    *  or, if the root, none.
    */
  private def siblingsAndParent(rowIdx: Int)
      : (Long, Seq[Long]) =
  {
    //elErrorIf(rowIdx == 0, "Searching for siblings and parent on idx = 0")

    val baseRow = contents(rowIdx).asInstanceOf[TableRow]
    val baseIndent = baseRow.indent
    var b = new collection.mutable.ArrayBuffer[Long]()

    //search up
    var i = rowIdx - 1
    var cont = true
    var row: TableRow = null
    var indent: Int = 0

    // If on row 0, don't search up, and parent is zero.
    val parent =
      if (rowIdx == 0) 0
      else {

        while(i >= 0 && cont) {
          row = contents(i).asInstanceOf[TableRow]
          indent = row.indent
          if (indent == baseIndent) b += row.id
          cont = row.indent >= baseIndent
          i -= 1
        }

        // Get parent
        if (cont) 0
        else row.id
      }

    // Reverse the child seq
    b = b.reverse

    // Add the current element
    b += baseRow.id

    //search down
    // defend against last row
    val limit = contents.size
    if(rowIdx != limit - 1) {
      i = rowIdx + 1
      cont = true
      row = null
      indent = 0

      while(i < limit && cont) {
        row = contents(i).asInstanceOf[TableRow]
        indent = row.indent
        if (indent == baseIndent) b += row.id
        cont = row.indent >= baseIndent
        i += 1
      }
    }
    (parent -> b.result)

  }


  /** Returns the parent id of this row.
    *
    * @return the parent id or, if the root, none.
    */
  private def parentId(rowIdx: Int) : Long = {
    val baseIndent = contents(rowIdx).asInstanceOf[TableRow].indent

    // if indent is 0, the parent is 0
    if (baseIndent == 0) 0
    else {
      var i = rowIdx - 1
      var cont = true
      var row: TableRow = null
      while(i >= 0 && cont) {
        row = contents(i).asInstanceOf[TableRow]
        cont = (row.indent >= baseIndent)
        i -= 1
      }
      row.id
    }
  }

  /** Tests if this row is the first child of a parent.
    *
    * Assumes the first row is the first child of some imaginary root.
    */
  private def isFirstChild(rowIdx: Int) : Boolean = {
log(s"first Child rowIdx:$rowIdx")
    val baseIndent = contents(rowIdx).asInstanceOf[TableRow].indent

    // if indent is 0, the parent is 0
    if (rowIdx == 0) true
    else {
      var i = rowIdx - 1
      var cont = true
      var row: TableRow = null
      while(i >= 0 && cont) {
        row = contents(i).asInstanceOf[TableRow]
        cont = row.indent > baseIndent
        i -= 1
      }
      (baseIndent != row.indent)
    }
  }

  /** Finds the index where descendants stop.
    */
  private def descendantsTo(
    rowIdx: Int,
    currentIndent: Int
  )
      : Int =
  {
    val limit = contents.size

    // Trap the last row and return
    if (rowIdx >= limit - 1) {
      rowIdx
    }
    else {
      // To be a child, indent must be > rowIdent
      val baseIdent = currentIndent

      var i = rowIdx + 1

      var cont = true
      while(i < limit && cont) {
        var newRowIdent = contents(i).asInstanceOf[TableRow].indent
        if (newRowIdent > baseIdent) {
          // It's a child
          i += 1
        }
        else cont = false
      }
      i - 1
    }
  }


  /** Indents a row and it's descendants.
    *
    */
  private def setIdents(
    fromIdx: Int,
    toIdx: Int,
    offset: Int
  )
  {
    //log(s"setIdents fromIdx:$fromIdx toIdx:$toIdx")
    var row : TableRow = null
    var rowIdent = 0
    var i = fromIdx

    while(i <= toIdx) {
      row = contents(i).asInstanceOf[TableRow]
      rowIdent = row.indent
      row.setIndent(rowIdent + offset)
      i += 1
    }
  }


  /** Moves a range of rows.
    *
    * This is a raw method, it simply shunts the rows, without regard
    * to indenting or validity of the move.
    *
    *
    * @param fromIdx rows to be moved start here.
    * @param descIdx rows to be moved stop here.
    * @param toIdx index to be moved to.
    *
    * @return the insert point (where the tree arrived. This will
    * differ from toIdx if the tree is moved down).
    */
  private def moveDescendants(
    fromIdx: Int,
    descIdx: Int,
    toIdx: Int
  )
      : Int =
  {
    //log(s"fromIdx:${fromIdx} toIdx:$toIdx descIdx: $descIdx")
    val b = Seq.newBuilder[TableRow]

    for(i <- fromIdx to descIdx) {
      b += contents.remove(fromIdx).asInstanceOf[TableRow]
    }

    val insertComponents = b.result()

    // Now find the insert point.
    // This will differ from from if the tree is  moved down,
    // so the removals have shifteed the requested index
    val insertPoint =
      if(toIdx < fromIdx) toIdx
      else {
        toIdx - insertComponents.size + 1
      }
    //log(s"insertPoint:${insertPoint}")

    // Now paste elements where they are going
    // Insert works on the last element, cheers!
    b.result.reverse.foreach { row =>
      contents.insert(insertPoint, row)
    }

    insertPoint
  }

  /** Move a row with horizontal placements allowed.
    *
    * @return true if something was moved.
    */
  private def moveRowWithHorizontal(
    from: Int,
    toIdx: Int,
    x: Int
  )
      : Option[Int] =
  {


    //log(s"view moveRowWithHorizontal from:$from to:$toIdx")

    // Get the current indent.
    // need to stash this for tree indent adjustment
    val srcRowIndent = contents(from).asInstanceOf[TableRow].indent
    val requestedIndent: Int = (if(x == 0) 0 else x/20).toInt
    val descTo = descendantsTo(from, srcRowIndent)

    // if to == from, do nothing (dragged to the same place).
    if(from == toIdx) {

      // Moving indents only.
      // Only moves from sibling to child allowed. Further outward
      // moves cause orphaning of children. Inward moves break
      // subsequent siblings from the family.
      //
      // First children disallowed as that breaks the tree from
      // family.
log(s"isFirstChild:${isFirstChild(toIdx)}")
      if (
        !isFirstChild(toIdx)
          && requestedIndent > srcRowIndent
      )
      {
        // Move outwards
        setIdents(
          from,
          descTo,
          +1
        )
        Some(from)
      }
      else None
      

    }
    else {
      // Moving position and maybe idents


      // Avoid ugly case
      // - trying to move a tree *into*
      // the tree being moved
      if (
        toIdx > descTo
          || toIdx < from
      )
      {

        val insertPoint = moveDescendants(
          from,
          descTo,
          toIdx
        )
        //log(s"insertPoint:${isFirstChild(toIdx)}")
        // For tree moves, there are several strategies -
        // ours is to anywhere reindent.
        //
        // Wether moving up or down, the inserted elements must go
        // into the tree aligned with the following element. If the
        // element has a child, they become a child themselves (which
        // avoids divorcing the original parent). If only a sibling
        // follows, then the insert can be a following sibling.
        //
        // NB: srcRowIndent, descTo still valid
        val insertEnd = insertPoint + (descTo - from)

        // The exception is a downward drop on the last row.
        //
        // This has no following element, nothing to divorce, so can
        // be aligned with the previous element.
        val aligningIndent =
          if (insertEnd + 1 < contents.size) {
            contents(insertEnd + 1).asInstanceOf[TableRow].indent
          }
          else {
            contents(insertPoint - 1).asInstanceOf[TableRow].indent
          }
        //log(s"aligningIndent:$aligningIndent")
        // Only reindent if the indents are changing
        if(aligningIndent != srcRowIndent) {

          setIdents(
            insertPoint,
            insertEnd,
            aligningIndent - srcRowIndent
          )
        }

        revalidate()
        Some(insertPoint)
      }
      else None

    }
  }

  protected def moveRow(from: Int, to: Int, x: Int) {
    elErrorIf(
      (from > contents.size || from < 0),
      s"Row move out of range: $from from to:$to"
    )


      val oldParent = parentId(from)

      val insertPoint = moveRowWithHorizontal(from, to, x)
      if (insertPoint != None) {
        // Move made, so publish
        val (newParent, newSiblings) = siblingsAndParent(insertPoint.get)
        publish(new BranchMoved(oldParent, newParent, newSiblings))
      }



  }



  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------
  



}//DraggableTree



object DraggableTree {

  def test()
      : DraggableTree =
  {
    
    val t = new DraggableTree()
    t.append(4, "Index", 0)
    t.append(7, "About", 3)
    t.append(12, "Foreword", 1)
    t.append(13, "Guide", 2)
    t.append(22, "Data", 3)
    t.append(23, "Guidelines", 4)
    t.append(24, "Terms", 1)
    t.append(25, "Shop", 1)
    t
  }


  def apply()
      : DraggableTree =
  {
    new DraggableTree()
  }
  

}//DraggableTree

