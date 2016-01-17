package freight.interface.generic

import scala.collection.mutable.ArrayBuffer



/** A sequence of route items.
  * 
  * This class gathers RouteItems into a collection. It has methods to
  * match [[freight.interfaces.common.RouteItem]]. For simple cases, it can be used on it's own, or
  * several can be gathered together.
  * 
  * See [[freight.interfaces.html.ConnectionMap]], which uses several
  * RouteSeq for before/after/Http verb route matching.
  */
//NB I tried building this as a limited method composition, but
// there's too much mapping which is handy elsewhere (hence the
// *match methods, preferred to inconsistent overrides) R.C.
// Could probably use a rethink about building to immutable, anyway.
class RouteSeq(
  var self: ArrayBuffer[RouteItem] = ArrayBuffer.empty
)
    extends collection.mutable.BufferProxy[RouteItem] 
{

  /** Returns the items matching a route.
    */
  def filterMatch(route: String)
      : Seq[RouteItem] =
  {
    //java.util.NoSuchElementException:
    self.filter(
      (routeItem) => routeItem.matcher(route)
    )
  }

  /** Optionally returns the first item matching a route.
    */  
  def findMatch(route: String)
      : Option[RouteItem] =
  {
    //java.util.NoSuchElementException:
    self.find((ri) => ri.matcher(route))
  }
  
  /** Returns a seq of pretty strings representing the items in this seq.
    */
  def mapPrettyString()
      : Seq[String] =
  {
    self.map{_.toPrettyString}
  }

  /** Appends the route items to a string builder.
    *  
    * Implemented to avoid adding class context or callback data.
    */  
  def addPrettyString(b: scala.StringBuilder, sep: String)
      : scala.StringBuilder =
  {
    var first = true

    for (ri <- self) yield {
      if (first) {
        ri.addPrettyString(b)
        first = false
      }
      else {
        b append sep
        ri.addPrettyString(b)
      }
    }

    b
  }
  
    /** Represents route items as string.
    *  
    * Implemented to avoid adding class context or callback data.
    */  
  def mkPrettyString(sep: String)
      : String =
  {
   addPrettyString(new StringBuilder, sep).toString
  }
  
}//RouteSeq



object RouteSeq {
  def empty : RouteSeq = new RouteSeq(ArrayBuffer.empty)
  
  def apply(ri: RouteItem*)
      : RouteSeq =
  {
    new RouteSeq(ArrayBuffer(ri: _*))
  }
  
}//RouteSeqO
