package freight.interface.generic


//import freight.interfaces.core.RouteItem

//import java.util.concurrent.ConcurrentHashMap
//import scala.collection.concurrent.{Map => ConcurrentMap}

/** Maps two sequences of route items, intended for before/after provision.
  * 
  * Collects two sets of routed callbacks, specifically for handling before/after filters in Connections.
  * The two separate Filter sets can contain keys for the same routes.
  * 
  *  It is expected this class will be used in an
  *  Immutable-On-Construction pattern, thus no delete or update
  *  methods are provided.
  */ 
//TODO: Should be in the timer. Moving towards this as the only before/after base
class RoutedBeforeAfterFilters 
//extends BeforeAfterGen[RouteItem]
{
  // private, because one day there may be a builder to make this immutable on construction.
  private var beforeSeq = RouteSeq()
  private var afterSeq = RouteSeq()

  /**
    * Appends a filter to the sequence of before filters.
    */
  def beforeAppend(route: RouteItem) = beforeSeq += (route)
  

  /** Returns the before callbacks for a given key and route.
    */
  //TODO: Can except?
  def beforeFilter(route: String)
      : Seq[RouteItem] =
  {
    beforeSeq.filterMatch(route)
  }
  
  /**
    * Appends a filter to the sequence of after filters.
    */
  def afterAppend(route: RouteItem) = afterSeq += (route)


        /** Selects route items from a given key and route.
    */
  //TODO: Can except?
  def afterFilter(route: String)
      : Seq[RouteItem] =
  {
    afterSeq.filterMatch(route)
  }


}//RoutedBeforeAfterFilters
