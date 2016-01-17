package freight
package core
package collection

import java.nio.channels.ReadableByteChannel

/** Mixes in the passing of output methods, as parameters, to a data supply.
  *
* This trait realizes a [[PluggableReadable]] for giver collections.
  */
trait PluggableGiverReadable
extends GenPluggableReadable[Giver]

