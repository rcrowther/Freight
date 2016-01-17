package freight
package core
package iface

import objects.mutable.WritingStringBuilder


/** Encapulates encoding.
  * 
  * Used in Interface code. Two methods are provided to provide giver
  * and a taker for converting to and from an encoding. Sometimes
  * CanSpeaks will be invisible if the interface has only one form of
  * output encoding (e.g. HTML). Sometimes CanSpeaks are a pluggable
  * component available to the coder
  * e.g. [freight.interface.file.Entry].
  */
// This has been opportunistically set to build/breakers
// Is that right, or should we abstract lower too?
trait StringCanSpeak {

  /** Returns a taker.
    */
  def to(overrideId: Option[String]): WritingStringBuilder


  /** Returns a giver.
    */
  def from(d: String): GenStringBreaker


}//StringCanSpeak
