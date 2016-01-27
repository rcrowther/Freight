package freight
package interface
package db

import freight.core.util.StringUtil


/** Route to a collection.
*
* The route has no consideration of source, nor what it may do when it arraives. It is a reference only. For routes with more detail, see [[Route]].

* Collection routes are internal and strict format. If they fail,
* thrown errors which are unconsidered by freight code.
*/
class CollectionRoute(
  val connectionType: Int,
  val collectionType: Int,
  val collectionName: String
)
{

  def toRL() : String  =
  {
    val b = new StringBuilder
    b++= ConnectionType.toMachineString(connectionType)
    b += '/'
    b++= CollectionType.toMachineString(collectionType)
    b += '/'
    b++= collectionName
    b.result
  }

}//CollectionRoute



object CollectionRoute {

  def apply(
    routeStr: String
  )
      : CollectionRoute =
  {
    val parts = routeStr split '/'

        new CollectionRoute (
          ConnectionType.fromMachineString(parts(0)),
          CollectionType.fromMachineString(parts(1)),
          parts(2)
        )

  }


  def apply(
    connectionType: Int,
    collectionType: Int,
    collectionName: String
  )
      : CollectionRoute =
  {
    new CollectionRoute (
      connectionType,
      collectionType,
      collectionName
    )
  }


}//CollectionRoute
