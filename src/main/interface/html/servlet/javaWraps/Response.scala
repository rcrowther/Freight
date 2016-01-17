package freight.interface.html.servlet
package javaWraps

import java.io.{OutputStream, PrintWriter}
import freight.interface.html.servlet.idset.{ResponseStatus}
import javax.servlet.http.{HttpServletResponse, HttpServletResponseWrapper, Cookie => ServletCookie}
import scala.collection.JavaConverters._
import scala.collection.mutable.Map
//import freight.common.util.String

/** Carries data about an Http response.
  * 
  * `Response` is a Scala representation of Java's
  * `javax.servlet.http.HttpServletResponse`. It wraps the object in a more
  * Scalalike API.
  */
case class Response(res: HttpServletResponse) {

  /** Returns the render status of this response.
    * This is the one piece of data not derived from the ServletResponse inheritance.
    * It is set to true by various Connection cases, such as if an exception is rendered,
    * or by the user, if they do a custom render. 
    */
  var rendered = false
  
  /** Get the status of the response.
    *
    * Note: the servlet API doesn't remember the reason.  If a custom
    * reason was set, it will be returned incorrectly here,
    */
  def status: ResponseStatus = ResponseStatus(res.getStatus)

    /** Returns the Java response object
   *  
   * ...out of the cave...
   */
  def response = res

  /** Set the status of the response.
*
* Codes can be used from [[idset.ResponseStatus]]. These should only be used for valid responses, use `error` to set errors.
    */
  def status_=(code: Int) {
    res.setStatus(code)
  }

  def locale = res.getLocale

  object headers extends Map[String, String] {
    def get(key: String): Option[String] =
      res.getHeaders(key) match {
        case xs if xs.isEmpty => None
        case xs => Some(xs.asScala mkString ",")
      }

    def iterator: Iterator[(String, String)] =
      for (name <- res.getHeaderNames.asScala.iterator)
      yield (name, res.getHeaders(name).asScala mkString ", ")

    def +=(kv: (String, String)): this.type = {
      res.setHeader(kv._1, kv._2)
      this
    }

    def -=(key: String): this.type = {
      res.setHeader(key, "")
      this
    }
  }

  def addCookie(cookie: Cookie) {
    import cookie._

    val sCookie = new ServletCookie(name, value)
    if (!isBlank(options.domain)) sCookie.setDomain(options.domain)
    if(!isBlank(options.path)) sCookie.setPath(options.path)
    sCookie.setMaxAge(options.maxAge)
    sCookie.setSecure(options.secure)
    if(!isBlank(options.comment)) sCookie.setComment(options.comment)
    sCookie.setHttpOnly(options.httpOnly)
    sCookie.setVersion(options.version)
    res.addCookie(sCookie)
  }

  def characterEncoding: Option[String] =
    Option(res.getCharacterEncoding)

  def characterEncoding_=(encoding: Option[String]) {
    res.setCharacterEncoding(encoding getOrElse null)
  }
  
  def contentType: Option[String] =
    Option(res.getContentType)

  def contentType_=(contentType: String) {
    res.setContentType(contentType)
  }
  
  /** Set a redirect on the response.
    */
  def redirect(uri: String) {
    res.sendRedirect(uri)
  }
  
  /** Set an error on the response.
    */
  def error(errorCode: Int, msg: String) {
    res.sendError(errorCode, msg)
  }

    /** Set an error on the response.
    */
  def error(statusLine: ResponseStatus) {
    res.sendError(statusLine.code, statusLine.message)
  }

  def outputStream: OutputStream =
    res.getOutputStream

  def writer: PrintWriter =
    res.getWriter

  /** Finish the response by flushing and closing buffers.
    *  i.e. the OutputStream.
    */
  def end() {
    res.flushBuffer()
    res.getOutputStream.close()
  }
  
  override def toString()
      : String =
  {
    val b = new StringBuilder("Response(status:")
    b append status
    b ++= ", contentType:"
    b append contentType
    b ++= "\", characterEncoding:"
    b append characterEncoding
    /// No way to get a cookie, suppose data is written not stored?
    b ++= ", header:"
    b ++= headers.mkString("Headers(", ", ", ")")
    b += ')'
    b.result
  }
  
}//Response
