package freight.interface.generic

//import freight.interfaces.common.RouteMatcher


/** Gathers data on a route.
 *  
 *  To bind parameters to the callbacks, wrap in a DynamicParameters method.
 *  
  * The matcher can be used to conditionally execute the callback.
  * @param matcher a RouteMatcher
  * @param callback a callback to execute  when the mater suceeds.
  * @param accessCallback an optional callback to determine access (authentication) to this route.
  */
class RouteItem (
  val matcher: RouteMatcher,
  val callback: () => Any,
  val accessCallback: Option[() => Boolean]
)
{
      
  /** Appends the route to a string builder.
    */
  def addPrettyString(b: scala.StringBuilder)
      : scala.StringBuilder =
  {
    matcher.addPrettyString(b)
    if(accessCallback != None) {
      b ++= " : Restricted"
    }
    b
  }

    def toPrettyString
      : String =
  {
    addPrettyString(new StringBuilder).toString
  }

  override def toString()
      : String =
  {
    val b = new StringBuilder("RouteItem(matcher:")
    b append matcher
    b ++= ", callback:"
    b append callback
    b ++= ", accessCallback:"
    b append accessCallback
b += ')'
    b.result
  }

}//RouteItem



object RouteItem {

  def apply(
    matcher: RouteMatcher,
    callback: () => Any
  )
      : RouteItem =
  {
    new RouteItem(matcher, callback, None)
  }

  def apply(
    matcher: RouteMatcher,
    callback: () => Any,
    accessCallback: () => Boolean
  )
      : RouteItem =
  {
    new RouteItem(matcher, callback, Some(accessCallback))
  }
}
