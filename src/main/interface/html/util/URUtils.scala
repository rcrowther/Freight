package freight.interface.html.util

import java.net.{URI, URL}
import org.apache.commons.codec.net.URLCodec

//Fromwhere?
//import rl.UrlCodingUtils


// URUtils.urlDecode("http://www.psyche.co.uk")

// Scalatra uses the RL library at
// https://github.com/scalatra/rl/releases
// available through Maven
// http://mvnrepository.com/artifact/org.scalatra.rl/rl_2.10
// Which is cute as anything, but I want to know we'll use it good
// before I go for dependancy. R.C.
object URUtils {
  
  // These may not work for params, but it works ok, and is not Java aggressive.
  // If not  happy, develop it (or import RL) R.C.
  //
  
  /** Decode a URL
   * 
   * URLs can use ASCII encoding, sometimes seen in URLS ("%20", for [space])
   * This should transform them.
   * 
   * Query and fragment data is stripped.
   */
  def urlDecode(url: String) : String = {
        val c = new URLCodec()
        c.decode(url)
  }

    /** Decode a URL
   * 
   * URLs can use ASCII encoding, sometimes seen in URLS ("%20", for [space])
   * This should transform them.
   * 
   * This must have a protocol?.
   */
  def urlEncode(url: String) : String =  {
        val c = new URLCodec()
        c.encode(url)
  }
  
}
