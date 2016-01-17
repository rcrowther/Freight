package freight.interface.html.servlet
package javaWraps

import java.net.{MalformedURLException, URL}
import javax.servlet.http.{HttpServlet, HttpServletRequest}
import javax.servlet.ServletContext
import javax.servlet.RequestDispatcher

/** Carries data about a servlet context.
 *  
  * `Context` is a Scala representation of Java's
  * `javax.servlet.http.ServletContext`. It wraps the object in a more
  * Scalalike API, and provides, on itself and on the initParameters, all the operations found in a map.
  */
//TODO: Needs cleaning. Is it doing the job?
case class Context(c: ServletContext)
      extends AttributesMap
{
  
  protected def attributes = c
  
  
  def context : ServletContext = c

   /** Guess the MIME of the given URL.
*
* Probably taken from a builtin table. Odd notion,but the gear is likely in here.
*/
  def guessMime(p: String) : String = {
c.getMimeType(p)
}

 /** Optionally returns a URL to the resource mapped to the given path.
   *
   * @param path a path to the resource
   * @return the resource located at the path, or None if there is no resource
   * at that path.
   */
  def resource(path: String): Option[URL] =
    try {
      Option(c.getResource(path))
    }
    catch {
      case e: MalformedURLException => throw e
    }
    
      /**  Optionally returns a URL to the resource mapped to the given path.
   *
   * @param req a servlet request
   * @return the resource located at the result of concatenating the request's
   * servlet path and its path info, or None if there is no resource at that path.
   */
  def resource(req: HttpServletRequest): Tuple2[String, Option[URL]] = {
    val path = req.getServletPath + (Option(req.getPathInfo) getOrElse "")
    //println(s"not found, trying static resources at path:$path ")

   (path, resource(path))
  }

        /**  Returns the named request dispatcher.
         *   
   * Request dispatchers wrap servlets, forwarding requests to resources.
   * The name is the servelt name, set in web.xml, or via the server.
   * 
   *  See `javax.servlet.RequestDispatcher` documentation for little more information.
   */
  def dispatcher(name: String)
  : RequestDispatcher = 
  {
    c.getNamedDispatcher(name)
  }
  
  
          /**  Returns the default request dispatcher.
         *   
   * Request dispatchers wrap servlets, forwarding requests to resources.
   * The default dispatcher is the dispatcher forwarding from the current servlet?
   * 
   *  See `javax.servlet.RequestDispatcher` documentation for little more information.
   */
    def dispatcher
  : RequestDispatcher = 
  {
    c.getNamedDispatcher("default")
  }

  /** Returns the context path.
   * 
   */
  def contextPath = c.getContextPath
  
  
  object initParameters extends collection.mutable.Map[String, String] {
    def get(key: String): Option[String] = Option(c.getInitParameter(key))

    def iterator: Iterator[(String, String)] = {
      val coll = c.getInitParameterNames

      new Iterator[(String, String)] {

        def hasNext: Boolean = coll.hasMoreElements

        def next(): (String, String) = {
          val nm = coll.nextElement()
          (nm, c.getInitParameter(nm))
        }
      }
    }

    def +=(kv: (String, String)): this.type = {
      c.setInitParameter(kv._1, kv._2)
      this
    }

    def -=(key: String): this.type = {
      c.setInitParameter(key, null)
      this
    }
  }

    override def addString(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "Supported Servlet Version:"
    b ++= c.getMajorVersion().toString
    b += '-'
    b ++= c.getMinorVersion().toString
        b ++= ", ContextPath:"
    b ++= c.getContextPath()
    b ++= ", InitParams("
    b ++= initParameters.mkString(" ,")
    b ++= "), attributes:("
    b ++= this.mkString(", ")
    b ++= ")"
    b
  }
}//Context
