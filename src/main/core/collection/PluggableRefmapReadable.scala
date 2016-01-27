package freight
package core
package collection

import scala.collection.mutable.ArrayBuffer

/** Mixes in the passing of output methods, as parameters, to a data supply.
  *
* This trait realizes a [[PluggableReadable]] for binary collections.
  */
trait PluggableRefmapReadable
{

/** Plugs an append method.
*
* This method will take a collection `append` and do some action by calling the method.
*
* @param f a method matching the transferable method `+`. 
*/
  def writeAppend(
    f: (Long, TraversableOnce[Long]) => Boolean
  ) : Boolean

/** Plugs an update method.
*
* This method will take an `update` and do some action by calling the method.
*
* @param f a method matching the transferable method `~`. 
*/
  def writeUpdate(
f: (Long, TraversableOnce[Long]) => Boolean
) : Boolean

}//PluggableRefmapReadable

