package freight
package core
package collection




/** Templates a collection with basic read and write methods.
  *
* The base trait for most collections. IARUD methods with one generic data type.
  *
  * This root trait may be useful for stub implementations and development.
*
  * @define coll transferable
  *
  * @tparam G the element type of the data
  */
trait Transferable[G] extends Any 
with GenCollection
{

  /** Inserts an element into the $coll.
    *
    * This method creates new data at a given id. Ids in the data are
    * overriden. If data exists at the id, the result is undetermined
    * (often a fail).
    * 
    * If the id is not relevant use `+`.
    *
    * @param id the id for data.
    * @param g a giver.
    * @return true if the selection was succcessful, else false.
    */
  def ^(
    id: Long,
    g: G
  ) : Boolean

  /** Appends an element to the $coll.
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
    * @param g a giver.
    * @return The new id generated. If the write fails,
    * the return is NullID.
    */
  def +(
    g: G
  ) : Long


  /** Selects an element in this $coll.
    *
    * @param id the id to select.
    * @param f a function to apply to the selected element.
    * @return true if the selection was succcessful, else false.
    */
// TODO:     f: (Long, G) => Long
// will plug to everything? 'cept append.
  def apply(
    id: Long,
    f: (G) => Unit
  ) : Boolean

/*
// Plug an insert in
  def apply(
    id: Long,
    f: (Long, G) => Boolean
  )
 : Boolean =
{
var ok = true
ok &= apply(id, (g: G) => { ok &= f(id, g)} )
ok
}

// Plug an append in
  def apply(
    id: Long,
    f: (G) => Long
  )
 : Long =
{

var newId = NullID
apply(id, (g: G) => { newId &= f(g)} )
newId
}
*/
  /** Updates an element in the $coll.
    *
    * This method updates data at an id. The id is found in the
    * data. The method will overwrite existing data at the id.
    *
    * @param g a giver.
    * @return The id used. If the write fails,
    * the return is NullID.
    */
  def ~(
    id: Long,
    g: G
  ) : Boolean



  /** Deletes an element in this $coll.
    *
    * Is silent if the element can not be found.
    *
    * @return true if an element is found and sucessfully deleted,
    *  else false.
    */
  def -(id: Long) : Boolean

  /** Applies a function to all elements in this $coll.
    *
    * The function will be passed a giver.
    *
    * Note: default methods often call apply() repeatedly. This method
    * should be overridden in many implementations, as more efficient
    * implementations will exist.
    */
  def foreach(
    f: (G) => Unit
  ) : Boolean


}//Transferable
