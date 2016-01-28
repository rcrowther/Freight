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
    with Transferable[TraversableOnce[Long]]
{



  /** Inserts a new key/value pair into the $coll.
    * 
    * This method creates new data at a given id. If data exists at
    * the id, the result is undetermined (often a fail).
    *  
    *  @param k the new key.
    *  @param v the new value.
    *  @return true if the selection was succcessful, else false.
    */
  def ^(k: Long, v: Long) : Boolean
  def ^(k: Long, v: TraversableOnce[Long]) : Boolean

  /** Do not call - throws exception.
    */
  def +(
    g: TraversableOnce[Long]
  ) : Long =
  {
    error("'append' method called on Refmap - this method is not available")
  }

  /** Select values in this $coll by key.
    *
    */
  def apply(k: Long) : ArrayBuffer[Long]

  // TODO: Fix return
  def apply(
    id: Long,
    f: (TraversableOnce[Long]) => Unit
  )
      : Boolean =
  {
    f(apply(id))
    true
  }

  /** Select keys by value in this $coll.
    *
    */
  def keysByVal(v: Long) : ArrayBuffer[Long]



  /** Updates an element in the $coll.
    *
    * This method replaces key/value data. The method will overwrite
    * existing data at the id.
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
    * This method replaces a key and values. The method will overwrite
    * existing data at the id.
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

  /** Deletes values from this $coll.
    *
    * Is silent if the element can not be found.
    *
    * @return true if elements are found and sucessfully deleted, else false.
    */
  def -(k: Long) : Boolean

  /** Deletes values from this $coll by many keys.
    *
    * Is silent if the element can not be found.
    *
    * @return true if elements are found and sucessfully deleted, else false.
    */
  def -(ks: TraversableOnce[Long]) : Boolean

  /** Deletes an element from this $coll by id and value.
    *
    * Refmaps are not unique on the key data, so this is occasionally
    * useful for certain deletion of an element.
    *
    * Is silent if the element can not be found.
    *
    * @return true if an element is found and sucessfully deleted, else false.
    */
  //def -(k: TraversableOnce[Long], v: Long) : Boolean

  def removeByVal(v: Long) : Boolean



  /** Applies a function to every single entry inn the map.
    * 
    * Often faster than the value-gathering `foreach` method, so
    * useful for backups and other raw interface handling.
    */
  def foreach(f: ((Long, Long)) ⇒ Unit) : Unit




  /** Select keys in this $coll.
    *
    * This foreach-like method returns *distinct* keys,
    * not a repetitive selection of all keys from all key values.
    */
  def keys(f: (Long) ⇒ Unit)


  /** Counts values on a key.
    *
    * @return the number of elements mapped to the key.
    */
  def size(k: Long) : Long


  
}//RefMap
