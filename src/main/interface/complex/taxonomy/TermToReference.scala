package freight
package interface
package complex
package taxonomy


final class TermToReference (
    val tid: Long,
    val id: Long
  )
{

def give(t: Taker) {
    t.long("tid", tid)
    t.long("id", id)
}

  override def toString()
      : String =
  {
    val b = new StringBuilder("Term(tid:")
    b append tid
    b ++= ", id:"
    b append id
    b += ')'
    b.result
  }
}

object TermToReference
//extends  TakeableMultiCompanion[Term]
 {

  // Freight
  def apply(g: Giver)
      : TermToReference =
  {
    new TermToReference (
      g.long(),
     g.long()
    )
  }

}
