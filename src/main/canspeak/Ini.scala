package freight
package canspeak

import core.iface.StringCanSpeak


object Ini 
extends StringCanSpeak
{
  def to(overrideId: Option[String]) = new builder.IniWriting(overrideId)
  def from(d: String) = new breaker.Ini(d)
}
