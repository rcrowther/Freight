package freight
package canspeak
package breaker


import freight.core.objects.mutable.{GenStringBreaker, SimpleLexer}




/** Breaks Ini from input.
  *
  * A very strict parser. The output must be written by freight for
  * any good chance of success.
  */
//{"id":9,"language":"Mirror","title":"Mirror","description":"Mirror","body":"Mirror"}
//TODO: Should it take a breaker? or just a string?
final class Ini(
  d: String
)
    extends GenStringBreaker
{
  val b = SimpleLexer(d)

  //TOCONSIDER: recoverable?
  //val id = idOrError(idStr)


  private var first = true

  private def number : String = {
    if(first) first = false
    else  b.skipNewline()

    b.symbol
    b ~ " = "
    b.number
  }

  private def fraction : String = {
    if(first) first = false
    else  b.skipNewline()
    b.symbol
    b ~ " = "
    b.fraction
  }

  private def str : String = {
    if(first) first = false
    else  b.skipNewline()
    b.symbol
    b ~ " = "
    b.quoted

  }


  def booleanStr : String = {
    str
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
    fraction
  }

  def doubleStr : String = {
    fraction
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

