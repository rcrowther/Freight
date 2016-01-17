package freight.core.interfaces

import scala.annotation.tailrec

/** Templates an interface that can write
 *  using a `+=` operator.
 *
 *  @define iface writable interface
 *  @define Iface `Writeable`
 */
// NB: vary like scala.collection.generic Growable - the missing link!
trait Writeable[-A] extends WriteOne[A] {

  /** Writes two or more datas to this $iface.
   *
   *  @param data1 the first data to write.
   *  @param data2 the second data to write.
   *  @param datas the remaining datas to write.
   *  @return the $iface itself
   */
  def +=(data1: A, data2: A, datas: A*): this.type = this += data1 += data2 ++= datas

  /** Writes all datas produced by a TraversableOnce to this $iface.
   *
   *  @param xs   the TraversableOnce producing the datas to write.
   *  @return  the $iface itself.
   */
  def ++=(xs: TraversableOnce[A]): this.type = {
    @tailrec def loop(xs: scala.collection.LinearSeq[A]) {
      if (xs.nonEmpty) {
        this += xs.head
        loop(xs.tail)
      }
    }
    xs.seq match {
      case xs: scala.collection.LinearSeq[_] => loop(xs)
      case xs                                => xs foreach +=
    }
    this
  }
}//Writable
