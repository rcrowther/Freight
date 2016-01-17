package freight
package interface
package complex
package set

import core.objects.Title
import scala.collection.mutable.ArrayBuffer


import core.collection._

/*
 import freight._
 import interface.complex.array.Collection

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
class ElementCollection(
  cr: Connector,
  collectionId: Long,
  meta: CompanionMeta
)
    extends TakableFieldQueryableProxy
{

  val coll: FieldQueryable = cr.elementsBase(collectionId, meta)

}//ElementCollection



object ElementCollection {

  def test: ElementCollection = {
    import freight.interface.db.sqliteJDBC.SyncConnection

    val db = SyncConnection.testDB
    apply(
      db,
      1,
      core.objects.Title
    )
  }




  /** Creates/opens a set element collection from a connection.
    *
    * The programmer
    * must ensure the collection is given an appropriate `meta`
    * for the generated element collection.
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
      : ElementCollection =
  {

    val cr = new Connector(conn)

    //log(s"opening Set collection collectionName:${coll.collectionName}")
    log(s"opening Set collection")
    new ElementCollection(
      cr,
      collectionId,
      meta
    )
  }

}//ElementCollection

