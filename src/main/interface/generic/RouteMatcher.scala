package freight.interface.generic

/** Base class for all route matchers.
  */
abstract class RouteMatcher {
  
  /** Evaluates the request path against the string.
    * @return true if the regex matches the request path, else false.
    */
  def  apply(requestPath: String) : Boolean
  
    /** Appends a pretty representation of the route to a StringBuilder.
    */
  def addPrettyString(b: scala.StringBuilder) : scala.StringBuilder
    
  /** Appends the route to a StringBuilder.
    */
  def addString(b: scala.StringBuilder) : scala.StringBuilder
}
