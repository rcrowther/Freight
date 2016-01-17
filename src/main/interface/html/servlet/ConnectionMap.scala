package freight.interface.html.servlet

import collection.mutable.Map
import freight.interface.generic.{RouteSeq, RouteItem}


/** Maps keys to sequences of PartialRoutes.
  *  
  * A is the type of the key.
  * 
  * The mapping enables ganging sets of routes for specific purposes
  * (usually methods on a runnable).  Each different key in the map
  * can carry RouteMatchers for the same route, with no bad effects.
  *  
  * The key can be typed as an Enumeration/StatMap etc. for clarity.
  *   
  * Sequences of before and after callbacks are also provided. These
  * can be loaded with callbacks. As the callbacks are simplictic,
  * they can be supplied from Scala's parameterless closures.
  *  
  * It is expected this class will be used in an
  * Immutable-On-Construction pattern, thus no delete or update
  * methods are provided.
  */ 
//TODO: // Change to cores RoutedBeforeAfter
class ConnectionMap[A]
{

  private val routeMap: Map[A, RouteSeq] = Map[A, RouteSeq]()

  private var beforeSeq = RouteSeq()
  private var afterSeq = RouteSeq()
  
  /** Appends a route to the method's route sequence.
    */
  def += (k: A, ri: RouteItem)
  {
    if (routeMap.contains(k)) {
      //routeMap += (k -> (route +: routeMap(k)))
      routeMap(k) += ri
    }
    else {
      routeMap += (k -> RouteSeq(ri))
    }
  }

  /** Returns the sequence of routes for one key.
    */   
  def apply(k: A): RouteSeq = routeMap(k)

  /** Returns the first callback from a given key and route.
    */
  def find(k: A, route: String)
      : Option[RouteItem] =
  {
    //java.util.NoSuchElementException:
    routeMap(k).findMatch(route)
  }

  /** Appends a filter to the sequence of before filters.
    */
  def appendBefore(route: RouteItem): Unit = beforeSeq += (route)
  
  /** Selects route items from a given key and route.
    */
  //TODO: Can except?
  def filterBefore(route: String)
      : Seq[RouteItem] =
  {
    beforeSeq.filterMatch(route)
  }
  
  /** Appends a filter to the sequence of after filters.
    */
  def appendAfter(route: RouteItem): Unit = afterSeq += (route)
  
  /** Selects route items from a given key and route.
    */
  //TODO: Can except?
  def filterAfter(route: String)
      : Seq[RouteItem] =
  {
    afterSeq.filterMatch(route)
  }
  
  /** Returns a map representation of main routes.
    *  
    *  The keys are method names, the values are sequences of
    *  stringified routes.
    */
  def stringMap()
      : Map[A, Seq[String]] =
  {
    routeMap.map{case(method, riSeq) =>
      (method, riSeq.mapPrettyString)
    }
  }
  
  /** Appends elements from one key of this RouteMap to a StringBuilder
    * using start, end, and separator strings.
    * 
    *  Implemented to avoid adding class context or callback data.
    *  The written text begins with the string `start`, ends with the
    *  string `end`, and elements are separated by the string `sep`.
    */
  def routesAddPrettyString(
    b: StringBuilder,
    k: A,
    start: String,
    sep: String,
    end: String
  )
      : StringBuilder =
  {
    var first = true

    b append start
    for (ri <- routeMap(k)) {
      if (first) {
        ri.matcher.addPrettyString(b)
        first = false
      }
      else {
        b append sep
        ri.matcher.addPrettyString(b)
      }
    }
    b append end
    b
  }
  
  /** Appends all elements of this RouteMap to a string builder using
    *  start, end, and separator strings.
    *  
    *  Implemented to avoid adding class context or callback data.
    *  The written text begins with the string `start` and ends with
    *  the string `end`, and elements are separated by the string
    *  `sep`. These parameter values are used for both the keyed lists
    *  and their inner sequences of routes.  See RouteSeq for a
    *  similar implementation.
    */
  def routesAddPrettyString(
    b: StringBuilder,
    start: String,
    sep: String,
    end: String
  )
      : StringBuilder =
  {
    routeMap.keys.foreach{k =>
      routesAddPrettyString(
        b,
        k,
        start: String,
        sep: String,
        end: String
      )
    }
    b
  }
  

  /** Adds the before filters to a string builder.
    */ 
  def beforeAddPrettyString(sb : StringBuilder)
      : StringBuilder =
  {
    val b = new StringBuilder()
    b.append("ConnectionMapBefore(")
    beforeSeq.addPrettyString(sb, ", ")
    b.append(")")
    b
  }

  /** Adds the after filters to a string builder.
    */
  def afterAddPrettyString(sb : StringBuilder)
      : StringBuilder =
  {
    val b = new StringBuilder()
    b.append("ConnectionMapAfter(")
    afterSeq.addPrettyString(sb, ", ")
    b.append(")")
    b
  }
  
  /** Returns a representation of the main route map as a string.
    */
  override def toString()
      : String =
  {
    val sb = new StringBuilder()
    sb.append("ConnectionMap(")
    sb.append(routeMap)
    sb.append(")")
    sb.toString()
  }
  
}//ConnectionMap
