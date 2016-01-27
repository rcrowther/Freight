package freight
package interface
package gui
package jswing

import swing._
import swing.event._



/** A preconfigured listview.
  * 
  * Aligns to left, sizes to width, writes data in horizontally,
  * wrapping. Asserts a minimum size.
  */
class ListField[A](
  multipleSelect: Boolean,
  horizontal: Boolean,
  cellWidth: Int,
  items: Seq[A]
)
    extends ScrollPane
{
  def this(
    multipleSelect: Boolean,
    horizontal: Boolean,
    cellWidth: Int
  ) = this(multipleSelect, horizontal, cellWidth, Seq.empty[A])

  def this(
    multipleSelect: Boolean
  ) = this(multipleSelect, false, 0, Seq.empty[A])

  val lv = new ListView[A](items)
  lv.selection.intervalMode =
    if(multipleSelect) ListView.IntervalMode.MultiInterval
    else ListView.IntervalMode.Single
  lv.visibleRowCount = 4
  lv.minimumSize = new Dimension(20, 20)
  lv.xLayoutAlignment = 0.0
  /*
   // NB: Horizontal callapse initial size to zero
   // Not worth fighting with R.C.
   if(horizontal){
   lv.peer.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP)
   lv.fixedCellWidth = cellWidth
   }
   */
  xLayoutAlignment = 0.0

  minimumSize = new Dimension(20, 20)
  def selection = lv.selection
  def clearSelection() = lv.peer.getSelectionModel().clearSelection()
  def selectionIdxs: Array[Int] = lv.peer.getSelectedIndices()
  def listData_=(items: Seq[A]) = {lv.listData = items}
  def listData: Seq[A] = lv.listData

  contents = lv

}
