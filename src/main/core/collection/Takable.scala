package freight
package core
package collection




/** Templates a collection with methods accepting takers.
  *
* The interface defines a set of IARUD methods, with convenience methods
* added for accepting a [[Taker]]
  *
* This generic giver and taker only collection 
* is independant of type. This is where [[BinaryTakable]] branches, as it does not include object returns or a field-based query interface. If no field-based querying is necessary, this class 
* can accept both [[GiverTakable]] and [[BinaryTakable]].
*
  * @define coll collection
  *
  * @tparam G the element type of the data
  * @tparam T the element type of builder params
  */
trait Takable[G, T]
 extends Any 
with Transferable[G]
{

  /** Applies element data to a taker.
    *
    *
    * @param id the id of an element providing binary data.
    * @param t the taker.
    */
  def apply(
    id: Long,
    t: T
  ): Boolean

  /** Applies a taker to all elements in this $coll.
    *
    * Note: default methods often call apply() repeatedly. This method
    * should be overridden in many implementations, as more efficient
    * implementations will exist.
    */
  def foreach(t: T)
 : Boolean 

}//Takable
