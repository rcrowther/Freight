package freight
package canspeak
package builder

import core.objects.mutable.WritingStringBuilder



/** Builds XML output.
  *
  * @param overrideId an id value to overide the first long
  * supplied to the builder.
  */
final class XMLWriting(
  overrideId: Option[String]
)
extends XML
with WritingStringBuilder
{

  private var firstLong = true
  var generatedId = NullIDStr
  var wasNew = false

 override def longStr(fieldName: String, v: String) {
    if (firstLong) {
      firstLong = false

      // NB: if the id is changed, must be an insert or append
      // otherwise, it's an update
      wasNew = (overrideId != None)

      generatedId =
        if(wasNew) overrideId.get
        else v

      tag(fieldName, "long", generatedId)
    }
    else {
      tag(fieldName, "long", v)
    }

  }

}//XMLWriting



