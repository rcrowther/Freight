package freight.interface.html
package servlet

import javaWraps.{Request, Response}
import util.MessageBuilder


/** Stash messages for a following request.
  * 
  * The messages last only until the next request. They are deleted by the trivial method of allowing one access.
  */
//TODO: Dont call it flash support
trait FlashMessageSupport
{  
  def wrequest: Request
  def wresponse: Response

  /** Store a message builder for a following request.
    *  
    * Override to implement custom session retriever, or sanity checks if session is still active
    * @param f
    */
  def flash(f: MessageBuilder) {
    try {
      // Save flashMap to Session.
      // A session can disappear during a request, so catch the exception.
      // This also ensures a session
      val s = wrequest.session()
      s += ("freight_co_uk_flash_messages" -> f)
    } catch {
      case e: Throwable =>
    }
  }

  /** Optionally retrieve a message builder.
    */
  def flashOption()
      : Option[MessageBuilder] =
  {
    val res = wrequest.sessionOption match {
      case Some(s) => {
        s.get("freight_co_uk_flash_messages") match {
          case Some(f) => Some(f.asInstanceOf[MessageBuilder])
          case None => None
        }
      }
      case None => None
    }
//TODO: Null not allowed on session?
    flash(null)
    res
  }

  def flashMkHtml
      : String =
  {
    flashOption() match {
      case Some(f) => {
        f.addHtml(new StringBuilder).toString
      }
      case None => ""
    }
  }

}//FlashMessageSupport
