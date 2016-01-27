package freight
package core
package util



/** Utilities for handling strings.
 */
object StringUtil{
  
def count(s: String, chr: Char) : Int = {
var i = 0
s.foreach{ c =>if(chr == c) i +=1 }
i
}

/** Tests if a string is null, empty, or full of whitespace.
*/
  def isBlank(str: String) : Boolean = (str == null || str.trim.isEmpty)

}//String
