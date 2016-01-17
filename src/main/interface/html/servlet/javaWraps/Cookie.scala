package freight
package interface.html.servlet
package javaWraps

import java.util.{Calendar, TimeZone, Date, Locale}
import core.util.DateTime

case class CookieOptions(
  domain  : String  = "",
  path    : String  = "",
  maxAge  : Int     = -1,
  secure  : Boolean = false,
  comment : String  = "",
  httpOnly: Boolean = false,
  version : Int = 0,
  encoding: String  = "UTF-8"
)

/** Persists data between client and server.
  *  
  * `Cookie` is a Scala representation of Java's
  * `javax.servlet.http.HttpResponse` cookie methods. 
  * It separates the handling into a namespaced class of data and methods.
  */
case class Cookie(
  name: String,
  value: String
)(implicit cookieOptions: CookieOptions = CookieOptions())
{
  import Cookie._

  val options = cookieOptions

  def toCookieString = {
    val sb = new StringBuffer
    sb append name append "="
    sb append value

    if(!isBlank(cookieOptions.domain) && cookieOptions.domain != "localhost")
      sb.append("; Domain=").append(
        (if (!cookieOptions.domain.startsWith(".")) "." + cookieOptions.domain else cookieOptions.domain).toLowerCase(Locale.ENGLISH)
      )

    val pth = cookieOptions.path
    if(!isBlank(pth)) sb append "; Path=" append (if(!pth.startsWith("/")) {
      "/" + pth
    } else { pth })

    if(!isBlank(cookieOptions.comment)) sb append ("; Comment=") append cookieOptions.comment

    appendMaxAge(sb, cookieOptions.maxAge, cookieOptions.version)

    if (cookieOptions.secure) sb append "; Secure"
    if (cookieOptions.httpOnly) sb append "; HttpOnly"
    sb.toString
  }

  private[this] def appendMaxAge(sb: StringBuffer, maxAge: Int, version: Int) = {
    val dateInMillis = maxAge match {
      case a if a < 0 => None // we don't do anything for max-age when it's < 0 then it becomes a session cookie
      case 0 => Some(0L) // Set the date to the min date for the system
      case a => Some(currentTimeMillis + a * 1000)
    }

    // This used to be Max-Age but IE is not always very happy with that
    // see: http://mrcoles.com/blog/cookies-max-age-vs-expires/
    // see Q1: http://blogs.msdn.com/b/ieinternals/archive/2009/08/20/wininet-ie-cookie-internals-faq.aspx
    val bOpt = dateInMillis map (ms => appendExpires(sb, new Date(ms)))
    val agedOpt = if (version > 0) bOpt map (_.append("; Max-Age=").append(maxAge)) else bOpt
    agedOpt getOrElse sb
  }

  private[this] def appendExpires(sb: StringBuffer, expires: Date) =
    sb append  "; Expires=" append formatExpires(expires)
  
  def addString(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "name:\""
    b ++= name
    b ++= "\", domain:\""
    b ++= cookieOptions.domain
    b ++= "\", path:\""
    b ++= cookieOptions.path
    b ++= ", contentType:"
    b ++= cookieOptions.path
    b ++= ", maxAge:"
    b ++= cookieOptions.maxAge.toString
    b ++= ", secure:"
    b ++= cookieOptions.secure.toString
    b ++= ", version:"
    b ++= cookieOptions.version.toString
    b ++= ", encoding:"
    b ++= cookieOptions.encoding
    b
  }
  
}//Cookie


object Cookie {
  @volatile private[this] var _currentTimeMillis: Option[Long] = None
  def currentTimeMillis = _currentTimeMillis getOrElse System.currentTimeMillis
  def currentTimeMillis_=(ct: Long) = _currentTimeMillis = Some(ct)
  def freezeTime() = _currentTimeMillis = Some(System.currentTimeMillis())
  def unfreezeTime() = _currentTimeMillis = None
  def formatExpires(date: Date) = DateTime.formatDate(date, "EEE, dd MMM yyyy HH:mm:ss zzz")

}//CookieO
