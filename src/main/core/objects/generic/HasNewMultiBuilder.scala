package freight
package core
package objects
package generic


import mutable.GenMultiBuilder

//deprecated
trait HasNewMultiBuilder[+To] extends Any {
  /** The builder that builds instances of Repr */
  protected[this] def newMultiBuilder: GenMultiBuilder[To]
}
