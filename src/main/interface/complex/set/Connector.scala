package freight
package interface
package complex
package set

import core.objects.Title
import scala.collection.mutable.ArrayBuffer


import core.collection.FieldQueryable


class Connector(
  val conn: Connection
)
{

  def marksBase()
      : FieldQueryable =
  {
    val coll = conn.fieldQueryCollection(
      "f_set_marks",
      Title
    )


    if (coll.isNew)
    {
      log(
        //"new Set marks collection collectionName:${coll.collectionName}"
        "new Set marks collection"
      )
    }
    coll
  }

  def marks()
      : TakableFieldQueryable =
  {
    new MarksCollection(this)
  }


  def elementsBase(
    collectionId: Long,
    meta: CompanionMeta
  )
      : FieldQueryable =
  {
    val coll = conn.fieldQueryCollection(
      s"f_set_elements_$collectionId",
      meta
    )


    if (coll.isNew)
    {
      log(
        //"New Set collection collectionName:${coll.collectionName}"
        "new Set collection"
      )
    }
    coll
  }

  def elements(
    collectionId: Long,
    meta: CompanionMeta
  )
      : TakableFieldQueryable =
  {
    new ElementCollection(
      this,
      collectionId,
      meta
    )
  }

  def removeColl(
    collectionId: Long
  )
  {
    conn.-(s"f_set_elements_$collectionId")
  }

  def close
      : () => Boolean =
  {
    conn.close _
  }

}//Connector
