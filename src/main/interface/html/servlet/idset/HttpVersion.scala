package freight.interface.html.servlet.idset

/** Representation of Http versions.
 * 
 * Uses a case class, so instances can be compared.
 */
case class HttpVersion(
    val protocolName: String,
    val majorVersion: Int,
    val minorVersion: Int
    )
    {
       override def toString: String = protocolName + "/" + majorVersion + '.' + minorVersion
    }