package freight
package core
package collection




/** Templates a collection with object methods.
  *
  *
* This is a full API interface. It should be used for external interfaces.
* If field-based querying is natural, prefer [[FieldQueryable]].
*
  * @define coll collection
  */
trait Generator
extends GiverTakable
{

/*
  // Members declared in freight.core.collection.Generator
  def append[A](o: A)(implicit ms: freight.MultiTakerFieldSelect[A]): Long = ???
  def insert[A](id: Long,o: A)(implicit ms: freight.MultiTakerFieldSelect[A]): Boolean = ???
  def update[A](id: Long,o: A)(implicit ms: freight.MultiTakerFieldSelect[A]): Long = ???

  // Members declared in freight.core.collection.GiverTakable
  def fieldBridge: freight.FieldBridge = ???
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

  /** Inserts an object into the $coll.
    *
    * This method creates new data at a given id. Ids in the data are
    * overriden. If data exists at the id, the result is undetermined
    * (often a fail).
    * 
    * If the id is not relevant use `+`.
    *
    * @param id the id for data.
    * @param o a class instance.
    * @return true if the selection was succcessful, else false.
    */
  def insert[A](
    id: Long,
    o: A
  )(implicit ms: MultiTakerFieldSelect[A]) : Boolean

  /** Appends an object to the $coll.
    *
    * This method creates data at an auto-generated id. Ids in the
    * data are overriden. Whatever the id in the giver supplied, the
    * action is an auto-append.
    *
    * Auto-generated ids are guarenteed to be larger than 0. They is
    * not guarenteed to be sequential, though the usual algorithm will
    * be to increase storage size by one.
    *
    * If writing to a known id use `^`.
    * 
    * @param o a class instance.
    * @return The new id generated. If the write fails,
    * the return is NullID.
    */
  def append[A](
    o: A
  )(implicit ms: MultiTakerFieldSelect[A]) : Long

  /** Returns an object from this $coll.
    *
    * @param id the id to select.
    * @param f a function to apply to the selected element.
    * @return an object if the selection was succcessful, else None.
    */
  @scala.annotation.implicitNotFound(msg = "No implicit available to construct an object of type ${A} from a Giver.")
  def get[A](
    id: Long
  )(implicit fs: (Giver) => A)
 : Option[A] = 
{
    var objO: Option[A] = None
    apply(id, (g: Giver) => { objO = Some(fs(g)) } )
    objO
  }


  /** Updates an element in the $coll.
    *
    * This method updates data at an id. The id is found in the
    * data. The method will overwrite existing data at the id.
    *
    * @param o a class instance.
    * @return The id used. If the write fails,
    * the return is NullID.
    */
  def update[A](
    id: Long,
    o: A
  )(implicit ms: MultiTakerFieldSelect[A]) : Long



  //--------------
  // -- foreach --
  //--------------


  /** Applies a function f to all elements of this $coll.
    *
    * The function will be passed an object.
    */
  def foreachObject[A](f: (A) => Unit)(implicit fs: (Giver) => A) 
{
    foreach( (g: Giver) => { f(fs(g)) } )
  }


}//Generator
