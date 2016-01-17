package freight
package core
package collection



/** Mixes in the passing of output methods, as parameters, to input methods.
  *
* Very useful so is base functionality. Can alwso be used to template
* classes accepting `read` methods, so they accept [[Transferrable]] methods `^`, `+` and `~` directly.
  *
  * @tparam G the element type of the data
  */
trait GenPluggable[G] extends Any 
{



// TODO:     f: (Long, G) => Long
// will plug to everything? 'cept append.
  def apply(
    id: Long,
    f: (G) => Unit
  ) : Boolean

/** Plugs an insert or update method into a select method.
*/
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

/** Plugs an append method into a select method.
*/
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



}//GenPluggable
