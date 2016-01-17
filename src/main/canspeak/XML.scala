package freight
package canspeak

import core.iface.StringCanSpeak

// see http://hmkcode.com/castor-java-object-xml/
object XML 
extends StringCanSpeak
{
  def to(overrideId: Option[String]) = new builder.XMLWriting(overrideId)
  def from(d: String) = new breaker.XML(d)
}
