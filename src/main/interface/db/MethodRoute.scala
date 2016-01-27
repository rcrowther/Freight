package freight
package interface
package db

import freight.core.util.StringUtil

class MethodRoute(
  val connectionType: Int,
  val collectionType: Int,
  val collectionName: String,
  val method: String
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
      b += '/'
      b++= method

    b.result
  }

}//MethodRoute



object MethodRoute {

  def apply(
    routeStr: String
  )
      : Option[MethodRoute] =
  {
    val parts = routeStr split '/'
try {
      Some(
        new MethodRoute (
          parts(0).toInt,
          parts(1).toInt,
          parts(2),
          parts(3)
        )
      )
    }
    catch {
case e: Exception => {
warning("Could not split to class routeStr:$routeStr", "MethodRoute")
None
}
}
  }

  def apply(
    connectionType: Int,
    collectionType: Int,
    collectionName: String,
    method: String
  )
      : MethodRoute =
  {
    new MethodRoute(
      connectionType,
      collectionType,
      collectionName,
      method
    )
  }
/*
  def apply(
    connectionType: Int,
    collectionType: Int,
    collectionName: String
  )
      : MethodRoute =
  {
    new MethodRoute (
      connectionType,
      collectionType,
      collectionName,
      ""
    )
  }
*/

}//MethodRoute
