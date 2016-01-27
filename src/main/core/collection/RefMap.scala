package freight
package core
package collection

import scala.collection.mutable.ArrayBuffer
import objects.GenTakeableOnce



/** Templates a collection which can map one id to another.
  *
 * The mapping allows multiple values for one key.
*
  * @define coll refmap
  */
//TODO: Look in sqliteJDBCRefMap
trait RefMap 
 extends Any 
with GenCollection
{



  /** Insert a key value.
    *
    * Whatever the id in the element data supplied, a new id will be
    * generated and the element appended/inserted, not updated???
    *
    * In freight, this method is mainly intended for bulk maintenace
    * (copying of persistent storage, writing down streamed input,
    * etc.). For common transfers of data between interfaces, prefer
    * the method `>` ('merge').
    *
    * @return The new id generated. If the write fails,
    * the return is NullID.
    */
  def ^(k: Long, v: Long) : Boolean
  def ^(k: Long, v: TraversableOnce[Long]) : Boolean


  /** Select values by key in this $coll.
    *
    */
  def apply(k: Long) : ArrayBuffer[Long]

  /** Select keys by value in this $coll.
    *
    */
  def keysByVal(v: Long) : ArrayBuffer[Long]



  /** Updates an element in the $coll.
    *
    * This method replaces all key values with a new key/value. The method will overwrite existing data at the id.
    *
* The method is enabled as a delete followed by an append.
*
    * @param g a giver.
    * @return The id used. If the write fails,
    * the return is NullID.
    */
  def ~(k: Long, v: Long)
 : Boolean =
{
  var ok = this.-(k)

ok = ok && this.^(k, v) 

ok
}

  /** Updates an element in the $coll.
    *
    * This method replaces all key values with a new key and multiple values. The method will overwrite existing data at the id.
    *
* The method is enabled as a delete followed by an append.
*
    * @param g a giver.
    * @return The id used. If the write fails,
    * the return is NullID.
    */
  def ~(k: Long, v: TraversableOnce[Long])
 : Boolean =
{
  var ok = this.-(k)
log(s"Update delete ok:$ok" )
ok = ok && this.^(k, v) 
log(s"Update insert ok:$ok" )
ok
}

  /** Deletes elements from this $coll by key.
    *
    * Is silent if the element can not be found.
    *
    * @return true if elements are found and sucessfully deleted, else false.
    */
  def -(k: Long) : Boolean

  /** Deletes many elements from this $coll by key.
    *
    * Is silent if the element can not be found.
    *
    * @return true if elements are found and sucessfully deleted, else false.
    */
  def -(k: TraversableOnce[Long]) : Boolean

  /** Deletes an element from this $coll by id and value.
    *
* Refmaps are not unique on the key data, so this is occasionally useful for
* certain deletion of an element.
*
    * Is silent if the element can not be found.
    *
    * @return true if an element is found and sucessfully deleted, else false.
    */
  //def -(k: TraversableOnce[Long], v: Long) : Boolean

  def removeByVal(v: Long) : Boolean

  /** Select keys in this $coll.
    *
* This foreach-like method returns *distinct* keys,
* not a repetitive selection of all keys from all key values.
    */
def distinctKeys(f: (Long) ⇒ Unit) 


  def foreach(f: ((Long, Long)) ⇒ Unit) : Unit


    /** Counts elements for a key.
    *
    * @return the number of elements mapped to the key.
    */
  def size(id: Long) : Long

  /** The number of distinct keys in this $coll.
    *
    * For some interfaces, `size` must be calculated, and can be
    * expensive to call. Note also that on some interfaces with
    * infinite sized collections, it may not terminate.
    *
    * @return the number of distinct keys in this collection.
    */
def distinctKeySize() : Long =
{
var n: Long = 0
distinctKeys((id: Long) => {n += 1})
n
}
 
}//RefMap
