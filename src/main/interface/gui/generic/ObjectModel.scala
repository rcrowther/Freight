package freight
package interface
package gui
package generic


/** Functional model of an object.
*
* This is a model, but it takes and stashes information in the input view.
*/
class ObjectModel(
  fields: Array[FieldModifiableComponent]
)
{

//TODO: Unused?
def idField : FieldModifiableComponent = {
fields(0)
}

  //NB: Single threaded, so we may as well keep a taker around
  private val resusableModelTaker = new ObjectModelTaker(fields)


  def taker: Taker = {
    resusableModelTaker.reset()
    resusableModelTaker
  }

  //NB: Single threaded, so we may as well keep a giver around
  private val resusableGiver = new ObjectModelGiver(fields)

  def giver : ObjectModelGiver = {
    resusableGiver.reset()
    resusableGiver
  }

  def disableNonId() {
    fields.drop(1).foreach(_.disable)
  }

  def disable() {
    fields.foreach(_.disable)
  }


  def enableNonId() {
    fields.drop(1).foreach(_.enable)
  }

  def enable() {
    fields.foreach(_.enable)
  }

  /** Clear the gui.
    *
    * Ignores the id field.
    */
  def clearNonId() {
    fields.drop(1).foreach(_.set(""))
  }

  /** Clear the gui.
    *
    * See also `clearNonId`.
    */
  def clear() {
    fields.foreach(_.set(""))
  }

  /** Clear the id field.
    *
    * See also `clearNonId`.
    */
  def clearId() {
    fields(0).set("")
  }

  def id_=(id: String) = fields(0).set(id)
  def id: String = fields(0).get

def id(id: Option[Long]) = { 
if(id == None) clearId()
else fields(0).set(id.get.toString)
}

  def idAsLong: Option[Long] = idAsOption(fields(0).get)

}//ObjectModel
