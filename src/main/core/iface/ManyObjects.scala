package freight
package core
package iface


/** A trait containing a method for signalling new objects.
  *
  * Allows a builder to handle many objects supplied sequentially,
  * rather than clearing, or producing individual builders.
  *
  * Builders implementing the method will likely open an object
  * automatically, and close an object on `result`. So only the one
  * method, which separates objects, is needed.
  */
trait ManyObjects extends Any {

  /** Signals an object is closed and that a new object should be opened.
    *
    * This method is implemented as a separator. The first call will
    * not perform an action, only register it has been called. For
    * example, implemented in a builder, the following code,
    *
    * {{{
    * val b = new builder()
    *
    * foreach{ x => 
    *   b.newObject()
    *   b.method(x)
    * }
    *
    * b.result()
    * }}}
    *
    * will result in output resembling,
    *
    * {{{
    *  > objectResult (newObjectResult objectResult) (newObjectResult objectResult)...
    * }}}
    *
    */
  def newObject()

}
