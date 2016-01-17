package freight
package objects


//val p = new Paper(new Id(5), "en", "Ovid", "poem", new Text("watch, and be watched"))

class Paper(
  // The ID of this paper
  val id: Long,
  // In some encoding?
  val language: String,
  //var created: Int = 0,
  //var uid: Int = 0,
  // traits
  val title: String,
  val description: String,
  val body: String
)
    //extends TakeableMultiDefaulted
   // with TakeableClassOps[Paper]
{

  // Freight
  def give(t: Taker)
  {
    t.long("id", id)
    t.string("language", language)
    t.string("title", title)
    t.string("description", description)
    t.text("body", body)
  }

override def toString()
 : String = 
{
    val b = new StringBuilder()
    b ++= "Paper("
    b ++= "id: "
    b append id
    b ++= ", language: "
    b append language
    b ++= ", title: "
    b append title
    b ++= ", description: "
    b append description
    b ++= ", body: "
    b append body
    b ++= ")"
    b.result()
}

}//Paper


//import freight.core.objects.generic.GenericCompanion

object Paper
    extends TakeableMultiCompanion[Paper]
with freight.core.objects.generic.CompanionMeta
{

  def titleString : String = "Paper"


  val stringFieldBridge : StringFieldBridge = stringFieldBrdge

// Used in file parsing and recovery
  def stringFieldBrdge(t: Taker, g: Giver) {
    t.longStr("id", g.longStr)
    t.stringStr("language", g.stringStr)
    t.stringStr("title", g.stringStr)
    t.stringStr("description", g.stringStr)
    t.textStr("body", g.textStr)
  }



  val multiFieldBridge : MultiFieldBridge = multiFieldBrdge

// Used in file parsing and recovery
  def multiFieldBrdge(t: Taker, g: Giver) {
    t.long("id", g.long)
    t.string("language", g.string)
    t.string("title", g.string)
    t.string("description", g.string)
    t.text("body", g.text)
  }
/*
// Deprecated
// Push objects into merges, etc.
  val stringTakerFieldSelector : StringTakerFieldSelect[Paper] = stringTakerFieldSelect
// Deprecated
  def stringTakerFieldSelect(t: Taker, o: Paper) {
    t.longStr("id", o.id.toString)
    t.stringStr("language", o.language)
    t.stringStr("title", o.title)
    t.stringStr("description", o.description)
    t.textStr("body", o.body)
  }
*/

  implicit def multiTakerFieldSelector: MultiTakerFieldSelect[Paper] = multiTakerFieldSelect

  def multiTakerFieldSelect(t: Taker, o: Paper) {
    t.long("id", o.id)
    t.string("language", o.language)
    t.string("title", o.title)
    t.string("description", o.description)
    t.text("body", o.body)
  }

// Used once in GUI?
// Should be descriptiveTake
// It is *not* a giver
val descriptiveGiver: DescriptiveGiver = take _

def take(t: DescriptiveTaker) {
    t.long("id", "", LongT)
    t.string("language", "the language of the text", StringT)
    t.string("title", "the title of the article", StringT)
    t.string("description", "a description", StringT)
    t.text("body", "the body of the text", StringT)
}


  def apply(
    id: Long,
    language: String,
    title: String,
    description: String,
    body: String
  )
      : Paper =
  {
    new Paper(id, language, title, description, body)
  }

  implicit def multiGivable:  (Giver) => Paper = apply
  // Freight
  def apply(g: Giver)
      : Paper =
  {
    new Paper (
      g.long(),
      g.string(),
      g.string(),
      g.string(),
      g.string()
    )
  }

def testGiver: Giver = {
val a = Array[String]("0", "en", "Browning", "Poem", "When the fight begins within himself, a man's worth something.")
new core.objects.immutable.StringArrayBreaker(a)
}
  // Freight
/*
  def apply(g: Giver)
      : Paper =
  {
    new Paper (
      g.long(),
      g.string(),
      g.string(),
      g.string(),
      g.string()
    )
  }
*/

  def testPaper : Paper = new Paper(1, "en", "Browning", "Poem", "When the fight begins within himself, a man's worth something.")

  def testNewPaper : Paper = new Paper(NullID, "en", "Edgar", "Play", "Yellow, quite beserk.")

}//Paper


