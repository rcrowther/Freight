package freight
package interface
package gui
package generic


import scala.collection.mutable.Buffer



/** Split collection for ref maps.
  *
  * Offers a limited set of methods for use with refmaps. The set of
  * methods is chosen to supply GUIs with the data they need.
  *
  * The methods are giver-based and `apply`-type `response` methods
  * may accept data directly, not by the usual method of using
  * methods. This is to facilitate their usage with servers and
  * serializers for wire transmittion.
  *
  * Async views split the main methods into request/response pairs.
  * `request` methods are expected to take little time and
  * load. `response` methods are expected to template callbacks.
  * 
  * @define view refmap
  */
class RefmapCommunicatorLocal(
  coll : RefMap
)
    extends RefmapCommunicator
{
  def idDataRequest(id: Long)
  {
    val values = coll(id)
    if(values.isEmpty) opFailResponse("read")
    else idDataResponse(id, values)
  }

  def updateRequest(id: Long, values: Seq[Long])
  {
    log(s"askUpdate id: $id values: $values")
    val ok = coll.~(id, values)
    if (!ok) opFailResponse("update")
    else updateResponse(id)
  }

  def upsertRequest(id: Long, values: Seq[Long])
  {
    log(s"askUpsert id: $id values: $values")
    val ok = coll.~(id, values)
    if (!ok) opFailResponse("upsert")
    else upsertResponse(id)
  }

  def deleteRequest(id: Long)
  {
    log(s"delete id: $id")
    val ok = coll.-(id)
    if (!ok) opFailResponse("delete")
    else deleteResponse(id)
  }

  def sizeRequest()
  {
    val s = coll.size()
    sizeResponse(s)
  }

}//RefMapReader
