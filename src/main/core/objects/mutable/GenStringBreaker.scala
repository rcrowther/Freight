package freight
package core.objects
package mutable



/** Templates a breaker guaranteeing methods for reading Freight types.
 *  
 * A breaker deconstructs some source incrementally, by taking the source, then extracting elements using typed methods.
 *  
 */
trait GenStringBreaker
//extends StringGiver
extends GiverForString
with BreakerOps[String]

object GenStringBreaker {

private val emptyThing : GenStringBreaker = new GenStringBreaker{
  def reload(src: String): Unit = ???

  // Members declared in freight.core.iface.StringGiver
  def binaryStr(): String = ???
  def booleanStr(): String = ???
  def doubleStr(): String = ???
  def floatStr(): String = ???
  def intStr(): String = ???
  def localeStr(): String = ???
  def longStr(): String = ???
  def shortStr(): String = ???
  def stringStr(): String = ???
  def textStr(): String = ???
  def timeStr(): String = ???
  def timestampStr(): String = ???
}

def empty() : GenStringBreaker = emptyThing


}//GenStringBreaker
