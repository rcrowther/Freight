package freight
package core
package collection



/** Mixes in the passing of output methods, as parameters, to a data supply.
  *
* Will template a
* class with `read` methods. The methods will accept [[Transferrable]] methods `^`, `+` and `~`.
  *
  * @tparam G the element type of the data
  */
trait GenPluggableReadable[G] extends Any 
{



// TODO:     f: (Long, G) => Long
// will plug to everything? 'cept append.
  def writeTo(
    f: (G) => Unit
  ) : Boolean

/** Plugs an insert or update method.
*
* This method will take a collection `insert` or `update` and do some action by calling the method.
*
* @param f a method matching the transferable method `^` or `~`. 
*/
  def writeTo(
    id: Long,
    f: (Long, G) => Boolean
  )
 : Boolean =
{
var ok = true
ok &= writeTo((g: G) => { ok &= f(id, g)} )
ok
}

/** Plugs an append method.
*
* This method will take a collection `append` and do some action by calling the method.
*
* @param f a method matching the transferable method `+`. 
*/
  def writeTo(
    f: (G) => Long
  )
 : Long =
{

var newId = NullID
writeTo((g: G) => { newId = f(g)} )
newId
}



}//PluggableReader
