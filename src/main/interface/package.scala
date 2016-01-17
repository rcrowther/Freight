package freight

import scala.language.implicitConversions
import scala.annotation._, elidable._




/**
*
* == Structure ==
* `out` interfaces have a common structure. A `connector` is instanciated, and then a `collection`, targeting one collection of raw objects, is created from this. 
*
* The only exception is that some `out` interfaces have no connector, they only
* need a `collection` or `target` to be instanciated.
*  
* == Concurrency ==
* If the connection is prefixed with `Sync...` then it is safe for use with multiple threads.
*
* The Freight model is this - only one `Connection` is allowed per Freight instance. Multiple `Collection`s may be spun off this. Currently, there is no guarantee that if multiple `Collections` point at the same `Collection`, they will be threadsafe. But if they point at different collections, they will be.  
*
* == Availability ==
*
* Often, only a `SyncConnection` is available. This is because the underlying interface code is organised for concurrent operation.
*
* == Accessors ==
  * Note that most interfaces have private accessors i.e., they can
  * not be created with `new`, but must use a factory mthod e.g.
  * 
  * {{{
  * val f = Entry(freight.canspeak.XML, "/home/pete/db/boop.xml", StandardCharsets.UTF_8)
  * }}}
  * 
  * This is because the object is a single contact point and may carry
  * data and code for ensuring concurrency and/or cataloguing users.
  *
  * @define pkg interface
  */
package object interface {





  // Exceptions, warnings, and Annotations
/*
  /** An exception that indicates an error during interface actions.
    */
  case class FreightInterfaceException(msg: String) extends Exception(msg)

  /** Conditionally prints a warning during $pkg actions.
    */
  @elidable(WARNING) def warningIf(trigger: Boolean, msg: String) = {
    if (trigger) println(s"FreightInterface: warning: $msg")
  }

  /** Conditionally throws an error during $pkg actions.
    */
  @elidable(SEVERE) def errorIf(trigger: Boolean, msg: String) = {
    if (trigger) {
      throw new FreightInterfaceException(msg)
    }
  }
*/
}
