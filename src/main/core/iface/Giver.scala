package freight
package core
package iface



import java.util.Locale


/** Templates methods for returning Freight types.
  *  
  * Implementations of this trait return typed data from freight
  * fields.
  * 
  * Inheriting classes are often breakers.
  *
  * Givers are data structures that allow to repeatedly elements of
  * data from some source. They have methods for returning typed
  * data. Calling code must know which types to call, and the order to
  * call in.
  *
  * Givers are mutable: operations change its state. They are most
  * often used to wrap interface returns in a consistent
  * interface. They can also be used to mutate between object data, or
  * be backed by other collections of data (e.g. see StringObject,
  * backed by an array of strings).
  *
  * It is important to note that, unless stated otherwise (for
  * example, in a resettable breaker) a Giver can only be used
  * once. Generally, they should be consumed by purpose built methods
  * (which will contain FieldSelectors, for defining the calls).
  *
  * Givers are not typed, but carry an idea not only of the types
  * used, but the overall type of the object they represent (the field
  * types, and the order called). In terms used elsewhere in
  * computing, they are `duck-typing` objects
  */
trait Giver
    extends Any
{
  def boolean() : Boolean
  def short() : Short
  def int() : Int
  def long() : Long
  def float() : Float
  def double() : Double
  def string() : String
  def text() : String
  def binary() : Array[Byte]
  def time() : Long
  def timestamp() : Long
  def locale() : Locale

  def booleanStr() : String
  def shortStr() : String
  def intStr() : String
  def longStr() : String
  def floatStr() : String
  def doubleStr() : String
  def stringStr() : String
  def textStr() : String
  def binaryStr() : String
  def timeStr() : String
  def timestampStr() : String
  def localeStr() : String
}



object Giver {

  /** Returns the id from a giver.
    *
    * If the id is all that is needed, this method will return it.
    *
    * It is of importance to note that, unless reloadable, the giver
    * can not be used after this method is called on it. The method is
    * placed in the complementary object to encourage cautious use.
    */
  def id(g: Giver): Long = g.long()

}
