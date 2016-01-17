package freight
package interface
package html
package pageBuilder

/*
 import freight.interface.html.HTML5
 val b = HTML5.testHTML5()
 */
/** Builds HTML5 webpages.
  *
  * Builds DOCTYPE HTML, a header HEAD, and encloses content in BODY tags.
  *
  * A STYLE tag is not included, as the tag is intended for inline
  * styles, an unlikely use here. The NOSCRIPT tag is also excluded
  * as, if used in a header, it requires a full complement of
  * attributes and internal tags.
  *
  * The only parameter which prints tags is `title` (the builder
  * always prints title tags). The others print their input
  * through. Supply nothing and they will print nothing.
*
* This class is good for pages with custom needs. For more general/sitewide page handling, see [[PreparedHTML5]]
  *
  * @param title a non-visible title read by browsers. Important
  *  for search engines. One element only. Not optional, and tags printed.
  * @param base rebases all relative links in the page. One element only.
  * @param links connects to external resources (stylesheets, data
  *  etc.). Many elements allowed.
  * @param metas data about the page (stylesheets, data etc.). Many
  *  elements allowed. Can take global attributes like language.
  * @param scripts connects to client-side scripts, mainly
  *  javascript. Many elements allowed. Can take global attributes
  *  such as `language`.
  */
//https://support.google.com/webmasters/answer/35624?rd=1
class HTML5(
  title: String,
  base: String,
  links: String,
  metas: String,
  scripts: String
)
{
  private val b = new StringBuilder()
  b ++= "<!DOCTYPE html>\n<html>\n<head>\n<title>"
  b ++= title
  b ++= "</title>\n"
  b ++= base
  b ++= links
  b ++= metas
  b ++= scripts
  b ++= "</head>\n<html>"

  def ++= (sb: StringBuilder) = b append sb
  def ++= (v: String) = b append v
  //def ++= (v: ObjectBuilder) = b ++= v.b

  def result(): String =  {
    b ++= "</html>"
    b.result()
  }


}//HTML5



object HTML5 {

  def apply(
    title: String,
    base: String,
    links: String,
    metas: String,
    scripts: String
  )
      : HTML5 =
  {
    new HTML5(
      title,
      base,
      links,
      metas,
      scripts
    )
  }

  /** Generates a page with title only.
    */
  def apply(
    title: String
  )
      : HTML5 =
  {
    new HTML5(
      title,
      "",
      "",
      "",
      ""
    )
  }

  def testHTML5(): HTML5 = HTML5("Test Page")

}//HTML5
