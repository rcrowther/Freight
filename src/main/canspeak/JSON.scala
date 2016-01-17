package freight
package canspeak

import core.iface.StringCanSpeak

object JSON 
extends StringCanSpeak
{
  def to(overrideId: Option[String]) = new builder.JSONWriting(overrideId)
  def from(d: String) = new breaker.JSON(d)
}
