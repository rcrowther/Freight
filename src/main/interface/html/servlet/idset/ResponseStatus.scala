package freight.interface.html.servlet.idset



/** Wraps a response html code and message.
  *
  * Also includes print utilities for status codes.
  */
case class ResponseStatus(code: Int, message: String)
    extends Ordered[ResponseStatus]
{
  def compare(that: ResponseStatus) = code.compareTo(that.code)


  /** Returns a string representation as one line.
    */ 
  def toLineString: String = {
    val b = new StringBuilder(message.length + 5)
    b.append(code)
    b.append(' ')
    b.append(message)
    b.toString()
  }
  
  /** Returns an html representation.
    * 
    *  The representation is a `h3` title.
    */ 
  def toHtmlTitle(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "<h3>HTTP Status "
    b ++= code.toString
    b ++= " - "
    b ++= message
    b ++= "</h3>"
    b
  }
}



object ResponseStatus {

  def apply(code: Int): ResponseStatus =
    ResponseStatus(code, ReasonMap.getOrElse(code, ""))
  
  def reason(code: Int): String = ReasonMap.getOrElse(code, "")


  /** Map of response codes to string reasons.
    */
  // Status code list taken from http://www.iana.org/assignments/http-status-codes/http-status-codes.xml
  val ReasonMap = Map(
    100 -> "Continue",
    101 -> "Switching Protocols",
    102 -> "Processing",
    200 -> "OK",
    201 -> "Created",
    202 -> "Accepted",
    203 -> "Non-Authoritative Information",
    204 -> "No Content",
    205 -> "Reset Content",
    206 -> "Partial Content",
    207 -> "Multi-Status",
    208 -> "Already Reported",
    226 -> "IM Used",
    300 -> "Multiple Choices",
    301 -> "Moved Permanently",
    302 -> "Found",
    303 -> "See Other",
    304 -> "Not Modified",
    305 -> "Use Proxy",
    307 -> "Temporary Redirect",
    400 -> "Bad Request",
    401 -> "Unauthorized",
    402 -> "Payment Required",
    403 -> "Forbidden",
    404 -> "Not Found",
    405 -> "Method Not Allowed",
    406 -> "Not Acceptable",
    407 -> "Proxy Authentication Required",
    408 -> "Request Timeout",
    409 -> "Conflict",
    410 -> "Gone",
    411 -> "Length Required",
    412 -> "Precondition Failed",
    413 -> "Request Entity Too Large",
    414 -> "Request-URI Too Long",
    415 -> "Unsupported Media Type",
    416 -> "Requested Range Not Satisfiable",
    417 -> "Expectation Failed",
    418 -> "I'm a teapot",
    422 -> "Unprocessable Entity",
    423 -> "Locked",
    424 -> "Failed Dependency",
    425 -> "Unordered Collection",
    426 -> "Upgrade Required",
    428 -> "Precondition Required",
    429 -> "Too Many Requests",
    431 -> "Request Header Fields Too Large",
    500 -> "Internal Server Error",
    501 -> "Not Implemented",
    502 -> "Bad Gateway",
    503 -> "Service Unavailable",
    504 -> "Gateway Timeout",
    505 -> "HTTP Version Not Supported",
    506 -> "Variant Also Negotiates",
    507 -> "Insufficient Storage",
    508 -> "Loop Detected",
    510 -> "Not Extended",
    511 -> "Network Authentication Required"
  )
}

