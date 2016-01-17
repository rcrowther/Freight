package freight.interface.html.servlet.idset

/** The request scheme from a URL.
 *  
 * i.e. http, https
 */
sealed trait UrlScheme

case object Http extends UrlScheme

case object Https extends UrlScheme