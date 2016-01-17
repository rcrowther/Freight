package freight
package interface
package gui
package jswing

import swing.Publisher

import jswing.event.StatusMessage




/** Controls a collection in a collection-friendly way.
  *
  * A controller differs from a collection in a few ways,
  *  - They use a reduced AIRUD interface, strictly giver or binary-based.
  *  - They can have variable parameter sets (so are not templated, for now?)
  *  - `append` returns an support-code-friendly Option[Long], not the
  *    usual `Long` with `NullID`.
  *  - parameters are supplied as [[GenPluggableReadable]] and
  *    [[GenPluggableLoadable]]
  *
  * The last item is important. It means any object equipped with
  * `Pluggable` methods (which are a handful in number), is able to be
  * passed directly into the controller. This makes any class based in
  * a `GenSingleTakeableController` easy to use. Any display, editing,
  * or rendering class (equipped with `Pluggable`) can be supplied
  * directly. Controller code can handle these parameters with a
  * method accepting underlying collection method, no paarameters
  * required. And controller logic is fully separated from display or
  * rendering code.
  *
  * This controller is the generic base for controllers handling a
  * single collection.
  */
trait GenSingleTakeableController[G, T]
{

  def coll: core.collection.Takable[G, T]


  def ^(
    id: Long,
    dataReader: core.collection.GenPluggableReadable[G]
  )
      : Boolean =
  {
    dataReader.writeTo(id, coll.^)
  }

  def +(
    dataReader: core.collection.GenPluggableReadable[G]
  )
      : Option[Long] =
  {
    var newId: Long = dataReader.writeTo(coll.+ _)
    if (newId != NullID) Some(newId)
    else None
  }


  def apply(
    id: Long,
    dataLoader: core.collection.GenPluggableLoadable[G]
  )
      : Boolean =
  {
    dataLoader.loadFrom(id, coll.apply _)
  }


  def ~(
    id: Long,
    dataReader: core.collection.GenPluggableReadable[G]
  )
      : Boolean =
  {
    dataReader.writeTo(id, coll.~)
  }

  def -(id: Long)
      : Boolean =
  {
    coll.-(id)
  }

  def size = coll.size

}//GenSingleTakeableController
