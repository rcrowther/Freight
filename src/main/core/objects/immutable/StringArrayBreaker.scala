package freight
package core
package objects
package immutable



/** A breaker for arrays of strings.
*
* Used in [[StringObject]] to supply transformers.
*/
class StringArrayBreaker(seq: Array[String])
    //extends StringGiver
    extends GiverForString
{
var i = 0

def ret(): String = {
val r = seq(i)
i += 1
r
}

  def booleanStr() : String = ret()
  def shortStr() : String = ret()
  def intStr() : String = ret()
  def longStr() : String = ret()
  def floatStr() : String = ret()
  def doubleStr() : String = ret()
  def stringStr() : String = ret()
  def textStr() : String = ret()
  def binaryStr() : String = ret()
  def timeStr() : String = ret()
  def timestampStr() : String = ret()
  def localeStr() : String = ret()

}//StringSeqBreaker
