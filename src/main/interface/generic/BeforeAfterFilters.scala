package freight.interface.generic

//import collection.mutable.Seq

import scala.collection.JavaConverters._
//import java.util.concurrent.ConcurrentHashMap
//import scala.collection.concurrent.{Map => ConcurrentMap}

/** Maps keys to sequences of PartialRoutes.
  *
  * Collects two sets of routed callbacks, specifically for handling before/after filters in Connections.
  * The two separate Filter sets can contain keys for the same routes.
  * 
  *  It is expected this class will be used in an
  *  Immutable-On-Construction pattern, thus no delete or update
  *  methods are provided.
  */ 
//TODO: unused? If in timer, remove, all before/after filers are routed?
class BeforeAfterFilters 
//extends BeforeAfterGen[() => Any]
{
  type NullCallback = () => Any

  private var beforeSeq : Seq[NullCallback] = Vector.empty
  private var afterSeq : Seq[NullCallback] = Vector.empty

  /**
    * Appends a filter to the sequence of before filters.
    */
  def appendBefore(callback: NullCallback) = beforeSeq :+= callback
  

  /** Returns the before callbacks for a given key and route.
    */
  def collectBeforeCallbacks()
      : Seq[NullCallback] = beforeSeq

  /**
    * Appends a filter to the sequence of after filters.
    */
  def appendAfter(route: NullCallback) = afterSeq :+= route

  /**
    * Returns the after callbacks for a given key and route.
    */
  def collectAfterCallbacks()
      : Seq[NullCallback] = afterSeq
  


  /** Appends a count of before callbacks to a string builder.
    */ 
  def addBeforeString(sb : StringBuilder)
      : StringBuilder =
  {
    sb ++= "callback count: "
    sb ++= beforeSeq.size.toString
    sb
  }

  /** Appends a count of after callbacks to a string builder.
    */
  def addAfterString(sb : StringBuilder)
      : StringBuilder =
  {
    sb ++= "callback count: "
    sb ++= afterSeq.size.toString
    sb.append(")")
    sb
  }
  

}//RoutedBeforeAfterFilters
