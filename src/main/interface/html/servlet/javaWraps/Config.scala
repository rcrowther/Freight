package freight.interface.html.servlet
package javaWraps

import java.net.{MalformedURLException, URL}
import javax.servlet.http.{HttpServlet, HttpServletRequest}
import javax.servlet.{ServletConfig, ServletContext }
import javax.servlet.RequestDispatcher

/** Carries data about a servlet config.
 *  
 *  *Not to be confused with a Freight Config file!*
 *  
  * `Config` is a Scala representation of Java's
  * `javax.servlet.http.ServletConfig`. It wraps the object in a more
  * Scalalike API, and provides, on the initParameters, all the operations found in a map.
  */
//TODO: Needs cleaning. Is it doing the job?
case class Config(c: ServletConfig)
{

  /** Returns a reference to the context this config is running in.
   */
  def context : ServletContext = c.getServletContext
  
  /** Returns the name of this servlet instance.
   */
  def name : String = c.getServletName


  object initParameters extends scala.collection.DefaultMap[String, String] {
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

  }

  def addString(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "name:"
    b ++= name
    b ++= ", InitParams("
    b ++= initParameters.mkString(" ,")
    b ++= ")"
    b
  }
    
}//Config
