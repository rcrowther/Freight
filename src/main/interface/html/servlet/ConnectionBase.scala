package freight.interface.html.servlet

import javax.servlet.http.HttpServlet
import javax.servlet.ServletConfig
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse





/** Connects to Html request/responses.
  * 
  * Subclasses of this class can be used as Runnables. Subclasses have
  * the necessary interfacing to be used in Servlets, pointed at by
  * web.xml files, etc.
  *  
  * Subclasses have several features:
  * 
  * - responses and requests are wrapped in classes.
  * - route-keyed methods are used to filter requests to code (like
  *   Sinatra/Scalatra etc.)
  * - failed routing is passed to an overridable method
  *   `notFound()`. By default, this switches to search for static
  *   resources, if that fails, then renders route information.
  * - both the wrapped response and request are available in the
  *   route-keyed methods
  * - data from requests (e.g. get/post) is gathered into a wrapped
  *   parameters class, also available in the route-keyed methods.
  * - outputs are mostly guessed?
  * - cookies are also wrapped into an API @see WrappedCookie
  * - Most of the underlying gear has toString() methods, so can be
  *   easily examined.
  * 
  * Simplistic usage:
  * 
  * {{{
  * class TestServlet
  *   extends Connection
  *   {
  *     before {
  *       println("before call!")
  *     }
  * 
  *     get("razor") { 
  *       "<p>" + request.pathInfo + "</p>" + "oudda da
  *     blue" } 
  *   }
  * }}}
  * 
  * This class contains little more than the HttpServlet interface
  * override. For the machinery, @see
  * [[freight.interfaces.html.ScalaServlet]].
  * 
  * For a more sophisticated and thoroughly written version of these
  * ideas, @see {{www.scalatra.com}}
  */
abstract class ConnectionBase
    extends HttpServlet
    with ScalaServlet
{
  //type ConfigT = ServletConfig
  

  override def init(config: ServletConfig) {
    super.init(config)
    // Boster our class with an easy access config
    //TODO: Needs cleaning. Is it doing the job?
    initialize(config)
  }
  

  /** Searches for static resources before calling stock notFound.
    */
  override def notFound()
      : Any =
  {
    serveStaticResource() getOrElse super.notFound()
  }
  
  /** Search for static resources.
    *
    * The method concatenates the servlet path with the request
    * path. For absolute URLs this tries the very bottom of the server
    * path, for relative URLs the container path is concatenated to
    * the current URL.
    *
    * This search can fail within numerous configurations and URL
    * variations. The key is to reconfigure links and the server, not
    * this method.
    * 
    * Good solutions depend on the application and deployment. Some
    * apps reconfigure relative links, using pre-calculated back
    * stepping. Some use absolute URLs forwarded or re-routed through
    * server configuration.
    * 
    * Probably the best balance of options overall is to configure
    * links to static resources with an absolute URL using an
    * application name at base ("/myapp/<more paths...>"). If the
    * application name is added dynamically, at initialisation, say,
    * then the application can then be reconfigured if the server
    * setup changes.
    */
  protected def serveStaticResource(): Option[Any] = {
    val (path, urlOpt) = context.resource(wrequest.request)
    val ret = urlOpt map { _ =>
      context.dispatcher.forward(wrequest.request, wresponse.response)
    }

    if (ret != None) wresponse.contentType = context.guessMime(path)
    println(s"static found: $ret wresponse: ${wresponse}")
    ret
  }

  /*
   /**
   * Invoked when no route matches.  By default, calls `serveStaticResource()`,
   * and if that fails, calls `resourceNotFound()`.
   *
   * This action can be overridden by a notFound block.
   */
   protected var doNotFound: Action = () => {
   serveStaticResource() getOrElse resourceNotFound()
   }

   /**
   * Attempts to find a static resource matching the request path.  Override
   * to return None to stop this.
   */
   protected def serveStaticResource(): Option[Any] =
   servletContext.resource(request) map { _ =>
   servletContext.getNamedDispatcher("default").forward(request, response)
   }

   /**
   * Called by default notFound if no routes matched and no static resource
   * could be found.
   */
   protected def resourceNotFound(): Any = {
   response.setStatus(404)
   if (isDevelopmentMode) {
   val error = "Requesting \"%s %s\" on servlet \"%s\" but only have: %s"
   response.getWriter println error.format(
   request.getMethod,
   Option(request.getPathInfo) getOrElse "/",
   request.getServletPath,
   routes.entryPoints.mkString("<ul><li>", "</li><li>", "</li></ul>"))
   }
   }
   
   */



  override def destroy() {
    //shutdown()
    super.destroy()
  }

  override def service(
    request: HttpServletRequest,
    response: HttpServletResponse
  )
  {
    // println(
    // this.getClass.getClassLoader().asInstanceOf[URLClassLoader].getURLs().mkString(",\n")
    //)
    handle(request, response)
  }
  

}//Connection


