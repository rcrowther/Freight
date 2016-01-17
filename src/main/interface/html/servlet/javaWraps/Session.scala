package freight.interface.html.servlet
package javaWraps


import javax.servlet.http.HttpSession

/** Provides controlled persistence between Http requests.
  *  
  * `Session` is a Scala representation of Java's
  * `javax.servlet.http.HttpSession`. It wraps the object in a more
  * Scalalike API, and provides all the operations found in a map.
  */
case class Session(session: HttpSession)
    extends AttributesMap
    //Not yet, this includes the attributes map.
   // with freight.common.StringOps
{
  override val stringPrefix = "Session"
    
  protected def attributes = session
  
  
  def id = session.getId

  override def addString(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "id:"
    b ++= id
    b ++= ", CreationTime:"
    b ++= session.getCreationTime().toString
    b ++= ", LastAccessedTime:"
    b ++= session.getLastAccessedTime().toString
    b ++= ", MaxInactiveInterval:"
    b ++= session.getMaxInactiveInterval().toString
    b ++= ", attributes("
    b ++= this.mkString(", ")
    b += ')'
    b
  }
 /*
    override def toString()
  : String =
  	{
   val b = new StringBuilder
   b ++= "Session("
   addString(b)
   b += ')'
   b.toString
 }
*/ 
}//Session
