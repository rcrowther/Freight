package freight
package interface
package gui
package jswing
package component

import swing._

import jswing.event.SelectionChanged


/** A table base with selection mechanism.
  *
  * The component is row based. The code handles selection tracking
  * and highlighting. The component emits `SelectionChanged` on
  * positive highlights *only* (not deselections).
  *
  * No `append` is provided as the signature will be specific.
  *
  * Emits `SelectionChanged`.
  *
  * See [[ListRow]].
  */
class LineList[ROW <: ListRow[ROW]]
    extends BoxPanel(Orientation.Vertical)
{

  /** Row of the current selection.
    *
    * @return the row if selected, else None.
    */
  protected var selectedRow: Option[ROW] = None


  //def selection: Option[ROW] = selectedRow

  /** Sets the selection.
    *
    * Deselects the current selection. Publishes `SelectionChanged`.
    *
    * @param row optional row component. Supply None to deselect.
    */
  def select(row: Option[ROW]) {
    // Update graphics and tracking
    if (selectedRow != None) {
      selectedRow.get.unSelect()
    }

    //selectedRow = row.asInstanceOf[Option[ROW]]
    selectedRow = row

    if (selectedRow != None) {
      selectedRow.get.select()
    }

    publish(new SelectionChanged(row))
  }

  /** Clear the $cpnt contents.
    *
    * Publishes `SelectionChanged` as `None`.
    */
  def clear() {
    contents.clear()
    select(None)
  }

  /** Clear the $cpnt contents.
    *
    * No signal is published. After calling this method, the field
    * `selectedRow` will be in error. This method is for use as a
    * component in code. For example, as a preliminary to populating a
    * list.
    */
  def clearNoNotify() {
    contents.clear()
  }

  /** Tests whether this $cpnt is empty.
    *
    */
  def isEmpty
      : Boolean =
  {
    contents.isEmpty
  }



  /** Replaces a row in the list.
    *
    * If the row is selected, the method ensures the updated row is
    * selected after the update. No signal is published.
    */
  protected def update(idx: Int, row: Component) {
    val oldRow = contents.remove(idx)
    contents.insert(idx, row)

    if (
      selectedRow != None
        && selectedRow.get == oldRow.asInstanceOf[ROW]
    )
    {
      selectedRow = Some(row.asInstanceOf[ROW])
      selectedRow.get.select()
    }
  }

  /** Removes a row from the table.
    *
    * If the item is selected, selects a new item and publishes. If
    * the new list is empty, nothing is selected and
    * `SelectionChanged` is published as `None`. If the first item is
    * removed, then the new first item is selected and published. Else
    * the item before the item removed is selected and published.
    *
    * If the list is empty, returns silently.
    */
  protected def remove(idx: Int)
  {
    if (!contents.isEmpty) {


      val wasSelected = (
        selectedRow != None
          && selectedRow.get == contents(idx).asInstanceOf[ROW]
      )

      val oldRow = contents.remove(idx, 1)

      if (wasSelected) {
        val newSelection =
          if (!contents.isEmpty) {
            val targetIdx = if (idx < 1) 0 else idx - 1
            Some(contents(targetIdx).asInstanceOf[ROW])
          }
          else None
        select(newSelection)
      }

    }
  }

  /** Finds the row index of a component.
    * 
    * Can be useful, as `selection` returns the row component.
    */
  private def indexOf(component: java.awt.Component)
      : Option[Int] =
  {
    var i = -1
    val ret = contents.find{ row =>
      i += 1
      row.asInstanceOf[ROW] == component
    }
    
    if(ret == None) None
    else Some(i)
  }

  protected def onUserSelect(row: ROW) {
    select(Some(row))
  }



  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------
  



  /*
   // Update graphics and tracking
   if (selectedRow != None) {
   selectedRow.get.unSelect()
   }
   selectedRow = klass.asInstanceOf[Option[ROW]]
   if (selectedRow != None) {
   selectedRow.get.select()
   }
   }
   */



}//LineList
