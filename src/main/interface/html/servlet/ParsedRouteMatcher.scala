package freight.interface.html.servlet

import freight.interface.generic.RegexRouteMatcher
import scala.util.matching.Regex



/** Matches routes using a parsed version of the regex string.
 * 
 * The parser used is [[freight.interfaces.html.ConvenienceRouteParser]]
 */
class ParsedRouteMatcher(str: String)
extends RegexRouteMatcher
{
   //protected val regex : Regex = new Regex(RouteParse(str) + '$')
  // Adds string delimiters to the regex, so nothing slips through.
   protected val regex : Regex = new Regex("^/" + RouteParse(str) + '$')
}



object ParsedRouteMatcher {
  //private lazy val _all =  ".*".r
  //def all : Regex = _all
  private lazy val _all =  ".*"
  def all : String = _all

}//RegexRouteMatcher
