package freight.core.interfaces


/** Templates an interface that can read
 *  using a `-=` operator.
 *
 *  @define iface readable interface
 *  @define Iface `Readable`
 */
// NB: vary like scala.ifaceection.generic Growable - the missing link!
trait ReadOne[+A] extends Any {
  /** Reads a single data to this $iface.
   *
   *  @param data  the data to read.
   *  @return the $iface itself
   */
  def apply(): A

}//ReadOne
