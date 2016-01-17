package freight
package interface
package html
package contentBuilder



/*
 import freight.interface.html.content.PreparedEmptyPot
 PreparedEmptyPot.testPreparedEmptyPot()("header", "headmark", "alert! We need 'em", "not elegance but dance")
 */
/** Builds HTML page/body content with headers, footer, and two free boxes.
  *
  * PreparedEmptyPot uses two ideas:
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
  * purpose of the `marks`, to introduce extra content to the header and
  * footer. In printing that would be chapter or author names etc.,
  * but on the web might be breadcrumbs, RSS feeds, anything that
  * could be usefully split from the header or footer content for
  * theming or structural reasons.
  */
class PreparedEmptyPot(
  footer: String,
  footMark: String,
  box0: String,
  box1: String
)
{

  val b = new StringBuilder()


  b ++=  "\n  </div>\n\n"
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
  
  val pageClose: String =  b.result()

  def apply(
    header: String,
    headMark: String,
    messages: String,
    content: StringBuilder
  )
      : String =
  {
    val b = new StringBuilder()
    b ++= "<div id=\"page\">\n  <header id=\"pageHeader\">\n"
    b ++= headMark
    b ++= "\n"

    if (header != "") {
      b ++= "    <div id=\"header\">\n"
      b ++=  header
      b ++=  "\n    </div>\n"
    }

    b ++=  "  </header>\n\n  <div id=\"pageContent\">\n"
    if (messages != "") {
      b ++= "    <section id=\"messages\">\n"
      b ++= messages
      b ++= "\n    </section>\n"
    }

    b append content


    b ++= pageClose
    b.result()
  }


  /** Generates an HTML page.
    */
  def apply(
    header: String,
    headMark: String,
    messages: String,
    content: String
  )
      : String =
  {
    apply(
      header,
      headMark,
      messages,
      new StringBuilder(content)
    )
  }

}//PreparedEmptyPot



object PreparedEmptyPot {

  def apply(
    footer: String,
    footMark: String,
    box0: String,
    box1: String
  )
      : PreparedEmptyPot =
  {
    new PreparedEmptyPot(
      footer,
      footMark,
      box0,
      box1
    )
  }

  /** Generates a page with no footmark.
    */
  def apply(
    footer: String,
    box0: String,
    box1: String
  )
      : PreparedEmptyPot =
  {
    new PreparedEmptyPot(
      footer,
      "",
      box0,
      box1
    )
  }

  def testPreparedEmptyPot(): PreparedEmptyPot = PreparedEmptyPot(
    "footer",
    "footMark",
    "box0",
    "box1"
  )

}//PreparedEmptyPot
