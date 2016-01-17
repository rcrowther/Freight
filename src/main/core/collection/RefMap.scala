package freight
package core
package collection

import scala.collection.mutable.ArrayBuffer
import objects.GenTakeableOnce



/** Templates a collection which can map one id to another.
  *
  * @define coll refmap
  */
//TODO: Look in sqliteJDBCRefMap
trait RefMap 
 extends Any 
with GenCollection
{



  /** Select a value by key in this $coll.
    *
    */
  def apply(k: Long) : ArrayBuffer[Long]

  /** Select keys by value in this $coll.
    *
    */
  def keysByVal(v: Long) : ArrayBuffer[Long]

  /** Append an element.
    *
    * Whatever the id in the element data supplied, a new id will be
    * generated and the element appended/inserted, not updated.
    *
    * In freight, this method is mainly intended for bulk maintenace
    * (copying of persistent storage, writing down streamed input,
    * etc.). For common transfers of data between interfaces, prefer
    * the method `>` ('merge').
    *
    * @return The new id generated. If the write fails,
    * the return is NullID.
    */
  def +(k: Long, v: Long) : Boolean
  def +(k: Long, v: TraversableOnce[Long]) : Boolean

  def foreach(f: ((Long, Long)) ⇒ Unit) : Unit

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

    /** Counts elements for a key.
    *
    *
    * @return the number of elements mapped to the key.
    */
  def size(id: Long) : Long


}//RefMap
