package freight.interface.html



/** A servlet connection for HTML.
  *
* == Install ==
* This module needs a,
*  - javax-servlet-api-<version>.jar
*
* to compile. Some people may like to add JSP templating (humm, what about Scalate, or FreeMarker?),
*  - jsp-api-<version>.jar
*
* == Running the servlet in a container server ==
* A WebContent folder with J2EE contents is needed. An example is provided in the `html` folder. There is nothing special in the contents of WebContent, with a standard `web.xml` file which forwards all non-welcome requests to an instance of [[freight.interface.html.servlet.SyncConnection]].
*
* If not using an IDE, to use the code directly,  
  * == Background ==
  * The HTML Connection is based in the Java JEE2 specification.
  *
  * The Connection is a Runnable with the necessary JEE2 interfacing. So subclassses are valid Servlets. They can be used in servers handling containers (Jetty, GlassFish etc.), pointed at by web.xml files, etc.
  *
  * == Overview ==
  * The classes use a routing DSL similar to Sinatra/Scalatra.
  *
  * For a more sophisticated and thoroughly written version of these
  * ideas, @see {{www.scalatra.com}}
  *
  * == Feature List ==
  *
  * Connection subclasses have several features:
  * 
  *  - responses and requests are wrapped in classes.
  *  - route-keyed methods are used to filter requests to code (like
  *   Sinatra/Scalatra etc.)
  *  - routes use regex matching.
  *  - routes are grouped by Http method (so different methods can have
  *   the same route matcher)
  *  - failed routing is passed to an overridable method
  *   `notFound()`. By default, this renders route information.
  *  - the wrapped response and request are available in the
  *   route-keyed methods
  *  - data in requests (get or post) are returned as a Scala-friendly
  *   map[String, Seq[String]] from request.parameters.
  *  - data from requests (e.g. get/post) is gathered into a wrapped
  *   parameters class (type: Map[String[String]]), also available in
  *   the route-keyed methods.
  *  - outputs are mostly guessed?
  *  - cookies are also wrapped into an API @see WrappedCookie
  *
  * == Implementation Notes ==
  *  - every callback returns something, if only empty strings and an error set.
  *  - all exceptions are caught. Any which escape code to the
  *   connection will send Http errors to the server.
  *  - Most of the underlying gear has toString() methods, so can be
  *   easily examined.
  *
  * == Modes ==
  * Development mode is turned on by default.
  * It can be turned off by overriding the variable `developmentMode` to `false`.
  * 
  * In development mode, when FObjects render, they render with boxes round them and the name of the 
  * supplying object's generic name (the 'meta' object type). Also, if a route fails, the default
  * `routeFailed` method renders a list of interactive route information.
  * 
  * The features are somewhat influenced, though not nearly so deeply implemented, by features in
  * {{http://seaside.st/}} (`halos`) and {{https://helloreverb.com/developers/swagger}} (the admin API).
  * Though, compared to those phenomenal 
  * pieces of software, Freight is smaller and has a very different remit. 
  * 
  * == Routes ==
  * Currently matched by Regex. Other routing styles are possible, and
  * custom routing too, but there are no current implementations.
  *
  * Only one route is allowed per block.
  * 
  * Routes are matched from the top of declarations, not the
  * bottom (note for Scalatra users, this is backwards).
  * The matcher enforces specific matches on the routes (matching end with anchors),
  * so any ordering will usually work.
  * However, if matches like "\w*" or [:wild:] are used, then the order may become significant.
  * It is likely better to put more general routing towards the bottom of the file.
  *
  * Only one main route block is matched and executed. Later
  * declared matches will not be executed.
  * 
  * The builtin declaration of `matchPath` enforces a trailing slash on the route to be matched.
  * There is no need to state this in the route, though.
  * 
  * To match a pathless URL (e.g. for 'home' page), put this at the end of the Connection class,
  * 
  * {{{
  * // Matches http://www.mysite/ and http://www.mysite
  * get(""){
  *   ...
  *   }
  * }}}
  * 
  * 
  * The current routing has a couple of convenience methods.
  * 
  * {{{
  * // Matches a freightId (not exactly, but up to 19 digits)
  * get("/test/[:freightId:]"){
  *   ...
  *   }
  *   
  * // Matches anything for the first element.
  * // [:wild:] stops its ignorance when it reaches a forward slash ('/')  
  * get("/[:wild:]/insert"){
  *   ...
  *   } 
  * }}}
  * 
  * 
  * == Available Values ==
  * Unlike other Sinatra rewrites, the routes do not pass values into the block.
  * They match, that's all.
  * 
  * Data is available from the methods `wrequest` and `wresponse`. These return a
  * reduced, more Scala-friendly and coherent version of the Java request/response classes.
  * 
  * However, the convenience method `url` is available (the same as `wrequest.url`).
  * This returns a [[freight.interfaces.html.servlet.URLDisassembler]]).
  * It can clean up a lot of connection code. As this method returns a class, if used a lot, it can be loaded to a value. 
  * 
  * To access cookies, use `wrequest.cookies` etc. To access the original Java request, use `wrequest.request`.
  * 
  * 
  * == Return Values ==
  * The connection expects, as default, a String return. 
  * This will be rendered with the default contentType (Mime type) "text/html".
  *
  * Unlike Sinatra clones, the connection makes few guesses about the
  * type or rendering of return values - the style is more Java than
  * that.
  *
  * To return other types there are a set of convenience methods,
  * `renderInputStream(data: InputStream)` and it's friends. These can
  * be placed at the end of any routed callback, and the code will
  * understand.
  *
  * It is the responsibility of the coder to match the contentType
  * (Mime type) correctly for the rendered body.
  *
  * Example,
  * {{{
  * get("/test/img"){
  *   wresponse.contentType = "image/jpeg;"
  *   val a = Array[Byte]
  *   renderByteArray(a)
  *   }
  * }}}
  *
  * Rendering an image,
  * 
  * {{{
  * get("image") {
  *   import java.io.FileInputStream
  *   wresponse.contentType = "image/jpeg" 
  *   val fin = new FileInputStream("/home/robert/Photos/summer_of_64/IMG_53.JPG")
  *   renderInputStream(fin)
  * }
  * }}}
  *
  * Rendering text,
  *  
  * {{{
  * get("razor") {
  *   // String returns are implicitly rendered
  *   "<p>" + wrequest.pathInfo + "</p>"
  *   }
  * }}}
  *
  * Rendering text,
  *   
  * {{{
  *  get("string"){
  *   val str = "Sheepshank"
  *   // Explicity return a string for rendering
  *   renderString(str)
  *   }
  * }}}
  *
  * == Templating ==
  * Freight provides no templating (and Core never will).
  * But an HTTP interface is non-compliant without support markup.
  * Freight uses the [[freight.interfaces.html.FBuilder]]. 
  * 
  * FObjects are rendered automatically. Custom objects too.
  * So if you buy into the system, and do not wish to subvert it, there is little to do.
  * 
  * The main necessity is to make a surrounding HTML wrap for each render
  *  (in the Sinatra, Ruby On Rails and similar templating systems, a ''layout'').
  * Freight will not do this automatically for you, but
  * has some prebuilds. The most straightforward approach is to define a method in the
  * connection,
  * 
  * {{{
  *  def layout[Meta](obj: FObject[Meta])
  *  : String =
  *  {
  *   val b = new StringBuilder()
  *    Templates.fsystem(
   *     messages = flashMkHtml,
    *    title = obj.stringPrefix
     *   )(content = obj)
      *  b.result()
  * }
  * }}}
  * 
  * Note that the builder will not take strings for the `content` argument, as it is functional
  * (it wants something that accepts a StringBuilder, and an FObject can behave like that)
  * Also note that this template will use the CSS provided as stock.
  * 
  * Now a route can look like,
  * 
  * {{{
  *   get("paper/display/[:freightId:]") {
  *     layout( mongoColl(wrequest.url.path(3).toLong) )
  *     }
  * }}}
  * 
  * Unlike other systems, Freight system has very little to say about all of this. 
  * Put renderers in objects. Add a templating system. Gather them in one place or associate them with code. 
  * Configure and implement the way you think fit. 
  * 
  * In this, Freight was somewhat influenced by the wayward and wonderful
  * {{http://seaside.st/}}
  * 
  * == Other return modifications ==
  * Ways are redirection, error throwing. Examples,
  *
  * {{{
  * get("/test/redirect"){
  *   redirect("/test/otherplace")
  *   }
  * }}}
  *
  * {{{
  * get("/test/error"){
  *   renderError(404, "quite a mystery, this")
  *   }
  * }}}
  *
  * These examples also apply to the notFound() callback.
  *
  * == Sample Usage ==
  * For a fuller list of features, see above,
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
  *       "<p>" + wrequest.pathInfo + "</p>" +
  *       "<p>" + wrequest.parameters.toString + "</p>" +
  *       "oudda da blue"
  *     } 
  *   }
  * }}}
  *
  * This new class `TestServlet` can be pointed to using the web.xml
  * file in a J2EE project.
  *
  *
  * == Filters ==
  * Two special methods, before() and after(), allow code to be run
  * before and after the main routing block.
  *
  * These callbacks can be routed. If they are not, then they are run
  * on every request.
  * 
  * Note that the routing means that parameter brackets will be needed
  * even to signal an empty route(unlike much Scala). Without them,
  * the connection will try to eveluate the body as a route, resulting
  * in strange errors.
  * 
  * {{{
  * class TestServlet
  *   extends Connection
  *   {
  *     //match any route
  *     before() {
  *       println("before call!")
  *     }
  * 
  *     // match a specific route
  *     before("excentric") {
  *       println("before call!")
  *     }
  * 
  *     // No, will result in a server error.
  *     before {
  *       println("before call!")
  *     }
  * }}}
  *
  * == the notFound() method ==
  * A special method called when a route is not matched. The base
  * method contains tthe code to render missing paths (developmentMode
  * is true) or an error
  *
  * The method can be overriden to do whatever is required. Code
  * within the method functions as in the other preset methosd, the
  * objects can be acessed, errors rendered, etc.
  *
  * == Static files ==
  * Currently, there is no static file provision.
  *
  * == Exception handling ==
  * If exceptions escape the class, they are caught and a `500 Error`
  * returned. There should '''never''' be a server error page from the
  * servlet.
  *
  * If a path is not found, a `404 error` is returned.
  *
  * An overridable variable `developmentMode` controls errors. When true
  *  - All errors returned on custom styled Freight pages 
  *  - Error titles are supplemented with path (request) data.
  *  - Error titles inform which general area of operation caused the error.
  *  - When a path is missing, other paths from the same path are
  *   rendered to the browser.
  *  - Exceptions are printed to the browser.
  * when false,
  *  - errors are from the server via the servlet API
  *  - Request data is not included
  *  - error messages are generic stubs.
  *  - Exceptions are not reported to the browser.
  * 
  * The value is `true` by default, and should probably be overridden
  * to `false` for production.
  *
  * ''Note:'' when error messages are generated from within callbacks,
  * they can have custom messages.
  * 
  * == Pipeline handling ==
  * If the sequence of callbacks (before(), main route, after() etc.)
  * encounters rendering code, it will cease to perform further
  * actions. This applies to errors generated by escaping exceptions,
  * even if nothing is rendered.
  *
  * == Templating ==
  * There is no provision for templating, because the connection is seen as a
  * base connector (not that Scala's base XML provision is much use
  * for HTML - it isn't).
  * 
  * A template engine, such as FreeMarker, can be easily traited into
  * the Connection subclass. You'll probably want to set it's context
  * through an override of the init() method (not an before() method),
  * thus,
  * 
  * {{{
  * }}}
  *
  * == Cache Headers ==
  *
  * == Cookies ==
  *  ?
  *
  * == Acessing Request and Response Objects ==
  * Request and reposnse objects are available everywhere (notFound(),
  * main routed callbacks, before()/after() filters etc.)
  *
  * For various reasons, the connection wraps the request and
  * response. The API for these can be found in WrappedRequest and
  * WrappedResponse. It ius mainly convenience methods. Access the
  * objects by calling methods on `wrequest` or `wresponse`,
  *
  * {{{
  * ...
  *     get("razor") {
  *       val ct = wrequest.contentType
  * wrequest.contentType = "gzip"
  *     }
  * ...
  * }}}
  * 
  */
package object servlet {

/** Tests if a string is null, empty, or full of whitespace.
*
* Wouldn't want this everywhere, but deals with Java interaction nicely.
*/
// NB now in core.util.String
  def isBlank(str: String) : Boolean = (str == null || str.trim.isEmpty)
}
