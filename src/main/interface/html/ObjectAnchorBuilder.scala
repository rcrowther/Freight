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
 val b = interface.html.ObjectAnchorBuilder("article", 2, "article")
 val o = f("1", (g: StringGiver) => {Paper.stringTransformer(b, g)})
 b.result

 val b2 = EmptyPot.testEmptyPot()
 b2 += b
 b2.result
 */

/** Builds (x)html anchors from objects into a list.
  *
  * The objects are built into a (x)html list of li elements
  * containing anchors. The list will need surrounding with list
  * ('\<ul\>' etc.) declarations.
  *
  * The href is the hrefBase followed by the id.
  *
  * `result` should always be called on the builder after appending
  * data, even if the return from the method is not used.
  *
  * @param b a stringbuilder to append data to.
  * @param objectName surrounds each object with named tags.
  * @param anchorTextIdx index of the field to place in anchor
  *  tags.
  * @param hrefBase used as the base for the link ref in the
  *  anchor tags. A trailing slash is inserted automatically. The
  *  identified field must contain a string.
  * @param hrefPathIdx index of the field used for the identifying
  *  tail of the anchor href. The identified field must contain a
  *  string.
  */
// TODO: Inherit?
// TODO: Offer variable tags (lis, div, span...)
class ObjectAnchorBuilder(
  b: StringBuilder,
  val objectName: String,
  anchorTextIdx: Int,
  hrefBase: String,
  hrefPathIdx: Int
)
    extends core.objects.mutable.GenManyObjectsStringBuilder[StringBuilder]
{

  private var firstObject = true
  private var currentId = "0"
  private var i = 0

  private var hrefPath = ""
  private var anchorText = ""

  // Auto init
  init()

  def init() {
    b ++= "<li class=\""
    b ++= objectName
    b ++= "\">\n"
  }


  def tag(fieldName: String, v: String) {
    if (i == anchorTextIdx) {
      anchorText = v
    }
    if (i == hrefPathIdx) {
      hrefPath = v
    }
    i += 1
  }

  def writeAnchor() {
    b ++= "<a href=\""
    b ++= hrefBase
    b += '/'
    b ++= hrefPath
    b ++= "\">"
    b ++= anchorText
    b ++= "</a>"
  }



  def booleanStr(fieldName: String, v: String) = tag(fieldName, v)
  def shortStr(fieldName: String, v: String) = tag(fieldName, v)
  def intStr(fieldName: String, v: String) = tag(fieldName, v)
  def longStr(fieldName: String, v: String) =  tag(fieldName, v)
  def floatStr(fieldName: String, v: String) = tag(fieldName, v)
  def doubleStr(fieldName: String, v: String) = tag(fieldName, v)
  def stringStr(fieldName: String, v: String) = tag(fieldName, v)
  def textStr(fieldName: String, v: String) = tag(fieldName, v)
  def binaryStr(fieldName: String, v: String) = tag(fieldName, v)
  def timeStr(fieldName: String, v: String)= tag(fieldName, v)
  def timestampStr(fieldName: String, v: String)= tag(fieldName, v)
  def localeStr(fieldName: String, v: String) = tag(fieldName, v)

  def newObject() = {
    if (firstObject) firstObject = false
    else {
      writeAnchor()
      b ++= "</li>\n<li class=\""
      b ++= objectName
      b ++= "\">\n"
    }

    i = 0
  }

  def clear() = {
    b.clear()
    init()
    firstObject = true
    hrefPath = ""
    anchorText = ""
    i = 0
  }

  def result(): StringBuilder = {
    writeAnchor()
    b ++= "</li>\n"
    b
  }

}//ObjectAnchorBuilder




object ObjectAnchorBuilder {

  def apply (
    objectName: String,
    anchorTextIdx: Int,
    hrefBase: String,
    hrefPathIdx: Int
  )
      : ObjectAnchorBuilder =
  {
    new ObjectAnchorBuilder(
      new StringBuilder(),
      objectName,
      anchorTextIdx,
      hrefBase,
      hrefPathIdx
    )
  }

  def apply (
    b: StringBuilder,
    objectName: String,
    anchorTextIdx: Int,
    hrefBase: String,
    hrefPathIdx: Int
  )
      : ObjectAnchorBuilder =
  {
    new ObjectAnchorBuilder(
      b,
      objectName,
      anchorTextIdx,
      hrefBase,
      hrefPathIdx
    )
  }

}//ObjectAnchorBuilder
