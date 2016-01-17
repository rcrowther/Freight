package freight
package interface
package gui
package jswing
package component

import swing._

import jswing.event.{
SelectedIdChanged,
SelectionChanged
}

/** A list base where the rows carry an id.
  *
  * With a guarenteed id, several selection methods can be added.
  *
  * Emits `SelectionChanged`, `SelectedIdChanged`.
  *
  * See [[ListRowWithId]].
  */
class LineListWithId[ROW <: ListRowWithId[ROW]]
    extends LineList[ROW]
{

  /** Finds id of the selected row.
* 
* @return optional id of the item, None if no item is selected.
    */
  def selectedId: Option[Long] =
    if(selectedRow == None) None
    else Some(selectedRow.get.id)

  /** Set the selection by index.
    *
    * Useful anytime the data in the list is changed, especially if a
    * selected row is deleted, or a new row appended.
    *
    * Updates `selectedRow`.
    */
  def selectionId_=(id: Long) {
    if(selectedId != None) {
      val row = find(selectedId.get)
      row.unSelect
    }
    val newRow = find(id)
    selectedRow = Some(newRow)
    newRow.select
  }

  /** Find a row from an id.
    */
  def find(id: Long) : ROW = {
    val rowO = contents.find((r) => {r.asInstanceOf[ROW].id == id})
    if (rowO != None) rowO.get.asInstanceOf[ROW]
    else error("find could not locate row id: $id")
  }

  /** Finds the row index from an id.
    */
  def indexOf(id: Long) : Option[Int] = {
    var i = 0
    var cont = true
    var oid = 0L
    while (i < contents.size && cont) {
      oid = contents(i).asInstanceOf[ROW].id
      cont = (oid != id)
      i += 1
    }
    if (!cont) Some(i - 1)
    else None
  }

  /** Finds the nearest row index from an id.
*
* ** Untested!**
*
* @return index of the first row with an id >= to the given id.
    */
  def nearestIndexOf(id: Long) : Int = {
    var i = 0
    var cont = true
    var oid = 0L
    while (i < contents.size && cont) {
      oid = contents(i).asInstanceOf[ROW].id
      cont = (oid < id)
      i += 1
    }
    if (i == 0) 0
    else i - 1
  }

  /** Finds the row index from a java component.
    */
  def indexOf(component: java.awt.Component)
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

  /** Removes a row from this $cpnt by id.
    * 
    */
  def removeById(oid: Long)
  {
    val iO = indexOf(oid)
    if (iO != None) {
      super.remove(iO.get)
    }
  }

  /** Update a row in this $cpnt by id.
    */
  def updateById(oid: Long, row: Component)
  {
    val iO = indexOf(oid)
    if (iO != None) {
      super.update(iO.get, row)
    }
  }

  listenTo(this)
  reactions += {
    // Forward this to the view
    case SelectionChanged(klass) => {
    publish(new SelectedIdChanged(selectedId))
    }
  }
}//LineListWithId
