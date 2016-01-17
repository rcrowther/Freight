package freight
package interface
package complex
package generic

//import core.iface.{ RefMap, FieldQueryCollection }
import core.objects.Title


object Util {

  def ensureMark(
    coll: FieldQueryable,
    collectionId: Long,
    isNew: Boolean
  )
      : Title =
  {
    val maybeMark: Option[Title] = coll.get(collectionId)

    if (maybeMark == None || isNew) {
      // lost/no mark term

      val newMark = Title(
        collectionId,
        "mark",
        "new mark for a set"
      )

      coll.insert(
        collectionId,
        newMark
      )

      if(isNew) {
        log4("New mark created for collection collectionName: ${coll.collectionName} collectionId: $collectionId")
      }
      else {
        warning("This collection has no mark. The mark is restored with default content collectionName: ${coll.collectionName} collectionId: $collectionId", "complex.util.Util")
      }

      newMark
    }
    else maybeMark.get
  }

}//Util
