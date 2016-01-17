package freight
package canspeak
package breaker


import freight.core.objects.mutable.{GenStringBreaker, SimpleLexer}




/** Breaks JSON from input.
  *
  * A very strict parser. The output must be written by freight for
  * any good chance of success.
  */
//{"id":9,"language":"Mirror","title":"Mirror","description":"Mirror","body":"Mirror"}
//TODO: Should it take a breaker? or just a string?
final class JSON(
//val transform: StringTransformer,
 // idStr: String,
d: String
)
    //extends StringMultiBreaker[String]
    extends GenStringBreaker
{
  val b = SimpleLexer(d)

//TOCONSIDER: recoverable?
//val id = idOrError(idStr)


  private var first = true
  b ~ '{'

  private def number : String = {
    if (first) first = false
    else b ~ ','
    b.quoted
    b ~ ':'
    b.number
  }

  private def str : String = {
    if (first) first = false
    else b ~ ','
    b.quoted
    b ~ ':'
    b.quoted
  }

  def booleanStr : String = {
    if (first) first = false
    else b ~ ','
    b.quoted
    b ~ ':'
    b.symbol
  }

  def shortStr : String = {
    number
  }

  def intStr : String = {
    number
  }

  def longStr : String = {
    number
  }

  def floatStr : String = {
    if (first) first = false
    else b ~ ','
    b.quoted
    b ~ ':'
    b.fraction
  }

  def doubleStr : String = {
    if (first) first = false
    else b ~ ','
    b.quoted
    b ~ ':'
    b.fraction
  }

  def stringStr : String = {
    str
  }

  def binaryStr : String = {
    str
  }

  def textStr : String = {
    str
  }

  def timeStr : String = {
    number
  }

  def timestampStr : String = {
    number
  }

  def localeStr : String = {
    str
  }

  def reset() = b.reset()
  def reload(d: String) = b.reload(d)
}

