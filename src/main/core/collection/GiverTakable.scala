package freight
package core
package collection




/** Templates a collection for giver and taker data.
  *
  * The trait templates field-based queries on a
  * Giver-defined [[Takable]].
*
* The interface defines a set of IARUD methods, with convenience methods
* added for accepting a [[Taker]]
  *
* This giver and taker only version of a queryable interface can be used
* to implement internal APIs. If field-based querying is possible, prefer [[TakableFieldQueryable]].
*
  * @define coll giver collection
  */
trait GiverTakable
extends Takable[core.iface.Giver, core.iface.Taker]
{
/*

  // Members declared in freight.core.collection.GiverTakable
  protected def giverTakerBridge: freight.FieldBridge = ???
  def meta: freight.CompanionMeta = ???

  // Members declared in freight.core.collection.Transferable
  def -(id: Long): Boolean = ???
  def +(g: freight.core.iface.Giver): Long = ???
  def ~(id: Long,g: freight.core.iface.Giver): Long = ???
  def ^(id: Long,g: freight.core.iface.Giver): Boolean = ???
  def addString(b: StringBuilder): StringBuilder = ???
  def apply(id: Long,f: freight.core.iface.Giver => Unit): Boolean = ???
  def clear(): Boolean = ???
  def foreach(f: freight.core.iface.Giver => Unit): Boolean = ???
  def genreName: String = ???


*/
  /** A meta object.
    *
    * Provides methods for field selection and describing an interface
    * connection. See [[objects.generic.CompanionMeta]]
    */
  // Should be protected, or ok like this?
  def meta: CompanionMeta

  /** Bridges between givers and takers.
    *
    * Should be defined as `meta.stringFieldBridge` or
    * `meta.multiFieldBridge`, depending on the natural handling of
    * the collection.
    */
  def giverTakerBridge: FieldBridge


/*
  def apply(
    id: Long,
    t: Taker
  )
      : Boolean =
  {
    apply(id, (g) => {giverTakerBridge(t, g)} )
  }
*/

/*
  def foreach(t: Taker)
 : Boolean =
{
    foreach((g) => {giverTakerBridge(t, g)})
  }
*/

}//GiverTakable
