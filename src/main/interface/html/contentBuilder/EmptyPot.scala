package freight
package interface
package html
package contentBuilder

/*
 import freight.interface.html.EmptyPot
 val b = EmptyPot.testEmptyPot()
 */
/** Builds HTML page/body content with headers, footer, and two free boxes.
  *
  * EmptyPot uses two ideas:
  * 
  *  - (from ZenGarden) free boxes at the bottom of the HTML.
  *
  * This is not much use for default browser rendering, aside from
  * being robust (box contents will show, even if in an odd
  * position). But it is search engine friendly (significant content
  * is first in the HTML tags), and css absolute positioning can put
  * the boxes where you need them (probably as sidebars, but see
  * http://www.csszengarden.com/)
  *
  *  - the theme reflects typograpy. 
  * 
  * Tags are IDed with vaguely typographical names, and that's the
  * purpose of the `marks`, to introduce new content to the header and
  * footer. In printing that would be chapter or author names etc.,
  * but on the web might be breadcrumbs, RSS feeds, anything that
  * could be usefully split from the header or footer content for
  * theming or structural reasons.
  */
class EmptyPot(
  header: String,
  headMark: String,
  messages: String,
  footer: String,
  footMark: String,
  box0: String,
  box1: String
)
{
  val b = new StringBuilder()

  b ++= "\n<div id=\"page\">\n  <header id=\"pageHeader\">\n"
  b ++= headMark
  b ++= "\n"

  if (header != "") {
    b ++= "    <div id=\"header\">\n"
    b ++=  header
    b ++=  "\n    </div>\n"
  }

  b ++=  "  </header>\n\n  <div id=\"pageContent\">\n"
  if (messages != "") {
    b ++= messages
  }

  b ++= "\n    <div class=\"clear-block\">\n    <main>\n"


  def ++= (sb: StringBuilder) = b append sb

  def result(): String =  {
    b ++=  "    </main>\n    </div>\n  </div>\n\n"
    b ++=  "  <footer id=\"pageFooter\">\n"
    b ++=  footMark
    b ++= "\n"

    if (footer != "") {
      b ++= "    <div id=\"footer\">\n"
      b ++=  footer
      b ++=  "\n    </div>\n"
    }
    b ++=  "  </footer>\n\n"

    if (box0 != "") {
      b ++= "  <aside id=\"box0\">\n"
      b ++= box0
      b ++= "\n  </aside>\n\n"
    }

    if (box1 != "") {
      b ++= "  <aside id=\"box1\">\n"
      b ++=  box1
      b ++=  "\n  </aside>\n\n"
    }

    b ++=  "</div>"
    b.result()
  }


}//EmptyPot



object EmptyPot {

  def apply(
    header: String,
    headMark: String,
    messages: String,
    footer: String,
    footMark: String,
    box0: String,
    box1: String
  )
      : EmptyPot =
  {
    new EmptyPot(
      header,
      headMark,
      messages,
      footer,
      footMark,
      box0,
      box1
    )
  }

  /** Generates a page with stock XHTML doctype.
    */
  def apply(
    header: String,
    messages: String,
    footer: String,
    box0: String,
    box1: String
  )
      : EmptyPot =
  {
    new EmptyPot(
      header,
      "",
      messages,
      footer,
      "",
      box0,
      box1
    )
  }

  def testEmptyPot(): EmptyPot = EmptyPot(
    "header",
    "headMark",
    "messages",
    "footer",
    "footMark",
    "box0",
    "box1"
  )

}//EmptyPot
