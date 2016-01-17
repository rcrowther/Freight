package freight
package core
package objects


class Image (
  val id: Long,
  val title: String,
  val description: String,
  val contentLocation: String,
  val author: String,
  val date: Long
)
extends TitleBase
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
    b ++= ", contentLocation:"
    b append contentLocation
    b ++= ", author:"
    b append author
    b ++= ", date:"
    b append date
    b += ')'
    b.result
  }
}


object Image
extends CompanionMeta
{
  private val emptyThing = new Image (0,"","", "", "", 0)
  
  def empty = emptyThing

  def titleString : String = "Image"


val descriptiveGiver: DescriptiveGiver = take _

def take(t: DescriptiveTaker) {
    t.long("id", "", LongT)
    t.string("title", "the title of the text", StringT)
    t.string("description", "the description of the title", StringT)
    t.string("contentLocation", "the description of the title", StringT)
    t.string("author", "the creator of the item", StringT)
    t.long("date", "ordering of the title relative to others", StringT)
}

  val stringFieldBridge : StringFieldBridge = stringFieldBrdge

  def stringFieldBrdge(t: Taker, g: Giver) {
    t.longStr("id", g.longStr)
    t.stringStr("title", g.stringStr)
    t.stringStr("description", g.stringStr)
    t.stringStr("contentLocation", g.stringStr)
    t.stringStr("author", g.stringStr)
    t.longStr("date", g.longStr)
  }



  val multiFieldBridge : MultiFieldBridge = multiFieldBrdge

  def multiFieldBrdge(t: Taker, g: Giver) {
    t.long("id", g.long)
    t.string("title", g.string)
    t.string("description", g.string)
    t.string("contentLocation", g.string)
    t.string("author", g.string)
    t.long("date", g.long)
  }

  implicit def multiTakerFieldSelector: MultiTakerFieldSelect[Image] = multiTakerFieldSelect

  def multiTakerFieldSelect(t: Taker, o: Image) {
    t.long("id", o.id)
    t.string("title", o.title)
    t.string("description", o.description)
    t.string("contentLocation", o.contentLocation)
    t.string("author", o.author)
    t.long("date", o.date)
  }

  implicit def giverToObject: Take[Image] = take

  def take(g: Giver)
      : Image =
  {
    new Image (
      g.long(),
      g.string(),
      g.string(),
      g.string(),
      g.string(),
      g.long()
    )
  }

  def apply(
    id: Long,
    title: String,
    description: String,
    contentLocation: String,
    author: String,
    date: Long
  )
      : Image =
  {
    new Image (
id,
 title,
 description,
contentLocation, 
author, 
date
)
  }

}//Image
