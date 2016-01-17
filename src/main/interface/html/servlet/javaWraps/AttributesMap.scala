package freight.interface.html.servlet
package javaWraps

import scala.language.implicitConversions
import scala.language.reflectiveCalls

import scala.collection.mutable.Map
import scala.collection.JavaConverters._
import java.util.Enumeration

//import util.MutableMapWithIndifferentAccess



/** Adapts servlet objects to Scala mutable Maps.
  * 
  * Some examples of objects so modified are ServletRequest, HttpSession, and ServletContext.
  */
//TODO: Core material?
abstract class AttributesMap
    extends Map[String, Any]
//with MutableMapWithIndifferentAccess[Any]
{
  // The servlet interface objects have no underlying interface,
  // AFAIK so, like it or not, this must be structural typing.
  type JavaAttributes = {
    def getAttribute(name: String): AnyRef
    def getAttributeNames(): java.util.Enumeration[String]
    def setAttribute(name: String, value: AnyRef): Unit
    def removeAttribute(name: String): Unit
  }
  
  protected def attributes : JavaAttributes

  /**
    * Optionally returns the attribute associated with the key.
    *
    * @return an option value containing the attribute associated with the key
    * in the underlying servlet object, or None if none exists.
    */
  //TODO: Could or should there be an apply?
  def get(key:String): Option[Any] = {
    if (attributes == null) None
    else {
      attributes.getAttribute(key) match {
        case null => None
        case v => Some(v)
      }
    }
  }
  
  def getAs[A](key:String): Option[A] = {
    if (attributes == null) None
    else {
      attributes.getAttribute(key) match {
        case null => None
        case v => Some(v.asInstanceOf[A])
      }
    }
  }
  
  /**
    * Creates a new iterator over all attributes in the underlying object.
    *
    * @return the new iterator
    */
  def iterator: Iterator[(String, Any)] =
    attributes.getAttributeNames().asScala map { key =>
      (key, attributes.getAttribute(key))
    }

  /**
    * Sets an attribute on the underlying object.
    *
    * @param kv the key/value pair.  If the value is null, has the same effect
    * as calling `-=(kv._1)`.
    *
    * @return the map itself
    */
  def +=(kv: (String, Any)) = {
    attributes.setAttribute(kv._1, kv._2.asInstanceOf[AnyRef])
    this
  }

  /**
    * Removes an attribute from the underlying object.
    *
    * @param key the key to remove
    *
    * @return the map itself
    */
  def -=(key: String) = {
    attributes.removeAttribute(key)
    this
  }
}
