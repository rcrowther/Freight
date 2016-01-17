package freight.interface.generic

import scala.util.matching.Regex
//import freight.interfaces.common.RouteMatcher


//final class RegexRouteMatcher(regex: Regex)

/** Matcher for routes in regex notation.
  */
abstract class RegexRouteMatcher //(str: String)
    extends RouteMatcher
  {

  // Can be overridden to do parsing of strings, for example.
  //TODO: Add the start anchor back when sorted base relative with / or not
//  val regex : Regex = new Regex('^' + str + '$')
   protected val regex : Regex// = new Regex(str + '$')
   
  /** Evaluates the request path against the regular expression.
    * @return true if the regex matches the request path, else false.
    */
  def apply(requestPath: String)
      : Boolean =
  {
    //println ("match:")
    regex.findFirstIn(requestPath) match {
      case Some(x) => true
      case _ => false
    }
  }

  def addPrettyString(b: StringBuilder)
      : StringBuilder =
  {
    val s = regex.toString
// Len is always greater than 2 because of anchors. Drop the
// first char if not singular.
    val fs = 
if (s.size < 4) s.substring(1, s.length - 1)
else s.substring(2, s.length - 1)

    b ++= fs
  }


  def addString(b: StringBuilder)
      : StringBuilder =
  {
    b ++= regex.toString
  }
  
  override def toString
      : String =
  {
    "RegexRouteMatcher(" + regex.toString + ")"
  }
  
}//RegexRouteMatcher

/*
object RegexRouteMatcher {
  //private lazy val _all =  ".*".r
  //def all : Regex = _all
  private lazy val _all =  ".*"
  def all : String = _all

}//RegexRouteMatcher
*/
