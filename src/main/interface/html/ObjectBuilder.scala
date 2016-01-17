package freight
package interface
package html

/*

 import freight._
 import freight.interface.file._
 import freight.objects.Paper

 //import freight.canspeak.XML

 val f = Base.testBase

 // Read
 val b = new interface.html.ObjectBuilder()
 val o = f("1", (g: StringGiver) => {Paper.stringTransformer(b, g)})
 b.result

 val b2 = EmptyPot.testEmptyPot()
 b2 += b
 b2.result
 */

/** Builds neutral (x)html from objects.
  *
  * This class can build data for a single object only. Calling
  * `clear` will reset the stringbuilder.
  *
  * Most data is surrounded with a `div` element, but times and dates
  * are surrounded with a `time` element.
  *
  * For multiple objects, see [[ManyObjectBuilder]].
  * 
  * @param b a stringbuilder to append data to.
  * @param objectName surrounds each object with named tags.
  */
class ObjectBuilder(
  b: StringBuilder,
  objectName: String
)
    extends core.objects.mutable.GenStringBuilder[StringBuilder]
{

  // Auto init
  init()

  def init() {
    b ++= "<div class=\""
    b ++= objectName
    b ++= "\">\n"
  }

  def div(fieldName: String, v: String) {
    b ++= "<div class=\""
    b ++= fieldName
    b ++= "\">"
    b ++= v
    b ++= "</div>\n"
  }

  def timeTag(fieldName: String, v: String) {
    b ++= "<time class=\""
    b ++= fieldName
    b ++= "\">"
    b ++= v
    b ++= "</time>\n"
  }

  def booleanStr(fieldName: String, v: String) = div(fieldName, v)
  def shortStr(fieldName: String, v: String) = div(fieldName, v)
  def intStr(fieldName: String, v: String) = div(fieldName, v)
  def longStr(fieldName: String, v: String) = div(fieldName, v)
  def floatStr(fieldName: String, v: String) = div(fieldName, v)
  def doubleStr(fieldName: String, v: String) = div(fieldName, v)
  def stringStr(fieldName: String, v: String) = div(fieldName, v)
  def textStr(fieldName: String, v: String) = div(fieldName, v)
  def binaryStr(fieldName: String, v: String) = div(fieldName, v)
  def timeStr(fieldName: String, v: String)= timeTag(fieldName, v)
  def timestampStr(fieldName: String, v: String)= timeTag(fieldName, v)
  def localeStr(fieldName: String, v: String) = div(fieldName, v)


  def clear() = {
    b.clear()
    init()
  }

  def result(): StringBuilder = {
    b ++= "</div>\n"
    b
  }

}//ObjectBuilder



object ObjectBuilder {

  def apply (
    objectName: String
  )
      : ObjectBuilder =
  {
    new ObjectBuilder(new StringBuilder(), objectName)
  }

  def apply (
    b: StringBuilder,
    objectName: String
  )
      : ObjectBuilder =
  {
    new ObjectBuilder(b, objectName)
  }

}//ObjectBuilder
