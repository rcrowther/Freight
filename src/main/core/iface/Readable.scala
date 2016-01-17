package freight.core.interfaces

import scala.annotation.tailrec

/** Templates an interface that can read
 *  using a `-=` operator.
 *
 *  @define iface readable interface
 *  @define Iface `Readable`
 */
// NB: vary like scala.ifaceection.generic Growable - the missing link!
trait Readable[+A] extends ReadOne[A] {

  /** Reads all datas from this $iface.
   *
   *  @param data1 the first data to read.
   *  @param data2 the second data to read.
   *  @param datas the remaining datas to read.
   *  @return the $iface itself
   */
// TODO: Dunno if we want this yet?
  //def apply(): = this += data1 += data2 ++= datas

  /** Reads two or more datas from this $iface to a seq.
   *
   *  @param xs   the TraversableOnce producing the datas to read.
   *  @return  the $iface itself.
   */
  def apply(n: Int): Seq[A] = {
     val b = Seq.newBuilder[A]
     b += apply()
     b.result()
  }
}//Readable
