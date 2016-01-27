package freight
package interface
package gui
package generic




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
trait RefmapAsync
    extends Any
{

  def idDataRequest(id: Long)
  def idDataResponse(id: Long, values: Seq[Long])

  def upsertRequest(id: Long, values: Seq[Long])
  def upsertResponse(id: Long)

  def updateRequest(id: Long, values: Seq[Long])
  def updateResponse(id: Long)


  def deleteRequest(id: Long)
  def deleteResponse(id: Long)

  def distinctKeySizeRequest()
  def distinctKeySizeResponse(size: Long)

  def opFailResponse(method: String)

}//RefMapReader
