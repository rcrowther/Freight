package freight
package interface
package complex
package imageMap

import core.objects.Title
import scala.collection.mutable.ArrayBuffer


import core.collection.FieldQueryable

import java.nio.channels.ReadableByteChannel

class Connector(
  val conn: Connection,
  val imageConn: Connection
)
    extends generic.GenConnector
{

  def classString : String = "ImageMap"

  def machineClassString : String = "imagemap"


  def dataBaseCollection(
    collectionId: Long,
    meta: CompanionMeta
  )
      : TakableFieldQueryable =
  {
    fieldQueryableCollection(
      namedIdCollectionName("data", collectionId),
      meta
    )
  }

  def collection(
    collectionId: Long,
    meta: CompanionMeta
  )
      : Collection =
  {
    new Collection(
      this,
      collectionId,
      meta
    )
  }

  def imageBaseCollection(
    collectionId: Long
  )
      : BinaryTakable =
  {

    val coll = imageConn.binaryCollection(
      namedIdCollectionName("image", collectionId)
    )

    isNewNotify(coll.isNew)

    coll
  }


  def removeCollection(
    collectionId: Long
  )
  {
    conn.-(namedIdCollectionName("data", collectionId))
    conn.-(namedIdCollectionName("image", collectionId))
  }

  def close
      : Boolean =
  {
    var ok = true
    ok &= conn.close()
    ok &= imageConn.close()
    ok
  }

}//Connector
