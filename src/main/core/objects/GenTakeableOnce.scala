package freight
package core
package objects


//import freight.core.iface.{MultiTaker, StringTaker}

/** Templates takeable-once objects.
*
 *  @define obj object or multi-type item
*/
// Like GenTraversableOnce, but realised like TraversableOnce
trait GenTakeableOnce extends Any
{

/** A long value used as id.
 * 
 * 'Id' values are used to carry unique identifier numerics.
 * Several interfaces may return them on writing, or use them for 
 * selecting contents.
*
* An id is mantatory for an object to use the interfaces. The id must also be declared first in the supporting structures such as field selectors (the id field itself can be tagged into a class).
*/
  def id: Long

  /** Gives data from this $obj to a multi taker.
    *  
    * @param t the taker to which elements are appended.
    */
 // def give(t: MultiTaker)

  /** Gives data from this $obj to a string taker.
    *  
    * @param t the string taker to which elements are appended.
    */
 // def give(t: StringTaker) 

  /** Tests whether this $obj can repeatedly give.  Always
   *  true for Traversables and false for Iterators unless overridden.
   *
   *  @return `true` if it is repeatedly givable, `false` otherwise.
   */
  //def isGivableAgain: Boolean

// String gear...
}
