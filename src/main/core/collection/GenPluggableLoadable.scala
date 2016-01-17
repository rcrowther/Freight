package freight
package core
package collection



/** Mixes in the passing of input methods, as parameters, to a data supply.
  *
* Will template
* a class with a `load` method. The method accepts a [[Transferrable]] `apply` method.
  *
  * @tparam G the element type of the data
  */
trait GenPluggableLoadable[G] extends Any 
{

/** Plugs an apply method.
*
* This method will take a collection `apply` and do some action by calling the method.
*
* @param f a method matching the transferable method `apply`. 
*/
  def loadFrom(
    id: Long,
    f: (Long, (G) => Unit) => Boolean
  ) : Boolean

/** Plugs a foreach method.
*
* This method will take a collection `foreach` and do some action by calling the method.
*
* The method must be overridden to do a useful action. By default,
* it throws an error.
*
* @param f a method matching the transferable method `foreach`. 
*/
  def loadFrom(
    f: ((G) => Unit) => Boolean
  )
 : Boolean =
{
error("Class not enabled for field-selection query")
}

/** Plugs a field-selecting apply method.
*
* This method will take a collection field-selecting `apply` and do some action by calling the method.
*
* The method must be overridden to do a useful action. By default,
* it throws an error.
*
* @param f a method matching the transferable method `foreach`. 
*/
  def loadFrom(
    fieldIdxs: Seq[Int],
    idx: Long,
    f: (Seq[Int], Long, (G) => Unit) => Boolean
  )
 : Boolean =
{
error("Class not enabled for field-selection query")
}

}//PluggableReader
