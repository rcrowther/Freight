package freight.interface.generic

//import scala.collection.mutable.MapProxy;
import java.io._
//import freight.core.utilities.MapExternalization
import scala.collection.mutable.Map;
//import scala.collection.mutable.LinearSeq
//import freight.common.MapStringOps
import scala.language.implicitConversions


/**
  * Class for handling stock attributes.
  * 
  * The factory call can take a Seq of attributes with arrow notation.
  * 
  * Must use XHTML formed attributes, attribute minimization is forbidden, must be XHML compliant e.g.
  * not
  *
  * disabled
  * 
  * but,
  * 
  * disabled = "disabled" 
  * 
  * Note that, like a StringBuilder, this is a mutable class. 
  */
//TODO: Needs concat and replace item functions, badly?.
//TODO: Maybe not placced here?
final class Attributes(
  var repr: Map[String, String]
)
    extends Externalizable
{

   def this() = this(Map.empty)

  /** Appends extra class elements.
    * These are concatenated into one string on the key "class"
    */
  def appendClass(elems: String*) {
    val b = new StringBuilder
    var emptyStr = true
    
    if (repr.contains("class")) {
      b.append(repr("class"))
      emptyStr = false
    }

    elems.foreach{ v =>
      if (!emptyStr) { b.append(" ") }
      b.append(v)
      emptyStr = false
    }
    repr.update("class", b.toString)
  }

  /** Appends (X)HTML to a string builder.
    *  The function does not sanitise output. If user data is supplied, wrap in  
    *  [[freight.interfaces.core.Utils.escape(text: String)]].
    *  
    * Attributes with empty values are ignored.
    * @return A string of attributes formed from data added. The string always starts with a prefixed space.
    */
  def addHtml(b: StringBuilder)
      : StringBuilder =
  {
    repr.foreach{ case(k, v) =>
      if (!v.isEmpty) {
        b.append(" ")
        b.append(k)
        b.append("=\"")
        b.append(v)
        b.append("\"")
      }
    }
    b
  }

  /** Returns an (X)HTML series of attributes.
    *  The function does not sanitise output. If user data is supplied, wrap in
    *  Utils.escape(text: String). 
    *  
    * Attributes with empty values are ignored.
    * @return A string of attributes formed from data added. The string always starts with a prefixed space.
    */
  def mkHtml()
      : String =
  {
    val sb = new StringBuilder
    addHtml(sb)
    sb.toString
  }
  
  override def toString()
      : String =
  {
    val sb = new StringBuilder
    repr.addString(sb, "Attributes(", ", ", ")")
    sb.toString
  }

  override def writeExternal(out: ObjectOutput)
  {
    out.writeInt(repr.size)
    //println("before:" + repr.size)
    repr.foreach{case(k, v) =>
      out.writeUTF(k)
      out.writeUTF(v)
    }
  }

  override def readExternal(in: ObjectInput)
  {
    val size = in.readInt()
    //println("after:" + size)
    for (i <- 1 to size) {
      repr += (in.readUTF() -> in.readUTF())
    }
  }
}//Attributes


object Attributes {
  def empty: Attributes = new Attributes(Map[String, String]())
  //def empty: Attributes =  MapProxy.this.repr.empty
  implicit def mapToAttributes(repr: Map[String, String]): Attributes = new Attributes(repr)
  implicit def attributesToMap(attributes: Attributes): Map[String, String] = attributes.repr

  def apply(elems: (String, String)*)
      : Attributes = {
    //new Attributes(elems.toMap)
    //new Attributes(elems: _*)
    new Attributes(Map(elems: _*))
  }
}
