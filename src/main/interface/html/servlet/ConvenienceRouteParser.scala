package freight.interface.html.servlet

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers


/** Parses a string to add some preset regex patterns.
  *  
  * The form is [:xxx:].
  * 
  * Available labels are,
  *  - long (allows 19 digits, slightly more than long)
  *  - int (allows 10 digits, a lot more than int)
  *  - short (allows 5 digits, a big lot more than short)
  *  - optionalElement (zeroOrMore to '/', only unreserved chars)
  *  - element (oneOrMore to '/', only unreserved chars)
  * e.g.
  * {{{
  * url/[:long:]
  * }}}
  */
class ConvenienceRouteParser
    extends RegexParsers
{

  def symbol: Parser[String] = (
    "long" |
      "int" |
      "short" |
      "optionalElem" |
      "elem"
  ) ^^ {str =>
    str match {
      case "long" => """\d{1,19}"""
      case "int" => """\d{1,10}"""
      case "short" => """\d{1,5}"""
      case "optionalElem" => """[a-zA-Z\-._~]*"""
      case "elem" => """[a-zA-Z\-._~]+"""
    }
  }
  def klass: Parser[String] = "[:" ~> symbol <~ ":]"
  def free:  Parser[String] = """[^\[]+""".r
  def root: Parser[String] = rep(free | klass) ^^(_.mkString)
}



/** Makes a RouteParser available as an object.
  */
object RouteParse
    extends ConvenienceRouteParser
{
  
  def test(route: String)
  {
    println(parseAll(root, route))
  }
  
  /** Returns a parsed from a route, or throw an error.
    *  
    * @return A string with any labels replaced.
    */
  def apply(route: String) : String =
    parseAll(root, route) match {
      case Success(res, _) => res
      case _ =>
        throw new IllegalArgumentException("Invalid route pattern: " + route)
    }
}
