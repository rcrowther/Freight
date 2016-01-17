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
 val b = interface.html.ObjectAnchoredFieldBuilder("article", 2, "article")
 val o = f("1", (g: StringGiver) => {Paper.stringTransformer(b, g)})
 b.result

 val b2 = EmptyPot.testEmptyPot()
 b2 += b
 b2.result
 */

/** Builds neutral (x)html from objects, with one field as an anchor.
  *
  * Most data is surrounded with a `div` element, but times and dates
  * are surrounded with a `time` element.
  *
  * The href is the hrefBase followed by the id.
  *
  * @param b a stringbuilder to append data to.
  * @param objectName surrounds each object with named tags.
  * @param anchoredFieldIdx index of the field to place in anchor
  *  tags.
  * @param hrefBase used as the base for the link ref in the anchor
  *  tags. No trailing slash.
  */
// TODO: Inherit?
class ObjectAnchoredFieldBuilder(
  b: StringBuilder,
  val objectName: String,
  anchoredFieldIdx: Int,
  hrefBase: String
)
    extends core.objects.mutable.GenManyObjectsStringBuilder[StringBuilder]
{

  private var firstLong = true
  private var firstObject = true
  private var currentId = "0"
  private var i = 0

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
    if (i == anchoredFieldIdx) {
      b ++= "<a href=\""
      b ++= hrefBase
      b += '/'
      b ++= currentId
      b ++= "\">"
      b ++= v
      b ++= "</a>"
    }
    else b ++= v

    b ++= "</div>\n"

    i += 1
  }

  def timeTag(fieldName: String, v: String) {
    b ++= "<time class=\""
    b ++= fieldName
    b ++= "\">"
    if (i == anchoredFieldIdx) {
      b ++= "<a href=\""
      b ++= hrefBase
      b += '/'
      b ++= currentId
      b ++= "\">"
      b ++= v
      b ++= "</a>"
    }
    b ++= "</time>\n"
    i += 1
  }

  def booleanStr(fieldName: String, v: String) = div(fieldName, v)
  def shortStr(fieldName: String, v: String) = div(fieldName, v)
  def intStr(fieldName: String, v: String) = div(fieldName, v)

  def longStr(fieldName: String, v: String) = {
    if (firstLong) {
      currentId = v
      firstLong = false
    }
    div(fieldName, v)
  }

  def floatStr(fieldName: String, v: String) = div(fieldName, v)
  def doubleStr(fieldName: String, v: String) = div(fieldName, v)
  def stringStr(fieldName: String, v: String) = div(fieldName, v)
  def textStr(fieldName: String, v: String) = div(fieldName, v)
  def binaryStr(fieldName: String, v: String) = div(fieldName, v)
  def timeStr(fieldName: String, v: String)= timeTag(fieldName, v)
  def timestampStr(fieldName: String, v: String)= timeTag(fieldName, v)
  def localeStr(fieldName: String, v: String) = div(fieldName, v)

  def newObject() = {
    if (firstObject) firstObject = false
    else {
      b ++= "</div>\n<div class=\""
      b ++= objectName
      b ++= "\">\n"
    }
    firstLong = true
    i = 0
  }

  def clear() = {
    b.clear()
    init()
    firstObject = true
    firstLong = true
    i = 0
  }

  def result(): StringBuilder = {
    b ++= "</div>\n"
    b
  }

}//ObjectAnchoredFieldBuilder




object ObjectAnchoredFieldBuilder {

  def apply (
    objectName: String,
    anchoredFieldIdx: Int,
    hrefBase: String
  )
      : ObjectAnchoredFieldBuilder =
  {
    new ObjectAnchoredFieldBuilder(
      new StringBuilder(),
      objectName,
      anchoredFieldIdx,
      hrefBase
    )
  }

  def apply (
    b: StringBuilder,
    objectName: String,
    anchoredFieldIdx: Int,
    hrefBase: String
  )
      : ObjectAnchoredFieldBuilder =
  {
    new ObjectAnchoredFieldBuilder(b, objectName, anchoredFieldIdx, hrefBase)
  }

}//ObjectAnchoredFieldBuilder
