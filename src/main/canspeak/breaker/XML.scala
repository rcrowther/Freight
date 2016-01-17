package freight
package canspeak
package breaker


import freight.core.objects.mutable.{GenStringBreaker, SimpleLexer}



/** Breaks XML from input.
  *
  * A very strict parser. The output must be written by freight for
  * any good chance of success.
  */
//<id type=id>9</id><language type=string>Mirror</language><title type=string>Mirror</title><description type=string>Mirror</description><body type=text>Mirror</body>null
//TODO: Should it take a breaker? or just a string?
final class XML(
//val transform: StringTransformer,
   // idStr: String,
d: String
)
    //extends StringMultiBreaker[String]
    extends GenStringBreaker
{


  val b = new SimpleLexer(d)

//TOCONSIDER: recoverable?
//val id = idOrError(idStr)

  def booleanStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=boolean"
    b ~ '>'
    val v = b.symbol
    b ~ "</"
    b.symbol
    b ~ '>'
    v
  }

  def shortStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=short"
    b ~ '>'
    val v = b.number
    b ~ "</"
    b.symbol
    b ~ '>'
    v
  }

  def intStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=int"
    b ~ '>'
    val v = b.number
    b ~ "</"
    b.symbol
    b ~ '>'
    v
  }

  def longStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=long"
    b ~ '>'
    val v = b.number
    b ~ "</"
    b.symbol
    b ~ '>'
    v
  }

  def floatStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=float"
    b ~ '>'
    val v = b.fraction
    b ~ "</"
    b.symbol
    b ~ '>'
    v
  }

  def doubleStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=double"
    b ~ '>'
    val v = b.fraction
    b ~ "</"
    b.symbol
    b ~ '>'
    v
  }

  def stringStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=string"
    b ~ '>'
    val v = b.freeStringUntil("</")
    b.symbol
    b ~ '>'
    v
  }

  def binaryStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=binary"
    b ~ '>'
    val v = b.freeStringUntil("</")
    b.symbol
    b ~ '>'
    v
  }

  def textStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=text"
    b ~ '>'
    val v = b.freeStringUntil("</")
    b.symbol
    b ~ '>'
    v
  }

  def timeStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=time"
    b ~ '>'
    val v = b.number
    b ~ "</"
    b.symbol
    b ~ '>'
    v
  }

  def timestampStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=timestamp"
    b ~ '>'
    val v = b.freeStringUntil("</")
    b.symbol
    b ~ '>'
    v
  }



  def localeStr : String = {
    b ~ '<'
    b.symbol
    b ~ " type=locale"
    b ~ '>'
    val v = b.number
    b ~ "</"
    b.symbol
    b ~ '>'
    v
  }

  def reset() = b.reset()
  def reload(d: String) = b.reload(d)
}

/*
object XML {

  // def apply(b: SimpleLexer): XML = {
  //   new XML(b)
  // }
  def apply(
breaker: StringGiver,
transform: StringTransformer,
d: String
): XML = {
    new XML(breaker, transform, d)
  }
}
*/
