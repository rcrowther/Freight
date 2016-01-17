package freight.interface.html.servlet
package javaWraps

import javax.servlet.http.HttpServletRequest
import java.net.URI
import scala.collection.{Map => CMap}
import scala.collection.immutable.DefaultMap
import scala.collection.JavaConverters._
import java.io.{InputStream, BufferedReader}
import freight.interface.html.servlet.idset.{HttpMethod, Http, Https}




/** Carries data about an Http request.
  *  
  * `Request` is a Scala representation of Java's
  * `javax.servlet.http.HttpServletRequest`. It wraps the object in a more
  * Scalalike API, and provides all the operations found in a map.
  * 
  * To look at the URL supplied, use the `urlExtractor` method.
  */
//TODO: Why use the map?
case class Request(req: HttpServletRequest)
{
  
  protected def attributes = req
  
  /*
   host	String
   port	int
   path	String
   query	String
   fragment	String
   */
  /*
   protocol (http)
   host (www.site.co.uk)
   path	String
   query	String
   fragment	String
   */
  type StringMapSeq = Map[String, Seq[String]]

  
  /** Returns the server protocol.
    *  
    * The version of the protocol the client used to send the request.
    * Typically this will be something like "HTTP/1.0" or "HTTP/1.1"
    * and may be used by the application to determine how to treat any
    * HTTP request headers.
    */
  /*
   def serverProtocol: HttpVersion = req.getProtocol match {
   case "HTTP/1.1" => HttpVersion("HTTP", 1, 0)
   case "HTTP/1.0" => HttpVersion("HTTP", 1, 1)
   }
   */
  
  //def uri = new URI(req.getRequestURL.toString)
  
  def url = req.getRequestURL.toString

  /** Returns the original Java request object
    *  
    * ...with hair shirt, if you need that in your life.
    */
  def request = req
  
  /** Returns the URL encoding scheme.
    *  
    * Http or Https, depending on the request URL.
    */
  /*
   def urlScheme: UrlScheme = req.getScheme match {
   case "http" => Http
   case "https" => Https
   }
   */
  /** Returns a class encapsulating the URL data for this request.
    * 
    * A convenience to tidy Connection code.
    */
  def urlExtractor: URLDisassembler = new URLDisassembler(req)
  
  /** Returns the HTTP request method.
    *  
    * e.g. GET or POST
    */
  def method = HttpMethod(req.getMethod)
  

  /** Returns a Map of the parameters of this request.
    *  
    * Parameters are contained in the query string or posted form
    * data.
    */
  /*
   def parameters: StringMapSeq = {
   req.getParameterMap.asScala.toMap
   .transform { (k, v) => v: Seq[String] }
   }
   */
  /** Returns a map of the headers.
    *  
    * Multiple header values are separated by a ',' character. The
    * keys of this map are case-insensitive.
    */
  object headers extends DefaultMap[String, String] {
    def get(name: String): Option[String] = Option(req.getHeader(name))

    private[html] def getMulti(key: String): Seq[String] =
      get(key).map(_.split(",").toSeq.map(_.trim)).getOrElse(Seq.empty)

    def iterator: Iterator[(String, String)] =
      req.getHeaderNames.asScala map { name => (name, req.getHeader(name)) }
  }

  /** Optionally returns the header as a string.
    * 
    */
  def header(name: String): Option[String] =
    Option(req.getHeader(name))

  /** Optionally returns the character encoding of the body.
    */
  def characterEncoding: Option[String] =
    Option(req.getCharacterEncoding)

  def characterEncoding_=(encoding: Option[String]) {
    req.setCharacterEncoding(encoding getOrElse null)
  }
  
  /** Optionally returns the content of the Content-Type header.
    */
  def contentType: Option[String] =
    Option(req.getContentType)

  /** Optionally returns the length, in bytes, of the body.
    */
  def contentLength: Option[Long] = req.getContentLength match {
    case -1 => None
    case length => Some(length)
  }


  /** Optionally returns the HTTP referrer.
    *
    * @return the `Referer` header, or None if not set
    */
  def referrer: Option[String] = req.getHeader("Referer") match {
    case s: String => Some(s)
    case null => None
  }
  
  /** Returns true if the request is an AJAX request.
    */
  def isAjax: Boolean = req.getHeader("X-Requested-With") != null

  /** Returns true if the request's method is not "safe" per RFC 2616.
    */
  def isWrite: Boolean = !HttpMethod(req.getMethod).isSafe

  /** Returns a map of cookie names to lists of their values.
    *  
    *  The default value of the map is the empty sequence.
    */
  def cookies: StringMapSeq = {
    val rr = Option(req.getCookies).getOrElse(Array()).toSeq.
      groupBy { _.getName }.
      transform { case(k, v) => v map { _.getValue }}.
      withDefaultValue(Seq.empty)
    rr
  }

  /** Returns a map of cookie names to values.
    * 
    * If multiple values are present for a given cookie, the value is
    * the first cookie of that name.
    */
  /*
   def cookies: CMap[String, String] = new MultiMapHeadView[String, String] {
   protected def multiMap = multiCookies
   }
   */
  //protected[scalatra] def attributes = req

  /** Returns an InputStream of the request body data.
    * 
    * The caller should not close this stream. This stream is not
    * rewindable.
    */
  def inputStream: InputStream = req.getInputStream

  /** Returns a BufferedReader of the request body data.
    * 
    * The caller should not close this stream. This stream is not
    * rewindable.
    */
  def reader: BufferedReader = req.getReader

  def readAll(): String = {
    val r = reader
    val b = new StringBuilder()
    var line = r.readLine()
    while (line != null)
    {
      b ++= line
      line = r.readLine()
    }
    b.result()
  }
  
  /** Returns the remote address the client is connected from.
    *  
    * This takes the load balancing header X-Forwarded-For into
    * account
    * @return the client ip address
    */
  def remoteAddress : Option[String] = header("X-FORWARDED-FOR") match {
    case Some(x) => Some(req.getRemoteAddr)
    case _ => None
  }


  def locale = req.getLocale

  def locales = req.getLocales

  
  /** Returns the current session, or creates a new one.
    */
  def session(): Session = Session(req.getSession())

  /** Optionally returns the current session.
    */
  def sessionOption: Option[Session] = {
    val s = req.getSession(false)
    if (s != null) Some(Session(s))
    else None
  }
  
  
  override def toString()
      : String =
  {
    val b = new StringBuilder("Request(contentType:")
    b append contentType
    b ++= "\", characterEncoding:"
    b append characterEncoding
    b ++= ", method:"
    b append method
    b ++= ", cookies:"
    b append cookies
    //b ++= ", parameters:"
    //b append parameters.toString
    b += ')'
    b.result
  }
  
}//Request

