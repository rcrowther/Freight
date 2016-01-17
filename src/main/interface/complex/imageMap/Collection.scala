package freight
package interface
package complex
package imageMap

//import core.objects.{Title, Weighed}
//import collection.mutable.ArrayBuffer

//import java.nio.file.{ Path }
import java.nio.channels.{ ReadableByteChannel }
//import java.io.File


import core.collection.TakableFieldQueryableProxy

/*
 import freight._
 import interface.complex.image.Collection

 val st = Collection.test
 st.columnIds("title", false)
 st.isValidBatchIdx(4, 3)
 val bld = new interface.html.ObjectBuilder()
 st.forSlice( fieldIndices = Seq(1,2,3), sortFieldIdx = 2, batchSize = 4, batchIdx = 1, b = bld)
 bld.result()
 */


/** A collection of data.
  * 
  * The elements have no ordering. The elements can be any
  * Freight-enabled object.
  *
  * The mark is cached in memory.
  *
  * Array data is held in collections identified by an id. The tables
  * for storing the data are automatically created or opened on
  * instanciation of this class through factory methods.
  *
  * Arrays are intended for user/coder element definitions and
  * selections of objects.
  *
  * @define collection array
  *
  * @param collectionId the id of this $collection.
  */
class Collection(
  cr: Connector,
  collectionId: Long,
  meta: CompanionMeta
)
{

  val coll = cr.dataBaseCollection(collectionId, meta)
  val imageColl: BinaryTakable = cr.imageBaseCollection(collectionId)

  def ^(
id: Long,
dataReader: core.collection.PluggableGiverReadable,
imageReader: core.collection.PluggableBinaryReadable
)
      : Boolean =
  {
    var ok = true
ok &= dataReader.writeTo(id, coll.^)
ok &= imageReader.writeTo(id, imageColl.^)
    ok
  }

  def +(
dataReader: core.collection.PluggableGiverReadable,
imageReader: core.collection.PluggableBinaryReadable
)
      : Option[Long] =
  {
    var ok = true
    var newId: Long = dataReader.writeTo(coll.+ _)
ok &= imageReader.writeTo(newId, imageColl.^)
    if (ok) Some(newId)
    else None
  }


  def apply(
id: Long,
dataLoader: core.collection.PluggableGiverLoadable,
imageLoader: core.collection.PluggableBinaryLoadable
)
      : Boolean =
  {
    var ok = true
    ok &= dataLoader.loadFrom(id, coll.apply)
    ok &= imageLoader.loadFrom(id, imageColl.apply)
    ok
  }


   def ~(
id: Long,
dataReader: core.collection.PluggableGiverReadable,
imageReader: core.collection.PluggableBinaryReadable
)
      : Boolean =
  {
    var ok = true
ok &= dataReader.writeTo(id, coll.~)
ok &= imageReader.writeTo(id, imageColl.~)
    ok
  }

  def -(id: Long)
      : Boolean =
  {
    var ok = true
    ok &= coll.-(id)
    ok &= imageColl.-(id)
    ok
  }

   def size = coll.size

}//Collection



object Collection {

  /*
   def test: TakableFieldQueryable = {
   import freight.interface.db.sqliteJDBC.SyncConnection

   val db = SyncConnection.testDB
   val imageCollPath = interface.file.binaryBase.SyncCollection.testPath()

   apply(
   db,
   1,
   core.objects.Image
   )
   }

   */

  /*
   /** Creates/opens a $coll marks collection from a connection.
   *
   * @param conn an interface connection.
   * @param collectionId the id of this $coll.
   * @param meta definition of the data type of this collection.
   */
   def apply(
   conn: Connection,
   collectionId: Long,
   meta: CompanionMeta
   )
   : TakableFieldQueryable =
   {

   val dataColl = conn.fieldQueryCollection(
   s"f_imageset_$collectionId",
   meta
   )

   val imageColl = file.binaryBase.SyncCollection(
   new File("/home/rob/Code/scala/freight/src/test/interface/file/binaryBase").toPath
   )



   //log(s"opening Set marks collection collectionName:${coll.collectionName}")
   log(s"opening ImageSet collection")
   new Collection(
   dataColl,
   imageColl
   )
   }
   */
}//Collection
