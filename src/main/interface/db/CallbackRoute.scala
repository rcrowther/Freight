package freight
package interface
package db

import freight.core.util.StringUtil



class CallbackRoute(
  val collectionRoute: CollectionRoute,
  val method: String,
val callback: AnyRef
)
{

}//CallbackRoute



object CallbackRoute {


  def apply(
   collectionRoute: CollectionRoute,
   method: String,
    callback: AnyRef
  )
      : CallbackRoute =
  {
    new CallbackRoute(
     collectionRoute,
method,
    callback
    )
  }
/*
  def apply(
    connectionType: Int,
    collectionType: Int,
    collectionName: String
  )
      : CallbackRoute =
  {
    new CallbackRoute (
      connectionType,
      collectionType,
      collectionName,
      ""
    )
  }
*/

}//CallbackRoute
