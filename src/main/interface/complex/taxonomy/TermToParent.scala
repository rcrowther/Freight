package freight
package interface
package complex
package taxonomy


final class TermToParent (
    val tid: Long,
    val parent: Long
  )
{

def give(t: Taker) {
    t.long("tid", tid)
    t.long("parent", parent)
}

  override def toString()
      : String =
  {
    val b = new StringBuilder("Term(tid:")
    b append tid
    b ++= ", parent:"
    b append parent
    b += ')'
    b.result
  }
}

object TermToParent
//extends  TakeableMultiCompanion[Term]
 {

  // Freight
  def apply(g: Giver)
      : TermToParent =
  {
    new TermToParent (
      g.long(),
     g.long()
    )
  }

}
