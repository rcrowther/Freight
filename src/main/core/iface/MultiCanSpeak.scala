package freight
package core
package iface


/** Encapulates encoding.
 * 
 * Used in Interface code. Two methods are provided to provide giver and a taker for converting to and from
* an encoding. Sometimes CanSpeaks will be invisible
* If the interface has only one form of output encoding (e.g. HTML),
* sometimes CanSpeaks are a pluggable component available to the coder e.g. [freight.interface.file.Entry].
*/
// This has been opportunistically set to build/breakers
// Is that right, or should we abstract lower too?
trait MultiCanSpeak {

  /** Returns a taker.
   */
  def from: Taker

  /** Returns a giver.
   */
  //def to: MultiGiver
  def to: Giver

}//MultiCanSpeak
