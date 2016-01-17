package freight.interface.html.servlet
package javaWraps

import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import idset.{HttpVersion}



/** Wraps a request to return url data.
  *  
  * The string returns are pre-trimmed if present, and return empty
  * string if not.
  */ 
//Limited, but an awful lot more consistent than Java.
//Does fragments work here?
//TODO: Url encoding - strip!
// see http://docs.oracle.com/javase/7/docs/api/java/net/URL.html
//TODO: Does the Request deal with fragment strings?
class URLDisassembler(req: HttpServletRequest)
//extends StringOps
{
  
  type StringMapSeq = Map[String, Seq[String]]

  /** Returns the path.
    *  
    * This is the section of a URL between the protocol/port and the query,
    * 
    * {{{http://dicey:80/[keyboards/cheap/battery]}}}
    * 
    * The value is not affected by servlet routing.
    * 
    * Note this is not decoded from possible ASCII encoding.
    */
  def fullPath : String = req.getRequestURI
  

  
  /** Returns the server protocol.
    *  
    * The version of the protocol the client used to send the request.
    * Typically this will be something like "HTTP/1.0" or "HTTP/1.1"
    * and may be used by the application to determine how to treat any
    * HTTP request headers.
    * 
    * {{{[http]:...}}}
    */
  def protocol: HttpVersion = req.getProtocol match {
    case "HTTP/1.1" => HttpVersion("HTTP", 1, 0)
    case "HTTP/1.0" => HttpVersion("HTTP", 1, 1)
  }
  
  /** Returns the host.
    *
    * This is the initial element of a URL, after the protocol,
    * 
    * {{{http://[dicey]}}}
    * 
    * From documentation, this is a search through headers,
    * serverName, and server IP (unverified?)
    */
  def host : String = {
    //Use header?
    req.getServerName
  }
  
  /** Returns the port the server received the request through.
    *  
    * Comes after the host,
    * 
    * {{{http://dicey:[80]}}}
    */
  def port : Int = req.getServerPort
  
  /** Returns the section of path used for servlet routing.
    *  
    * If used, part of the start of the path.
    * 
    * {{{http://dicey:80/[]}}}
    *  
    * This may be an empty string (as in the example), if
    * the application corresponds to the "root" of the server.
    * 
    * This is not significant to the URL scheme or the web.  It is
    * simply the first few element of the `path` (if used by the
    * Servlet).  However, it is significant for server code.
    *  
    * Mostly, the path()
    * is what is needed to be processed by code, not this.
    */
  def servletPathString: String = {
    Option(req.getServletPath) match {
      case None => ""
      case Some(str: String) => str.trim
    }
  }
  
  /** Returns elements of the path used for servlet routing.
    *  
    * If used, part of the start of the path. If the application is in
    * the server's virtual root,
    * 
    * {{{http://dicey:80/[]}}}
    *  
    * This may be an empty string (as in the example), if
    * the application corresponds to the "root" of the server.
    * 
    * This is not significant to the URL scheme or the web.  It is
    * simply the first few element of the `path` (if used by the
    * Servlet).  However, it is significant for server code.
    *  
    * Mostly, the path()
    * is what is needed to be processed by code, not this.
    */
  def servletPath: Seq[String] = {
    val p = Option(req.getServletPath) getOrElse ""
    p.split('/')
  }
  
  /** Returns the section of path used for servlet context.
    *  
    * If used, part of the start of the path. If the application is in
    * the root context,
    * 
    * {{{http://dicey:80/Optional(servletPath)/[]}}}
    * 
    * This may be an empty string (as in the example), if
    * the application corresponds to the "root" of the server.
    * 
    * This is not significant to the URL scheme or the web.
    * It is simply the first few element of the `path` (if used by the Servlet).
    * However, it is significant for server code.
    * 
    * The context path returned here is from the request. This may be
    * adapted by container server configuration. Use the return from
    * this method for external URL manipulation e.g. URL construction
    * for a redirect. For internal purposes, use the context path
    * gained from the method `contextPath` in [[Context]] e.g. for
    * finding resources in the server such as images/css files etc.
    *
    * Note this is not decoded from possible ASCII encoding.
    */
  def contextPathString : String = {
    Option(req.getContextPath) match {
      case None => ""
      case Some(str: String) => str.trim
    }
  }
  

  /** Returns the section of the path after servlet routing.
    *  
    * The tail part of the path, after server routing. If the first
    * element of the path is used for servlet routing,
    *      
    * {{{http://dicey:80/keyboards/[cheap/battery]}}}
    * 
    * The set of elements is not significant to the URL scheme or the
    * web.  It is a few end elements of the `path`. However, it is
    * significant for server code.
    * 
    * Mostly, the path()
    * is what needs to be processed by code.
    */
  def pathString : String = {
    Option(req.getPathInfo) match {
      case None => ""
      case Some(str: String) => str.trim
    }
  }
  
  /** Returns elements of the path after servlet routing.
    *  
    * The tail part of the path, after server routing. If the first
    * element of the path is used for servlet routing,
    *      
    * {{{http://dicey:80/keyboards/[cheap/battery]}}}
    * 
    * The set of elements is not significant to the URL scheme or the
    * web.  It is a few end elements of the `path`. However, it is
    * significant for server code.
    * 
    * Mostly, the path() needs to be processed by code.
    */
  def path : Seq[String] = {
    val p = Option(req.getPathInfo) getOrElse ""
    p.split('/')
  }
  
  /*
   def query : Seq[(String, String)] = {
   val q: Option[String] = Option(req.getQueryString)// getOrElse ""
   println("querystring: " + q)
   // No string would confuse the positioners, so explicitly return empty.
   if (q == None) Seq[(String, String)]()
   else {
   q.get.split('&').toSeq.map{e => 
   val pos = e.indexOf('=')
   (e.substring(0, pos).trim(), e.substring(pos + 1).trim())
   }
   }
   }
   
   */
  /** Returns the section of the query string.
    *  
    * Comes after the query delimiter '?',
    *  
    * {{{http://dicey:80/keyboards/cheap/battery?[&usr=56HUJ5K0&price=6]}}}
    * 
    * Parameters are contained in the query string or posted form
    * data.
    */
  def queryString: String = {
    Option(req.getQueryString) match {
      case None => ""
      case Some(str: String) => str.trim
    }
  }
  
  /** Returns the elements of the query string.
    *  
    * Comes after the query delimiter '?',
    *  
    * {{{http://dicey:80/keyboards/cheap/battery?[&usr=56HUJ5K0&price=6]}}}
    * 
    * Parameters are contained in the query string or posted form
    * data.
    */
  def query: StringMapSeq = {
    req.getParameterMap.asScala.toMap
      .transform { (k, v) => v: Seq[String] }
  }

  /*
   def fragment = {
   val q = Option(req.getQueryString) getOrElse ""
   val pos = q.lastIndexOf('#')
   if (pos != -1) q.substring(pos) else ""
   }
   */
  
  override def toString()
      : String =
  {
    val b = new StringBuilder()
    b ++= "URLDisassembler(protocol:"
    b ++= protocol.toString()
    b ++= ", fullPath:"
    b ++= fullPath
    b ++= ", host:"
    b ++= host
    b ++= ", port:"
    b ++= port.toString
    b ++= ", servletPathString:"
    b ++= servletPathString
    b ++= ", contextPathString:"
    b ++= contextPathString
    b ++= ", path:"
    b ++= pathString
    b ++= ", query:"
    b ++= queryString
    // b ++= ", fragment:"
    //b ++= fragment.toString
    b.result
  }
}
