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
trait RefmapCommunicator
    extends RefmapAsync
{
  var idDataFunc : Option[(Long, Seq[Long]) => Unit] = None
  var upsertFunc : Option[(Long) => Unit] = None
  var updateFunc : Option[(Long) => Unit] = None
  var deleteFunc : Option[(Long) => Unit] = None
  var sizeFunc : Option[ (Long) => Unit] = None
  var opFailFunc : Option[(String) => Unit] = None



  def registerIdDataResponse(f: (Long, Seq[Long]) => Unit)
  { idDataFunc = Some(f) }
  def idDataResponse(id: Long, values: Seq[Long])
  {
    elErrorIf(
      idDataFunc == None,
      "Unregistered callback call: idDataRequest"
    )
    idDataFunc.get(id, values)
  }


  def registerUpsertResponse(f: (Long) => Unit)
  { upsertFunc = Some(f) }
  def upsertResponse(id: Long)
  {
    elErrorIf(
      upsertFunc == None,
      "Unregistered callback call: upsertRequest"
    )
    upsertFunc.get(id)
  }


  def registerUpdateResponse(f: (Long) => Unit)
  { updateFunc = Some(f) }
  def updateResponse(id: Long)
  {
    elErrorIf(
      updateFunc == None,
      "Unregistered callback call: updateRequest"
    )
    updateFunc.get(id)
  }

  def registerDeleteResponse(f: (Long) => Unit)
  { deleteFunc = Some(f) }
  def deleteResponse(id: Long)
  {
    elErrorIf(
      deleteFunc == None,
      "Unregistered callback call: deleteRequest"
    )
    deleteFunc.get(id)
  }

  def registerSizeResponse(f: (Long) => Unit)
  { sizeFunc = Some(f) }
  def sizeResponse(s: Long)
  {
    elErrorIf(
      sizeFunc == None,
      "Unregistered callback call: distinctKeySizeRequest"
    )
    sizeFunc.get(s)
  }


  def registerOpFailResponse(f: (String) => Unit)
  { opFailFunc = Some(f) }
  def opFailResponse(method: String)
  {
    elErrorIf(
      opFailFunc == None,
      "Unregistered callback callback: opFailResponse"
    )
    opFailFunc.get(method)
  }

}//RefmapController
