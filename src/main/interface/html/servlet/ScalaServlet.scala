package freight
package interface
package html
package servlet

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletConfig
import idset.{HttpMethod, Options, Get, Post, Put, Delete, Trace}
import scala.util.matching.Regex
import java.io.{InputStream, BufferedInputStream}
import idset.ResponseStatus
import generic.RouteItem
import javax.servlet.ServletContext
import javaWraps.{Request, Response, Context}
import javaWraps.URLDisassembler
import util.URUtils

import scala.util.matching.Regex
import java.io.{OutputStream, PrintWriter}

/** Runs a Servlet with Sinatra-like conveniences.
  *  
  * Route execution and notFound defaulting are realized here.
  */
//From ServletBase
//TODO:
//halt?
// fix character encoding
// errors are not checked for mime error?
// fix buffer sizing
// test handle Array[Byte]
// test errors in filters
//Wheres the appname in request?
// Split servlet specifics from this class!
// Alternate routematchers?

//TODO: Content type presets
//TODO: Cast an eye at Scalatra's streaming execution
//TODO: Can we get rid of renders using apply?
//TODO: Need the site base URL something bad
// TODO: More Freight style method-passing renderers.
// TODO: Scalatra parameter extractor is cool - put it as method as compromise?
// TODO: Beginning to think the "find resource as default" is a washout here?
trait ScalaServlet
    extends DynamicParameters
{
  /**
    * Registered routes.
    */
  protected lazy val routes = new ConnectionMap[HttpMethod]()

  /** Set Development mode.
    *  This mode has two actions:
    *  1. Exceptions are rendered through the response i.e. to the browser, via custom exception pages.
    *  2. Routes which do not match render a list of routes to the browser (if the notFound() method is not overridden).
    * Set to ''true'' for development. Should be turned off for
    * production.
    */
  val developmentMode = true

  /** Buffer size for streamed output. 10KB capacity. Can be overridden
    * for different capacity.
    *
    * The default size set here is a general recommendation.
    */
  protected val DEFAULT_BUFFER_SIZE: Int = 10240
  
  /** The character encoding for requests and responses.
    */
  protected val defaultCharacterEncoding = "UTF-8"
  
  /** Returns the request path to be matched by routers.
    *  
    * The default implementation is for `path mapped` servlets (i.e., servlet
    * mapping ends in `&#47;*`).  The route should match everything matched by
    * the `&#47;*`.  In the event that the request URI equals the servlet path
    * with no trailing slash (e.g., mapping = `/admin&#47;*`, request URI =
    * '/admin'), a '/' is returned.
    *
    * Other servlet mappings likely want to return wrequest.servletPathString.
    * 
    * The method does some hunting about, stripping context and servlet path sections.
    * It also decodes encoded URLS.
    * For unusual cases, this method can be overridden.
    */
  protected def matchPath : String = {
    val reqUrl =  wrequest.urlExtractor
    reqUrl.fullPath match {
      case requestURI: String =>
        var uri = requestURI
        
        // Get tail without context info
        val cxtPath = reqUrl.contextPathString
        if (cxtPath.nonEmpty)  {
          uri = uri.substring(cxtPath.length)
        }
        
        // Get tail without servlet path info (this leaves something like getPathInfo)
        val srvPath = reqUrl.servletPathString
        if (srvPath.nonEmpty) {
          uri = uri.substring(srvPath.length)
        }
        
        if (uri.isEmpty) {
          uri = "/"
        } else {
          val pos = uri.indexOf(';')
          if (pos >= 0) uri = uri.substring(0, pos)
        }
        URUtils.urlDecode(uri)
      case null => "/"
    }
  }
  
  
  def configure(config: ServletConfig) {
    //conn.initialize()
    //println("Servlet initialize called!")
  }

  /** Handles a Servlet request.
    *
    * Takes parameters in line with Servlet Spec. It wraps the
    * request and response, then executes on the routes table, binding
    * in the wrapped request and response.
    */
  // ScalatraBase
  def handle(request: HttpServletRequest, response: HttpServletResponse) {
    if (request.getCharacterEncoding == null) {
      request.setCharacterEncoding(defaultCharacterEncoding)
      //super.handle(request, response)
    }
    response.setCharacterEncoding(defaultCharacterEncoding)
    response.setContentType("text/html")
    
    val wrequest = Request(request)
    //println("wrequest: " + wrequest)
    val wresponse = Response(response)
    //println(wresponse)

    // println("pathString: " + wrequest.url.pathString)
    //println(routes.beforeToString())
    //println(routes)
    withRequestResponse(wrequest, wresponse) {
      execute()
    }

  }
  
  /*
   def executeFilter(filters: Seq[() => Unit])
   : Option[Exception] =
   {
   try {
   filters.map(callback => callback())
   None
   }
   catch {
   case e: Exception => Some(e)
   }
   }
   */
  
  
  ///////////
  // Config //
  ///////////
  //TODO: Needs cleaning. Is it doing the job?

  /**
    * The configuration, typically a ServletConfig or FilterConfig.
    */
  private[this] var config: ServletConfig = _

  /**
    * Initializes the kernel.  Used to provide context that is unavailable
    * when the instance is constructed, for example the servlet lifecycle.
    * Should set the `config` variable to the parameter.
    *
    * @param config the configuration.
    */
  protected def initialize(config: ServletConfig) { this.config = config }
  
  /**
    * Gets an init paramter from the config.
    *
    * @param name the name of the key
    *
    * @return an option containing the value of the parameter if defined, or
    * `None` if the parameter is not set.
    */
  def initParameter(name: String)
      : Option[String] =
  {
    val p = config.getInitParameter(name)
    if (p == null) None
    else Some(p)
  }

  /** Returns the servlet context.
    */
  def context: Context = Context(config.getServletContext())
  
  
  
  //////////////////
  // URL Utils //
  //////////////////
  
  def urlExtractor: URLDisassembler = wrequest.urlExtractor

  /*
   protected def needsHttps =
   allCatch.withApply(_ => false) {
   servletContext.getInitParameter(ForceHttpsKey).blankOption.map(_.toBoolean) getOrElse false
   }
   * 
   */
  
  
  /**
    * The base path for URL generation.
    * 
    * Separated out in case needs overriding. Changing this value only has an effect on generated URLs if 
    * addContextPath and addServletPath are specified true, but they are unless switched off.
    * 
    * The return is used for Url construction, mostly in redirects.
    */
  protected def routeBasePath(reqUrl: URLDisassembler)
      : String =
  {
    //if (reqUrl == null)
    //throw new IllegalStateException("routeBasePath requires an active request to determine the servlet path")
    reqUrl.contextPathString + reqUrl.servletPathString
  }

  
  /** Ensures a start slash, and no end slash, on a URL.
    * 
    */
  private[this] def ensureSlashes(url: String) = {
    val p = if (url.startsWith("/")) url else "/" + url
    if (p.endsWith("/")) p.substring(0, p.length -1) else p
  }
  
  private[this] def stripServletPath(reqUrl: URLDisassembler, path: String)
      : String =
  {
    val sp = ensureSlashes(reqUrl.servletPathString)
    val np = if (path.startsWith(sp + "/")) path.substring(sp.length) else path
    ensureSlashes(np)
  }
  
  private[this] def stripContextPath(reqUrl: URLDisassembler, path: String)
      : String =
  {
    val cp = ensureSlashes(reqUrl.contextPathString)
    val np = if (path.startsWith(cp + "/")) path.substring(cp.length) else path
    ensureSlashes(np)
  }
  
  
  private[this] def stripContextPaths(reqUrl: URLDisassembler, path: String)
      : String =
  {
    stripContextPath(reqUrl, path)
    stripServletPath(reqUrl, path)
  }

  /** Provides a context-sensitive url string.
    *
    * Returns a context-relative, session-aware URL for a path and specified
    * parameters.
    * The result is run through `response.encodeURL` for a session
    * ID, if necessary.
    *
    * @param path the base path.  If a path begins with '/', then the context
    * path will be prepended to the result
    *
    * @param params params, to be appended in the form of a query string
    *
    * @return the path plus the query string, if any.  The path is run through
    * `response.encodeURL` to add any necessary session tracking parameters.
    */
  def url(
    path: String,
    params: Iterable[(String, Any)],
    addContextPath: Boolean,
    addServletPath: Boolean,
    absolutize: Boolean
  )
      : String =
  {
    val reqUrl = wrequest.urlExtractor
    val newPath = path match {
      case x if x.startsWith("/") && addContextPath && addServletPath =>
        // Add the whole route base to whatever is stripped
        ensureSlashes(routeBasePath(reqUrl)) + stripContextPaths(reqUrl, ensureSlashes(path))
      case x if x.startsWith("/") && addContextPath =>
        // Add the context to whatever has been stripped
        ensureSlashes(reqUrl.contextPathString) + stripContextPath(reqUrl, ensureSlashes(path))
      case x if x.startsWith("/") && addServletPath =>
        if(reqUrl.servletPathString.isEmpty) "/"
        else ensureSlashes(reqUrl.servletPathString) + stripServletPath(reqUrl, ensureSlashes(path))
      case _ if absolutize =>
        //Strip it of context, that's all
        stripContextPaths(reqUrl, ensureSlashes(path))
      case _ => path
    }

    val pairs = params map {
      case (key, None) => URUtils.urlEncode(key) + "="
      case (key, Some(value)) => URUtils.urlEncode(key) + "=" + URUtils.urlEncode(value.toString)
      case(key, value) => URUtils.urlEncode(key) + "=" + URUtils.urlEncode(value.toString)
    }
    val queryString = if (pairs.isEmpty) "" else pairs.mkString("?", "&", "")

    //addSessionId(newPath)// + queryString)
    wresponse.response.encodeURL(newPath + queryString)
  }

  
  def url(path: String, params: Iterable[(String, Any)])
      : String =
  {
    url(path, params, true, true, true)
  }

  def url(path: String)
      : String =
  {
    url(path, Iterable.empty, true, true, true)
  }
  
  
  /** Was this request intended to be secure?
    *  
    * Checks headers for the load balancer version, as well as
    * servlet request data.
    */
  private def isHttps = {
    // also
    val hO = wrequest.header("X-Forwarded-Proto")
    wrequest.request.isSecure || (hO.isDefined && hO.forall(_ equalsIgnoreCase "HTTPS"))
  }
  
  private[this] def authority = {
    val p = wrequest.request.getServerPort
    val h = wrequest.request.getServerName
    
    if (p == 80 || p == 443 ) h else h + ":" + p.toString
  }
  
  /** Builds a URL from the given relative path.
    *  
    * Adds the protocol, and takes into account the port configuration, https/htttp
    * and the servlet context.
    *
    * @param path a relative path
    *
    * @return the full URL
    */
  def protocolUrl(
    path: String,
    params: Iterable[(String, Any)],
    assureContextPath: Boolean,
    assureServletPath: Boolean
  )
      : String =
  {
    if (path.startsWith("http")) path
    else {
      val p = url(path, params, assureContextPath, assureServletPath, true)
      val protocol = "%s://%s".format(
        //TODO: Put needsHttps back.
        // if (needsHttps || isHttps) "https" else "http",
        if (isHttps) "https" else "http",
        authority
      )
      protocol + ensureSlashes(p)
    }
  }
  
  def protocolUrl(
    path: String,
    params: Iterable[(String, Any)]
  )
  {
    protocolUrl(path, params, true, true)
    
  }
  
  def protocolUrl(
    path: String
  )
  {
    protocolUrl(path, Iterable.empty, true, true)
    
  }
  


  ///////////////////
  // Private Utils //
  ///////////////////
  
  /** Render route alternatives as HTML.
    * Only renders the routes associated with the HttpMethod in the request.
    * 
    * Used for error reporting.
    */
  private def routesToHtml()
      : String =
  {
    routes.routesAddPrettyString(
      new StringBuilder,
      wrequest.method,
      "<ul><li>",
      "</li><li>",
      "</li></ul>"
    ).toString
  }
  
  /** Returns a message formatted with request detail.
    *
    * '''Note:''' routes can't be printed out.
    * @param actionMsg is appended to the preformatted details. Should be an action "failed to..." 
    */
  private def requestMessage(
    actionMsg: String
  )
      : String =
  {
    //TODO: Not strictly true, as not enforced?
    // TODO: surely get the pathstring for this area?
val pathStr = wrequest.urlExtractor.pathString
    val pathInfo = if(pathStr.isEmpty) "/" else pathStr

    // NB: is empty if web.xml is '/*'
    //TODO: May be in Context, someplace
    val sp = wrequest.urlExtractor.servletPathString
    val servletName = if(!sp.isEmpty) sp else "&lt;none reported&gt;"
    "Request \"%s %s\" on servlet \"%s\" %s".format(
      wrequest.method,
      pathInfo,
      servletName,
      actionMsg
    )

  }


  /** Generates a custom HTML error page.
    *  @param code the integer code used to define the status of the return 
    *  @param reason a string suffix to the title. If empty, will be expanded to a generic reason.
    *  @param detail a body for the page. Can be empty.
    */   
  private def mkHtmlErrorPage(code: Int, reason: String, detail: String)
      : String =
  {
    val b = new StringBuilder
    b ++= "<html><head><style><!--H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:black;font-size:22px;padding:8px 0 8px 1em;} .info {text-shadow:1px 1px red;} BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}A {color : black;}A.name {color : black;}HR {color : #525D76;}--></style></head><body>"
    b ++= "<h1><span class=\"info\">HTTP Status "
    b ++= code.toString
    b ++= " </span> - "
    val r  = if (reason.isEmpty) ResponseStatus.ReasonMap(code) else reason
    b ++= r
    if (!detail.isEmpty) b += ':'
    b ++= "</h1>"
    if (!detail.isEmpty) {
      b ++= "<p>"
      b ++= detail
      b ++= "</p>"
    }
    b ++= "</body></html>"
    b.toString
  }
  
  def mkHtmlStacktrace(st: String)
      : String =
  {
    val re : Regex =  "\n".r
    re.replaceAllIn(st, "<br>")
  }
  

  ////////////
  // Errors //
  ////////////
  
  /** Set an error on the response. Internal use.
    * If the `developmentMode` value is true this will,
    * - Boost the title with path details
    * - Print messages.
    * Otherwise this uses the servlet setError() method with a generic reason.
    * @param error takes an HttpServletResponse code.
    * @param reason the title to return with the error. Placed after status and request data. Will only be printed in development mode.
    * @param details the body message to return with the error. Will only be printed in development mode.
    */
  private def _setError(error: Int, reason: String, details: String)
  {
    val b = new StringBuilder

    wresponse.contentType = "text/html; charset=UTF-8"

    if (developmentMode){
      // build an informative error body for the response
      render(mkHtmlErrorPage(error, reason, details))
    }
    else {
      // set status and build body, excluding any body (e.g. exception) data
      //    val rs = ResponseStatus(error)
      //      wresponse.status = rs
      //rs.errorAddHtml(b)
      wresponse.error(error, ResponseStatus.reason(error))
    }
    //render(b.toString)

    wresponse.rendered = true
  }
  

  private def _setErrorFromException(error: Int, reason: String, details: String)
  {
    _setError(error, reason, mkHtmlStacktrace(details))
  }
  


  ///////////////
  // Renderers //
  ///////////////
  
  def stockError () {
    wresponse.error(HttpServletResponse.SC_NOT_FOUND, "Woozy")
  }
  
  def authError() {
    renderError(HttpServletResponse.SC_UNAUTHORIZED)
  }

  /** Renders an error message to the response.
    *
    * Will cause subsequent processing to cease.
    * Returns the server error pages. Unlike the _setError() method, the reason is printed. 
    */
  def renderError(error: Int, reason: String)
  {
    wresponse.error(error, reason)
    wresponse.rendered = true
  }
  
  /** Renders an error to the response.
*
    *  The reason for the error will be present, taken from a map of generic reasons to the error code e.g. 404 -> "Page not found".
    * Will not cause subsequent processing to cease (is not an exceptional halt() type method).
    * Returns a custom page, not throwing to the server. 
    */
  def renderError(error: Int)
  {
    wresponse.error(error, ResponseStatus.reason(error))
    wresponse.rendered = true
  }

  /** Renders a 201 to the response.
*
*  201 Created. Indicates a create action completed on the URL, and not delayed, but no message return. Appropriate for returns from POST and PUT.
*
* Should have a Location header field. This is assumed to be the requesting URL.
*
*/
    def render201()
  {
    wresponse.status = 201
log("****response URL!" + url(wrequest.url))
    wresponse.headers += ("Location" -> url(wrequest.url))
    wresponse.rendered = true
  }


  /** Renders a 204 to the response.
*
*  204 No Content. Indicates an action completed, and not delayed, but no message return. Appropriate for returns from POST, PUT, DELETE
*
* Should have no body at all.
*/
    def render204()
  {
    wresponse.status = 204
    wresponse.rendered = true
  }

  /** Renders a 205 to the response.
*
*   205 Reset Content. Tells a user agent to reset it's displays. Frankly, this is unsupported in any major browser, but may have custome built uses yet. Appropriate for returns from POST, PUT, DELETE
*
* Should have no body.
*/
    def render205()
  {
    wresponse.status = 205
    wresponse.rendered = true
  }



  /** Renders a 304 to the response.
*
*   304 Not Modified. Very special, indicates a GET has authenticated (if necessary) but failed on a known condition (not due to error). Appropriate for returns from (conditional) GET.
*
* Should have no body, but has some conditions such as including a date and cacheing information.
*/
    def render304()
  {
    wresponse.status = 304
    wresponse.rendered = true
  }

  /** Renders a BufferedInputStream to the response.
    *  On exceptions, the response is adapted.
    */
  def renderBufferedInputStream(
    data: BufferedInputStream
  )
  {
    import java.io.BufferedOutputStream
    
    val bos : BufferedOutputStream = null
    try {
      //wresponse.writer
      //          println("error 2")

      //val bos = new BufferedOutputStream(wresponse.outputStream, DEFAULT_BUFFER_SIZE)
      val bos = new BufferedOutputStream(wresponse.outputStream)

      // Write data
      var c = data.read()
      while(c != -1)
      {
        bos.write(c);
        c = data.read()
      }
      wresponse.rendered = true
    }
    catch {
      case e: Exception => Some(e)
        // 500 for big exceptions
        _setErrorFromException(500, "rendering BufferedInputStream", e.getStackTraceString)
    }
    finally {
      // Close stream
      if (bos != null) {
        try {
          bos.close();
        }
        catch {
          case e: Exception => Some(e)
            // 500 for big exceptions
            _setErrorFromException(500, "closing OutputStream", e.getStackTraceString)
        }
      }
    }
  }
  
  def renderStream(
    f: (OutputStream) => Boolean,
contentType: ContentType
  )
  {
    println("apply called")
/*
    f(wresponse.outputStream)
wresponse.rendered = true
*/

    if(f(wresponse.outputStream)) {
wresponse.contentType = contentType.mime
wresponse.rendered = true
    println("apply sucessful")
}
else {
        // 500 for big exceptions
        _setErrorFromException(500, "rendering BufferedInputStream", "failure to read stream reported")
    }

  }
  

  /** Renders a string to the response.
    * This happens by default, but can be made explicit with this method. 
    */
  def renderString(str: String) {
    wresponse.writer.println(str)
    wresponse.rendered = true
  }
  
  /** Renders an InputStream to the response.
    * This method constructs a BufferedInputStream to copy data in.
    * On exceptions, the response is adapted.
    */
  def renderInputStream(
    data: InputStream
  )
  {
    import java.io.BufferedInputStream
    val bin = new BufferedInputStream(data)
    renderBufferedInputStream(bin)
  }
  
  /** Renders a byte array to the response.
    *  On exceptions, the response is adapted.
    */
  def renderByteArray(
    data: Array[Byte]
  )
  {
    import java.io.BufferedOutputStream
    
    val bos : BufferedOutputStream = null
    try {
      val bos = new BufferedOutputStream(wresponse.outputStream, DEFAULT_BUFFER_SIZE)
      // Open streams.
      bos.write(data)
      wresponse.rendered = true
    }
    catch {
      case e: Exception => Some(e)

        //System.out.println("except1:" + e);
        //e.printStackTrace();

        // 500 for big exceptions
        _setErrorFromException(500, "rendering ByteArray", e.getStackTraceString)
    }
    finally {
      // Close stream
      if (bos != null) {
        try {
          bos.close();
        }
        catch {
          case e: Exception => Some(e)
            // 500 for big exceptions
            _setErrorFromException(500, "closing OutputStream", e.getStackTraceString)
        }
      }
    }
  }
  
  /** Renders data to the response.
*
    * Adapts the method of rendering depending on the content type in
    * the response (not altogether typed due to erasure, but works).
    */
  //TODO: The charset insert is a big pain in the butt.
  private def render(data: Any)
  {
    /*
     // Assert a content type on the response, text as default.
     val cth : String = wresponse.contentType match {
     case Some(cth) => cth
     case None => {
     val headerCT = "text/html" + defaultCharacterEncoding
     wresponse.contentType = Some(headerCT)
     headerCT
     }
     }
     
     // Split the content type from any encoding.
     val commaPos = cth.indexOf(';')
     val contentType =  if(commaPos == -1) cth else cth.substring(0, commaPos)
     println("contentype:" +contentType)
     // Render to the response dependent on content type.
     */
    data match {
      case _ : Unit => //  The callback handled the response (which callback? R.C.)
      case x: Any => {
        wresponse.writer.println(x.toString)
      }
    }
  }
  

  /** Execute matching callbacks, then render the result.
    *  Runs:
    * - before filters
    * - a callback with matching route
    * - After filters
    * - the renderer
    */
  private def execute()
  {
    val path = matchPath

    var data: Any = null
    try {
      routes.filterBefore(path).foreach(ri => ri.callback())
    }
    catch {
      case e: Exception => Some(e)
        _setErrorFromException(500, "Exception in beforeFilter", e.getStackTraceString)
    }

    if (!wresponse.rendered) {
      try {
        routes.find(wrequest.method, path) match {
          case Some(ri) =>
            if (ri.accessCallback != None && !ri.accessCallback.get()) {
              notAuthenticated(ri)
            }
            else {
              data = ri.callback()
            }
            
          case _ =>
            data = notFound()
        }
      }
      catch {
        case e: Exception => Some(e)
          _setErrorFromException(
500,
 "Exception in main routed callback",
 e.getStackTraceString
)
      }
      
      if (!wresponse.rendered) {
        try {
          routes.filterAfter(path).foreach(ri => ri.callback())
        }
        catch {
          case e: Exception => Some(e)
            _setErrorFromException(
500,
  "Exception in afterFilter",
 e.getStackTraceString
)
        }
      }
    }
    
    // Try for a string render using return data (the default).
    //println("data:" + data)
    if (!wresponse.rendered) {
      render(data)
    }
  }
  
  /** Routed callback to be invoked before other routed callbacks.
    * 
    * Called every time the server applies a request.
    */
  /*
   def before(matchRgx: Regex = ParsedRouteMatcher.all)(action: => Unit) {
   val rm = new ParsedRouteMatcher(matchRgx)
   val pr = new RouteItem(rm, () => action)
   routes.appendBefore(pr)
   }
   */
  def before(matchRgx: String = ParsedRouteMatcher.all)(action: => Unit) {
    val rm = new ParsedRouteMatcher(matchRgx)
    val pr = new RouteItem(rm, () => action, None)
    routes.appendBefore(pr)
  }
  
  /** Routed callback to be invoked after other routed callbacks.
    * 
    * Called every time the server applies a request.
    */
  /*
   def after(matchRgx: Regex = ParsedRouteMatcher.all)(action: => Unit) {
   val rm = new ParsedRouteMatcher(matchRgx)
   val pr = new RouteItem(rm, () => action)
   routes.appendAfter(pr)
   }
   */
  def after(matchRgx: String = ParsedRouteMatcher.all)(action: => Unit) {
    val rm = new ParsedRouteMatcher(matchRgx)
    val pr = new RouteItem(rm, () => action, None)
    routes.appendAfter(pr)
  }

  /** Append a route with an HttpMethod to the RouteMap.
    * 
    * Methods are limited by a fixed class enumeration,
    * @see freight.interfaces.html.idset.HttpMethods.
    * 
    * @param method Http method to be associated with the route.
    *  The methods allowed can be found in the HttpMethod class.
    * @param matchStr string to used for matching (this class
    *  uses a Regex matcher)
    * @param action a closure of code to be invoked if a request
    *  URL matches. 
    */
  def appendRoute(method: HttpMethod, matchStr: String)(action: => Any)
  {
    val rm = new ParsedRouteMatcher(matchStr)
    val pr = new RouteItem(rm, () => action, None)
    routes += (method,  pr)
  }
  
  def appendRouteRestricted(method: HttpMethod, matchStr: String)(accessCallback: ()=> Boolean)(action: => Any)
  {
    val rm = new ParsedRouteMatcher(matchStr)
    val pr = new RouteItem(rm, () => action, Some(accessCallback))
    routes += (method,  pr)
  }
  
  // Convenience functions for building routes. Common methods have a
  // convenience function - see the appendRoute() method to add
  // others.
  /** Routed callback convenience for Http method `OPTIONS`.
    */
  def options(matchStr: String)(action: => Any) = appendRoute(Options, matchStr)(action)

  /** Routed callback convenience for Http method `GET`.
    */
  def get(matchStr: String)(action : => Any) = appendRoute(Get, matchStr)(action)
  
  /** Routed callback convenience for Http method `POST`.
    */
  def post(matchStr: String)(action: => Any) = appendRoute(Post, matchStr)(action)

  /** Routed callback convenience for Http method `PUT`.
    */
  def put(matchStr: String)(action: => Any) = appendRoute(Put, matchStr)(action)

  /** Routed callback convenience for Http method `DELETE`.
    */
  def delete(matchStr: String)(action: => Any) = appendRoute(Delete, matchStr)(action)

  /** Routed callback convenience for Http method `PATCH`.
    */
  def patch(matchStr: String)(action: => Any) = appendRoute(Delete, matchStr)(action)
  
  

  /** Routed callback convenience for Http method `POST`.
    */
  def postRestricted(matchStr: String)(accessCallback: ()=> Boolean)(action: => Any) = appendRouteRestricted(Post, matchStr)(accessCallback)(action)

  /*
   * To redirect within web containers use a relative
   * URL, i.e. without a leading slash, as this will preserve any
   * host/port/appname elements of the Servlet.
   */
  /** Redirect the request to some other URL.
    *  
    * This is a complete URL redirection, respecting the context path
    * (but not the server path).
    * 
    * URLs should be preceeded with a forward slash (indicating they
    * will be, after this method handles them, absolute).
    * 
    * For finer detail, and different scenarios, set a redirect
    * directly using the `wresponse.redirect` method. For more
    * extensive use of unusual redirects, borrow code from this method
    * and construct something suitable.
    */
  def redirect(url: String) {
    println("redirecting to: " + protocolUrl(url, Iterable.empty, true, false))
    wresponse.redirect(protocolUrl(url, Iterable.empty, true, false))
    wresponse.rendered = true
  }
  
  /** Executes this code if a route is requested which does not match.
    *
    * By default, in development mode, `notFound` renders a list of
    * routes. If development mode is false, `notFound` returns a 404
    * error.
    *
    * `notFound` can be overridden to do other work. `notFound` is
    * similar to other routed callbacks - it expects a string return
    * it can render.  To change this overall behaviour, set the
    * contentType on the response
    */
  def notFound()
      : Any =
  {
    _setError(404, requestMessage(" but the only routes are"), routesToHtml())
  }
  
  /** Executes this code if access is denied to a route.
    * 
    * By default returns a non-active 401 error (no headers to cause
    * browser login automation) concatenated with the Scala toString
    * on the route item.  Can be overridden to do other work. However,
    * it is more likely a mixed-in authentication trait will do the
    * appropriate handling.
    * 
    * `notAuthenticated` is similar to other routed callbacks - it
    * expects a string return it can render or a redirect.
    */
  protected def notAuthenticated(ri: RouteItem)
      : Any =
  {
    _setError(
      401,
      requestMessage(" failed authentication"),
      "access on this route failed.\nCheck callback:\n<br>" + ri.toString
    )
  }

}//ScalaServlet

