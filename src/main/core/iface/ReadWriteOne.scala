package freight.core.interfaces


/** Templates an interface that can read and write one item.
 *
 */
trait ReadWriteOne[A]
extends ReadOne[A]
with WriteOne[A]
