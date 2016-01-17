package freight.interface.html.servlet
package javaWraps

import scala.language.implicitConversions
import javax.servlet.ServletContext
import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpSession}



/** Implicit conversions between Java servlet class objects and Freight wrappers. 
 * 
 * These objects include Requests, Responses, Sessions and Contexts.
 */
//TODO: So far, not using this trait, and is there any need?
trait Implicits {
  implicit def toRequest(request: HttpServletRequest): Request =
   Request(request)

  implicit def toResponse(response: HttpServletResponse): Response =
    Response(response)

  implicit def toFreightSession(session: HttpSession): Session =
    Session(session)

  implicit def toWrappedContext(servletContext: ServletContext): Context =
    Context(servletContext)
}

object Implicits extends Implicits
