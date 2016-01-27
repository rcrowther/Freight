package freight
package core
package collection

import scala.collection.mutable.ArrayBuffer

/** Mixes in the passing of output methods, as parameters, to input methods.
  *
* Very useful so is base functionality. Can alwso be used to template
* classes accepting `read` methods, so they accept [[Transferrable]] methods `^`, `+` and `~` directly.
  *
  */
object RefmapSignatures
{

  type Append = (Long, TraversableOnce[Long]) => Boolean
  type Read = (Long) => ArrayBuffer[Long]
  type KeysByVal = (Long)  => ArrayBuffer[Long]
  type Update = (Long, TraversableOnce[Long]) => Boolean
  type Delete = (Long) => Boolean
  type DeleteByVal  = (Long) => Boolean
  type Size = (Long) => Long

}//GenPluggable
