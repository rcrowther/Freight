package freight.core.interfaces

import scala.annotation.tailrec

/** Templates an interface that can write
 *  using a `+=` operator.
 *
 *  @define iface writable interface
 *  @define Iface `Writeable`
 */
// NB: vary like scala.collection.generic Growable - the missing link!
trait WriteOne[-A] extends AnyRef {
  /** Writes a single data to this $iface.
   *
   *  @param data  the data to write.
   *  @return the $iface itself
   */
  def +=(data: A): this.type

}//WriteOne
