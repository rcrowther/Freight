package freight.interface.html.servlet

import scala.util.DynamicVariable
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javaWraps.{Request, Response}

/** Binds Servlet data to a closure.
 * 
 * Explained below, which grew to be Scalatra,
 * http://www.riffraff.info/2009/4/11/step-a-scala-web-picoframework
 */
trait DynamicParameters {
  /**
   * The currently scoped request.  Valid only inside the `handle` method.
   */
  implicit def wrequest: Request = dynamicRequest.value

  private[this] val dynamicRequest = new DynamicVariable[Request](null)

  /**
   * The currently scoped response.  Valid only inside the `handle` method.
   */
  implicit def wresponse: Response = dynamicResponse.value

  private[this] val dynamicResponse = new DynamicVariable[Response](null)

  protected def withRequestResponse[A](request: Request, response: Response)(f: => A) = {
    withRequest(request) {
      withResponse(response) {
        f
      }
    }
  }

  /**
   * Executes the block with the given request bound to the `request`
   * method.
   */
  protected def withRequest[A](request: Request)(f: => A) =
    dynamicRequest.withValue(request) {
      f
    }

  /**
   * Executes the block with the given response bound to the `response`
   * method.
   */
  protected def withResponse[A](response: Response)(f: => A) =
    dynamicResponse.withValue(response) {
      f
    }
  
}//DynamicParameters
