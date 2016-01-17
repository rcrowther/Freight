package freight
package core
package objects


class Title (
  val id: Long,
  val title: String,
  val description: String
)
extends TitleBase
{

  override def toString()
      : String =
  {
    val b = new StringBuilder("Title(id:")
    b append id
    b ++= ", title:"
    b append title
    b ++= ", description:"
    b append description
    b += ')'
    b.result
  }
}


object Title
extends CompanionMeta
{
  private val emptyThing = new Title (0,"","")
  
  def empty = emptyThing

  def titleString : String = "Title"


val descriptiveGiver: DescriptiveGiver = take _

def take(t: DescriptiveTaker) {
    t.long("id", "", LongT)
    t.string("title", "the title of the text", StringT)
    t.string("description", "the description of the title", StringT)
}

  val stringFieldBridge : StringFieldBridge = stringFieldBrdge

  def stringFieldBrdge(t: Taker, g: Giver) {
    t.longStr("id", g.longStr)
    t.stringStr("title", g.stringStr)
    t.stringStr("description", g.stringStr)
  }



  val multiFieldBridge : MultiFieldBridge = multiFieldBrdge

  def multiFieldBrdge(t: Taker, g: Giver) {
    t.long("id", g.long)
    t.string("title", g.string)
    t.string("description", g.string)
  }

  implicit def multiTakerFieldSelector: MultiTakerFieldSelect[Title] = multiTakerFieldSelect

  def multiTakerFieldSelect(t: Taker, o: Title) {
    t.long("id", o.id)
    t.string("title", o.title)
    t.string("description", o.description)
  }

  implicit def giverToObject: Take[Title] = take

  def take(g: Giver)
      : Title =
  {
    new Title (
      g.long(),
      g.string(),
      g.string()
    )
  }


  def apply(
    id: Long,
    title: String,
    description: String
  )
      : Title =
  {
    new Title (id, title, description)
  }

}//Title
