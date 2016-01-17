package freight
package core
package objects


class WeighedTitle (
  val id: Long,
  val title: String,
  val description: String,
  val weight: Int
)
extends WeighedTitleBase
{

  override def toString()
      : String =
  {
    val b = new StringBuilder("WeighedTitle(id:")
    b append id
    b ++= ", title:"
    b append title
    b ++= ", description:"
    b append description
    b ++= ", weight:"
    b append weight
    b += ')'
    b.result
  }
}


object WeighedTitle
extends CompanionMeta
{
  private val emptyThing = new WeighedTitle (0,"","", 0)
  
  def empty = emptyThing

  def titleString : String = "WeighedTitle"


val descriptiveGiver: DescriptiveGiver = take _

def take(t: DescriptiveTaker) {
    t.long("id", "", LongT)
    t.string("title", "the title of the text", StringT)
    t.string("description", "the description of the title", StringT)
    t.int("weight", "ordering of the title relative to others", StringT)
}

  val stringFieldBridge : StringFieldBridge = stringFieldBrdge

  def stringFieldBrdge(t: Taker, g: Giver) {
    t.longStr("id", g.longStr)
    t.stringStr("title", g.stringStr)
    t.stringStr("description", g.stringStr)
    t.intStr("weight", g.intStr)
  }



  val multiFieldBridge : MultiFieldBridge = multiFieldBrdge

  def multiFieldBrdge(t: Taker, g: Giver) {
    t.long("id", g.long)
    t.string("title", g.string)
    t.string("description", g.string)
    t.int("weight", g.int)
  }

  implicit def multiTakerFieldSelector: MultiTakerFieldSelect[WeighedTitle] = multiTakerFieldSelect

  def multiTakerFieldSelect(t: Taker, o: WeighedTitle) {
    t.long("id", o.id)
    t.string("title", o.title)
    t.string("description", o.description)
    t.int("weight", o.weight)
  }

  implicit def giverToObject: Take[WeighedTitle] = take

  def take(g: Giver)
      : WeighedTitle =
  {
    new WeighedTitle (
      g.long(),
      g.string(),
      g.string(),
      g.int()
    )
  }

  // TOCONSIDER: Still needed, see taxonomy collection
  def newId(id: Long, t: WeighedTitle)
      : WeighedTitle =
  {
    new WeighedTitle (
      id,
      t.title,
      t.description,
      t.weight
    )
  }

  def apply(
    id: Long,
    title: String,
    description: String,
    weight: Int
  )
      : WeighedTitle =
  {
    new WeighedTitle (id, title, description, weight)
  }

}//WeighedTitle
