package freight
package interface
package complex
package set

//      core.objects.WeighedTitleString
import core.objects.Title
import collection.mutable.ArrayBuffer

import core.collection._



/** A collection of data.
  * 
  * The elements have no ordering. The elements can be any
  * Freight-enabled object.
  *
  * Set data is held in collections identified by an id. The tables
  * for storing the data are automatically created or opened on
  * instanciation of this class through factory methods.
  *
  * Sets are intended for user/coder element definitions and
  * selections of objects.
  *
  * See also [[Array]], a complex collection with order.
  *
  * @define coll set
  *
  * @param conn the data connection for this $coll.
  * @param coll the collection holding mark data.
  */
class MarksCollection(
  cr: Connector
)
    extends TakableFieldQueryableProxy
{

  val coll: FieldQueryable = cr.marksBase()


  override def -(id: Long)
      : Boolean =
  {

    val ok = coll.-(id)

    // delete the collection elements
    // NB: means little if this works or not (might not exist if
    // unopened), but try.
    cr.removeColl(id)
    ok
  }

}//MarksCollection



object MarksCollection {

  def test: MarksCollection = {
    import freight.interface.db.sqliteJDBC.SyncConnection

    val db = SyncConnection.testDB
    MarksCollection(
      db
    )
  }




  /** Creates/opens a $coll marks collection from a connection.
    *
    * @param conn an interface connection.
    * @param collectionId the id of this $coll.
    * @param meta definition of the data type of this collection.
    */
  def apply(
    conn: Connection
  )
      : MarksCollection =
  {

    val cr = new Connector(conn)

    //log(s"opening Set marks collection collectionName:${coll.collectionName}")
    log(s"opening Set marks collection")
    new MarksCollection(
      cr
    )
  }

}//MarksCollection
