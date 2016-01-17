package freight
package interface
package html
package pageBuilder

/*
import freight.interface.html.HTML4
val b = HTML4.testHTML4()
*/
/** Builds a page for viewing HTML.
*
* Wraps in a header and footer.
*/
class HTML4(
  language: String,
  textDirection  : String,
  headData: String,
  headTitle: String,
  styles: String,
  scripts: String,
  headProfile: String,
  doctype: String
)
{
  private val b = new StringBuilder()

  b ++= doctype
  b ++= """<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="""
  b ++= language
  b ++= """ version="XHTML+RDFa 1.0" textDirection="""
  b ++= textDirection
  b ++= ">\n<head profile=\""
  b ++= headProfile
  b ++= "\">\n"
  b ++= headData
  b ++= "<title>"
  b ++= headTitle
  b ++= "</title>\n"
  b ++= styles
  b ++= scripts
  b ++= "</head>\n"

  def ++= (sb: StringBuilder) = b append sb
  def ++= (v: String) = b append v
  //def ++= (v: ObjectBuilder) = b ++= v.b

  def result(): String =  {
    b ++= "</html>"
    b.result()
  }


}//HTML4



object HTML4 {
  def apply(
    language: String,
    textDirection  : String,
    headData: String,
    headTitle: String,
    styles: String,
    scripts: String,
    headProfile: String,
    doctype: String
  )
      : HTML4 =
  {
    new HTML4(
      language,
      textDirection,
      headData,
      headTitle,
      styles,
      scripts,
      headProfile,
      doctype
    )
  }

  /** Generates a page with stock XHTML doctype.
    */
  def apply(
    language: String,
    textDirection  : String,
    headData: String,
    headTitle: String,
    styles: String,
    scripts: String,
    headProfile: String
  )
      : HTML4 =
  {
    new HTML4(
      language,
      textDirection,
      headData,
      headTitle,
      styles,
      scripts,
      headProfile,
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">\n"
    )
  }

def testHTML4(): HTML4 = HTML4("en", "lr","","","","","")

}//HTML4
