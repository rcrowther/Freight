package freight
package interface
package html
package pageBuilder

/*
 import freight.interface.html.PreparedHTML5
 val b = PreparedHTML5.testPreparedHTML5()
 b("Article | Dynamos", "EN", "On dynamos", "The revolution turned")
 */
/** Builds HTML5 webpages.
  *
  * Builds DOCTYPE HTML, a header HEAD, and encloses content in BODY tags. Much of the
  * data is preprepared. The data in the class is immutable, so calls
  * on the methods can be multi-threaded. Use this class to define the
  * working page for a site, then forget about the issue.
  *
  * In this builder, the META tags are defined. A STYLE tag is not included, as the tag is
  * intended for inline styles, an unlikely use here. The NOSCRIPT tag
  * is also excluded as, if used in a header, it requires a full
  * complement of attributes and internal tags.
  *
  * HTTP-EQUIV is not set - do not set it using this builder (which
  * uses incompatible META tags). No keywords META tag, as "it's
  * mostly useless nowadays". No "author" either (use some
  * microformat).
  *
  * The only parameter which prints tags is `title` (the builder
  * always prints title tags). The others print their input
  * through. Supply nothing and they will print nothing.
  *
  * See also [[PageBuilderHTML5]].
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
// https://support.google.com/webmasters/answer/35624?rd=1
class PreparedHTML5(
  base: String,
  links: String,
  scripts: String,
  charset: String
)
{

  /*
   "<meta charset="UTF-8" lang=""></meta>"
   "<meta name="description" content="Free Web tutorials"></meta>"
   */

  val baseLinks = "</title>\n" + base + links
  val headClose = scripts + "</head>\n\n<body>\n"

  /** Generates an HTML5 page.
    */
  def apply(
    title: String,
    language: String,
    description: String,
    content: StringBuilder
  )
      : String =
  {
    val b = new StringBuilder()
    b ++= "<!DOCTYPE html>\n<html>\n<head>\n<title>"
    b ++= title
    b ++= baseLinks
    b ++= "<meta charset=\""
    b ++= charset
    b ++= "\" lang=\""
    b ++= language
    b ++= "\"></meta>\n"
    b ++= "<meta name=\"description\" content=\""
    b ++= description
    b ++= "\"></meta>\n"
    b ++= headClose
    b append content
    b ++= "\n</body>\n</html>"
    b.result()
  }

  /** Generates an HTML5 page.
    */
  def apply(
    title: String,
    language: String,
    description: String,
    content: String
  )
      : String =
  {
    apply(
      title,
      language,
      description,
      new StringBuilder(content)
    )
  }

  //def ++= (v: HTMLBuilder) = b ++= v.b


}//PreparedHTML5



object PreparedHTML5 {

  /** Generates a UTF-8 HTML5 page.
    */
  def apply(
    base: String,
    links: String,
    scripts: String
  )
      : PreparedHTML5 =
  {
    new PreparedHTML5(
      base,
      links,
      scripts,
      "UTF-8"
    )
  }

  /** Generates a UTF-8 HTML5 page with no external links.
    */
  def apply()
      : PreparedHTML5 =
  {
    new PreparedHTML5(
      "",
      "",
      "",
      "UTF-8"
    )
  }

  /** Generates a UTF-8 HTML5 page.
    */
  def apply(
    base: String,
    links: String,
    scripts: String,
    charset: String
  )
      : PreparedHTML5 =
  {
    new PreparedHTML5(
      base,
      links,
      scripts,
      charset
    )
  }

  def testPreparedHTML5(): PreparedHTML5 = PreparedHTML5()

}//PreparedHTML5
