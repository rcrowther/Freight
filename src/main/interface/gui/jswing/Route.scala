package freight
package interface
package gui
package jswing

import freight.core.util.StringUtil

class Route(
  val databaseGenre: String,
  val collectionType: String,
  val collectionName: String,
  val action: String
)
{

  def toRL() : String  =
  {
    val b = new StringBuilder
    b++= databaseGenre
    b += '/'
    b++= collectionType
    b += '/'
    b++= collectionName
    if(!StringUtil.isBlank(action)) {
      b += '/'
      b++= action
    }
    b.result
  }

}//Route



object Route {

  def apply(
    routeStr: String
  )
      : Option[Route] =
  {
    val parts = routeStr split '/'
    if (
      (parts.size == 3 || parts.size == 4)
        && !StringUtil.isBlank(parts(0))
        && !StringUtil.isBlank(parts(1))
        && !StringUtil.isBlank(parts(2))
    ) {
      val actionPart =
        if (parts.size == 3) ""
        else parts(3)
      Some(
        new Route (
          parts(0),
          parts(1),
          parts(2),
          actionPart
        )
      )
    }
    else None
  }

  def apply(
    databaseGenre: String,
    collectionType: String,
    collectionName: String,
    action: String
  )
      : Route =
  {
    new Route (
      databaseGenre,
      collectionType,
      collectionName,
      action
    )
  }

  def apply(
    databaseGenre: String,
    collectionType: String,
    collectionName: String
  )
      : Route =
  {
    new Route (
      databaseGenre,
      collectionType,
      collectionName,
      ""
    )
  }


}//Route
