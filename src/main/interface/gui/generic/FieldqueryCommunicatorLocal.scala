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
class FieldqueryCommunicatorLocal(
  coll : FieldQueryable
)
    extends FieldqueryCommunicator
{


  def idDataRequest(id: Long, fieldIdxs: Seq[Int])
  {
    log(s"read id: $id")
    var b: Tuple3[Long, String, String] = null
    val ok = coll(
      fieldIdxs,
      id,
      (g: Giver) => {
        b = Tuple3(g.long, g.string, g.string)
      }
    )
    if(!ok) opFailResponse("read")
    else idDataResponse(id, b)
  }



  def deleteRequest(id: Long)
  {
    log(s"delete id: $id")
    val ok = coll.-(id)
    if (!ok) opFailResponse("delete")
    else deleteResponse(id)
  }

  def foreachRequest(fieldIdxs: Seq[Int])
  {
    log(s"foreach fieldIdxs: $fieldIdxs")
    val b = Seq.newBuilder[(Long, String, String)]
    val ok = coll.foreach(
      fieldIdxs,
      (g: Giver) => {
        val id = g.long
        val title = g.string
        val desc = g.string
        b += Tuple3(id, title, desc)
      }
    )
    if (!ok) opFailResponse("foreach")
    else foreachResponse(b.result)
  }

  def sizeRequest()
  {
    val s = coll.size()
    sizeResponse(s)
  }


}//FieldqueryCommunicatorLocal
