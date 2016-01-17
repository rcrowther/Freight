package freight
package interface
package gui
package jswing

import swing._
import event._


import component.{
  GenTitleLineList,
  TitleListRow
}
import jswing.event.SelectionChanged




/** Base trait for line list views.
  * 
  * Templates a few methods, and provides helper methods for actions.
  *
  * Emits `SelectionChanged`, `SelectedIdChanged`, `ObjectModified`, `StatusMessage`, and `NeedsResizing`.
  */
// NB: since the selectable component is known, this can add read
class LineListReader(
  val notifyOfStatus: Boolean,
  listReader: (Seq[Int], (Giver) => Unit) => Boolean,
  elementReader: (Seq[Int], Long, (Giver) => Unit) => Boolean,
  titleFieldIdxs: Seq[Int]
)
    extends GenTitleLineList
    with FreightListReader
{


  def insertElement(
    id: Long
  )
      : Boolean =
  {
    elementReader(titleFieldIdxs, id, (g: Giver) => {insertById(g.long, g.string)} )
    true
  }


  def appendElement(
    id: Long
  )
      : Boolean =
  {
    elementReader(titleFieldIdxs, id, (g: Giver) => {append(g.long, g.string)} )
    true
  }

  def updateElement(
    id: Long
  )
      : Boolean =
  {
    elementReader(titleFieldIdxs, id, (g: Giver) => {updateById(g.long, g.string)} )
    true
  }

  def deleteElement(oid: Long)
      : Boolean =
  {
    removeById(oid)
    true
  }

  def readList()
      : Boolean =
  {
    clear()
    listReader(titleFieldIdxs, (g: Giver) => {appendNoNotify(g.long, g.string)} )
    true
  }


}//LineListReader


object LineListReader {

  /** Creates an line list reader from a single field query collection.
    */

  def apply(
    notifyOfStatus: Boolean,
    collection: TakableFieldQueryable,
    titleFieldIdx: Int
  )
      : LineListReader =
  {

    new LineListReader(
      notifyOfStatus: Boolean,
      listReader = collection.foreach,
      elementReader = collection.apply,
      titleFieldIdxs = Seq(0, titleFieldIdx)
    )
  }

}//LineListReader
