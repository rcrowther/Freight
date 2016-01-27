package freight
package interface
package gui
package jswing


import collection.mutable.Buffer

import swing._
import event._

import gui.generic._



/** View for single objects.
  *
  * A freight-based view. Handles all actions.
  *
  *
  * @define obj object
  *
  * @param showIdField if true, the id field and label will show,
  *  otherwise they are absent from the display (but not the
  *  background model).
  * @param meta supplies a descriptive giver
  * to populate gui fields.
  */
// TODO: Should maybe send resize signals - consider expanding textboxes?
// TODO: can loose the coll parameter.
class ObjectReader(
  val showIdField: Boolean,
  val notifyOfStatus: Boolean,
  val meta: CompanionMeta,
// deprecate
  val reader: (Long, (Giver) => Unit) => Boolean
)
    extends BoxPanel(Orientation.Vertical)
    with FreightObjectReader
with core.collection.PluggableGiverReadable
with core.collection.PluggableGiverLoadable
{


  //----------------
  //-- Components --
  //----------------

  // build the gui and model(s)
  private val t = new LayoutMultiTaker(showIdField)
  meta.descriptiveGiver(t)



  // getters and setters
  protected val model: ObjectModel = t.model()

  // widgets themselves
  private val objWidgets = t.widgets()




  //----------
  //-- Init --
  //----------

  def selectedId: Option[Long] = model.idAsLong
  def setSelectedId(oidO: Option[Long]) = model.id(oidO)

// deprecate
  def data(f: (Giver) => Unit)
  {
f(model.giver)
}

  def writeTo(f: (Giver) => Unit)
: Boolean =
  {
f(model.giver)
true
}

  def loadFrom(
    id: Long,
    f: (Long, (Giver) => Unit) => Boolean
  )
 : Boolean =
{
var ok = true
// Not ok is an exception...
try {
f(
id,
 (g:Giver) => { meta.stringFieldBridge(model.taker, g)}
) 
true
}
catch {
case e: Exception =>
warning("Unable to load object from giver", "ObjectReader")
false
}
}



  //------------------
  //-- Data actions --
  //------------------
// deprecate
  protected def readData(oid: Long)
      : Boolean =
  {
    reader(oid, (g:Giver) => {meta.stringFieldBridge(model.taker, g)} )
  }

// deprecate
  def load(g: Giver)
  {
    meta.stringFieldBridge(model.taker, g)
  }

  //--------------------
  //-- Action helpers --
  //--------------------

  def enable() {
    model.enable()
  }

  def disable() {
    model.disable()
  }

  /** Sets an action on the id field.
    *
    * A massive, non-templated fudge which should not be relied upon
    * outside the jswing interface.
    */
  // NB: Todo this systemicly, the model must return the component, or
  // enable where it can?
  def idAction(f:() => Unit){
    val action: Action = new Action("id edit done") {
      def apply(): Unit = f()
    }

    val cmpt: TextField = contents(1).asInstanceOf[TextField]
    cmpt.action=(action)
  }



  //-------------
  //-- Methods --
  //-------------

  def clearNonId(){
    model.clearNonId()
  }

  def clear(){
    model.clear()
  }



  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------


  objWidgets.foreach{ w =>
    contents.append(w)
  }
  contents.append(verticalSpacer)


}//ObjectReader




object ObjectReader {


  def apply(
    showIdField: Boolean,
    notifyOfStatus: Boolean,
    meta: CompanionMeta,
    collection: GiverTakable
  )
      : ObjectReader =
  {
    new ObjectReader(
      showIdField,
      notifyOfStatus,
      meta,
      collection.apply
    )
  }



}//ObjectReader
