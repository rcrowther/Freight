package freight.core

/** Collections of objects.
  *
  * These collections are not the same as Scala collections. The Scala
  * collections library is mainly intended for in-memory use, can be
  * immutable, and most classes are have a multitude of scan methods.
  *
  * These collections are designed for wrapping external interfaces
  * with collection-type behaviour. The interfaces are mutable. They
  * are not very scannable, being reduced to a `foreach`.
  *
  * The interfaces do, however, allow many modifications to the data
  * (IARUD, like CRUD). The interfaces also have many different types
  * of method parameters. These make interaction easy without having
  * to consider the representation of data. Foremeost amongst these
  * types are the many-type data wraps, [[iface.Giver]] and
  * [[iface.Taker]]. And [[Generator]] (and it's subclasses) will
  * accept class instances, if those classes are defined with suitable
  * implicit functions.
  */
package object collection {
}//collection

