package freight
package interface
package gui
package jswing
package component

import swing._



/** A list shown as labels stacked in a vertical box.
  *
  * Sets the list to be a simple list with titles. The form of the
  * listings is known, so an `append` can be added.
  *
  * This trait defines a complete component, see [[TitleLineList]].
  *
  * Emits `SelectionChanged`, `SelectedIdChanged`.
  *
  * @define cpnt title list
  */
class GenTitleLineList
    extends LineListWithId[TitleListRow]
{
  /** Inserts a new row into this $cpnt.
    *
    * Emits no events. Can be used for GUI construction etc.
* ** Untested!**
    */
  def insertByIdNoNotify(id: Long, title: String)
      : TitleListRow =
  {
    // log("TableBase append row id: $id title: $title")
    val row = new TitleListRow(
      id,
      title,
      onUserSelect
    )
    contents.insert(nearestIndexOf(id), row)
    row
  }

  /** Inserts a new row into this $cpnt.
*
* Note that this method takes an id, regardless.
* ** Untested!**
    */
  def insertById(id: Long, title: String)
  {
    // log("TableBase append row id: $id title: $title")
   val row = insertByIdNoNotify(id, title)
    select(Some(row))
  }

  /** Appends a new row to this $cpnt.
    *
    * Emits no events. Can be used for GUI construction etc.
    */
  def appendNoNotify(id: Long, title: String)
      : TitleListRow =
  {
    // log("TableBase append row id: $id title: $title")
    val row = new TitleListRow(
      id,
      title,
      onUserSelect
    )
    contents += row
    row
  }

  /** Appends a new row to this $cpnt.
*
* Note that this method takes an id, regardless.
    */
  def append(id: Long, title: String)
  {
    // log("TableBase append row id: $id title: $title")
    val row = appendNoNotify(id, title)
    select(Some(row))
  }


  /** Update a row in this $cpnt by id.
    */
  def updateById(oid: Long, title: String)
  {
    val row = new TitleListRow(
      oid,
      title,
      onUserSelect
    )
    updateById(oid, row)
  }



}//GenTitleLineList

