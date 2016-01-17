package freight
package canspeak
package builder

import core.objects.mutable.WritingStringBuilder



/** Builds JSON output.
  *
  * @param overrideId an id value to overide the first long
  * supplied to the builder.
  */
//TODO: Only for one offs. JSON needs wrapping,
// not figured separators (yet)
final class JSONWriting(
  overrideId: Option[String]
)
    extends JSON
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

      plain(fieldName, generatedId)
    }
    else {
      plain(fieldName, v)
    }

  }


}//JSONWriting


