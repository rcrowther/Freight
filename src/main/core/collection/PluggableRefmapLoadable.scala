package freight
package core
package collection

import scala.collection.mutable.ArrayBuffer

/** Mixes in the passing of output methods, as parameters, to a data supply.
  *
* This trait realizes a [[PluggableReadable]] for binary collections.
  */
trait PluggableRefmapLoadable
{


/** Plugs an apply method.
*
* This method will take a refmap `apply` and do some action by calling the method.
*
* @param f a method matching the transferable method `apply`. 
*/
  def load(
    id: Long,
    f: (Long) => ArrayBuffer[Long]
  ) : Boolean

/** Plugs a foreach method.
*
* This method will take a refmap `foreach` and do some action by calling the method.
*
* The method must be overridden to do a useful action. By default,
* it throws an error.
*
* @param f a method matching the transferable method `foreach`. 
*/
  def loadForeach(
    f: (((Long, Long)) => Unit) => Unit
  ) : Unit

}//PluggableRefmapLoadable

