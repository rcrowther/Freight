package freight
package core
package collection

import java.nio.channels.ReadableByteChannel


/** Mixes in the passing of input methods, as parameters, to a data supply.
  *
* This trait realizes a [[PluggableLoadable]] for binary collections.
  */
trait PluggableBinaryLoadable
extends GenPluggableLoadable[(ReadableByteChannel, Long)]
