package freight
package interface
package gui
package jswing

import swing.Publisher

import jswing.event.StatusMessage



/** Controls a field-selectable collection in a collection-friendly way.
  *
  * This controller handles a
  * single collection.
  */
class SingleFieldQueryableController(
  val coll: core.collection.TakableFieldQueryable
)
extends GenSingleTakeableController[Giver, Taker]
{

  def apply(
    fieldIdxs: Seq[Int],
    idx: Long,
    dataLoader: core.collection.PluggableGiverLoadable
  )
 : Boolean =
{
    dataLoader.loadFrom(fieldIdxs, idx, coll.apply)
}

  def foreach(
    dataLoader: core.collection.PluggableGiverLoadable 
  )
 : Boolean =
{
    dataLoader.loadFrom(coll.foreach)
}

}//GenSingleFieldSelectableController
