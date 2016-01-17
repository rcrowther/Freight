package freight
package core
package objects


final class WeighedTitleString (
  val id: Long,
  val title: String,
  val description: String,
  val weight: Int,
  val value: String
)
extends WeighedTitleBase
{

  override def toString()
      : String =
  {
    val b = new StringBuilder("WeighedTitleString(id:")
    b append id
    b ++= ", title:"
    b append title
    b ++= ", description:"
    b append description
    b ++= ", weight:"
    b append weight
    b ++= ", value:"
    b append value
    b += ')'
    b.result
  }
}


object WeighedTitleString
extends CompanionMeta
{
  private val emptyThing = new WeighedTitleString(0,"","", 0, "")
  
  def empty = emptyThing

  def titleString : String = "WeighedTitleString"
  def machineTitle : String = "wt_string"

val descriptiveGiver: DescriptiveGiver = take _

def take(t: DescriptiveTaker) {
    t.long("id", "the id of this object", LongT)
    t.string("title", "the title", StringT)
    t.string("description", "the description of the title", StringT)
    t.int("weight", "ordering of the title relative to others", StringT)
    t.string("value", "the value of the string", StringT)
}

  val stringFieldBridge : StringFieldBridge = stringFieldBrdge

  def stringFieldBrdge(t: Taker, g: Giver) {
    t.longStr("id", g.longStr)
    t.stringStr("title", g.stringStr)
    t.stringStr("description", g.stringStr)
    t.intStr("weight", g.intStr)
    t.stringStr("value", g.stringStr)
  }



  val multiFieldBridge : MultiFieldBridge = multiFieldBrdge

  def multiFieldBrdge(t: Taker, g: Giver) {
    t.long("id", g.long)
    t.string("title", g.string)
    t.string("description", g.string)
    t.int("weight", g.int)
    t.string("value", g.string)
  }

  implicit def multiTakerFieldSelector: MultiTakerFieldSelect[WeighedTitleString] = multiTakerFieldSelect

  def multiTakerFieldSelect(t: Taker, o: WeighedTitleString) {
    t.long("id", o.id)
    t.string("title", o.title)
    t.string("description", o.description)
    t.int("weight", o.weight)
    t.string("value", o.value)
  }

  implicit def giverToObject: Take[WeighedTitleString] = take

  def take(g: Giver)
      : WeighedTitleString =
  {
    new WeighedTitleString (
      g.long(),
      g.string(),
      g.string(),
      g.int(),
      g.string()
    )
  }


  def apply(
    id: Long,
    title: String,
    description: String,
    weight: Int,
    value: String
  )
      : WeighedTitleString =
  {
    new WeighedTitleString (id, title, description, weight, value)
  }

}//WeighedTitleString
