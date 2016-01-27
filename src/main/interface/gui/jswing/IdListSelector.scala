package freight
package interface
package gui
package jswing

import swing._
import swing.event._

import javax.swing.{Icon, ImageIcon}

import scala.collection.mutable.Buffer

import gui.generic._
import event.NeedsResizing2

import jswing.event.StatusMessage



/** Swing GUI view for ref maps.
  *
  * Offers a set of named elements to choose from (the `maybe` view)
  * and the ability to write a key/value of the working id to a
  * selected id.
  *
  * Writing a selection of none can have various effects, depending on
  * the `keyEnsurer` callback. The usual effect would be to remove the 
  * current key from the `RefMap`.
  * 
  * @define view refmap
  *
  * @param maybeKeyReader callback to read a list of elements
  *  to be potentially mapped, consisting of the id and a
  *  human-readable name. Intended for a `foreach` method.
  * @param removeIdFromMaybe if a refmaps keys are from the same
  *  source as the vals, then inserting a reference to self produces
  * an endless loop. Setting this parameter to true will remove the
  * working id from the selection list.
  * @param currentKeyReader callback to read a list of elements
  *  currently mapped, consisting of the id and a human-readable
  *  name. Intended for a `find` method, or similar.
  * @param keyEnsurer callback will clean existing keys, and replace
  *  with a new key/value set, if any any given.
  * @param closer callback for actions to do on gui closing.
  * @param meta supplies a descriptive giver and string
  *  transformer used to populate gui fields.
  */
// TODO: nEXT JOB, READ THE KEY VALUE
// then the list values
// and update readTo method
// get selectedId working
// ...and reading currently selected
class IdListSelector(
  valueAdvice: String
)
    extends BoxPanel(Orientation.Vertical)
{



  val selectedValues = scala.collection.mutable.Buffer.empty[Long]
  private var valuesList = scala.collection.mutable.Buffer.empty[Long]



  //---------
  //-- API --
  //---------

  def idDataResponse(id: Long, values: Seq[Long])
  {
    resetSelected(values)
    publish(NeedsResizing2())
  }




  //----------------
  //-- Components --
  //----------------

  private val selectedValueView = new ListField[Long](
    multipleSelect = false,
    horizontal = true,
    cellWidth = 32
  )


  private val addValuesButton = new Button("Add")
  private val removeValuesButton = new Button("Remove")

  // pack buttons in a HBox
  private val buttonBar = new BoxPanel(Orientation.Horizontal)
  buttonBar.xLayoutAlignment = 0.0
  buttonBar.contents += addValuesButton
  buttonBar.contents += removeValuesButton


  private val valueView = new IdView()
  // Wire the valueView for ENTER - a jswing speciality
  valueView.idAction(addToSelected)




  //----------------------
  //-- Internal Methods --
  //----------------------


  private def renderSelected() {
    selectedValueView.listData = selectedValues
  }



  private def addToSelected() {
    val selectedIdO = valueView.idAsLong
    // TODO: Reject if empty!

    if (selectedIdO != None) {
      val selectedId = selectedIdO.get
      if (!selectedValues.contains(selectedId)) {
        selectedValues += selectedId
        renderSelected()
        valueView.clear()
      }
    }
  }

  private def removeFromSelected() {
    val selected = selectedValueView.selection.items

    if (!selected.isEmpty) {
      val id = selected(0)


      val idx = selectedValues.indexOf(id)

      if (idx != -1) {
        selectedValues.remove(idx)
        renderSelected()
      }
    }
  }






  //-------------
  //-- Methods --
  //-------------


  def resetSelected(v: Seq[Long]) {
    selectedValues.clear()
    selectedValues ++= v
    renderSelected()
  }


  def clear() {
    valueView.clear()
    resetSelected(Buffer.empty[Long])
  }



  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------


  if (valueAdvice != "") {
    val valueAdviceLabel = stockItalicLabel(valueAdvice)
    contents.append(valueAdviceLabel)
    contents.append(verticalSpacer)
  }

  contents.append(selectedValueView)
  contents.append(verticalSpacer)
  contents.append(buttonBar)
  contents.append(verticalSpacer)
  contents.append(valueView)
  contents.append(verticalSpacer)


  listenTo(
    addValuesButton,
    removeValuesButton
  )

  reactions += {

    case ButtonClicked(`addValuesButton`) =>  {
      addToSelected()
    }

    case ButtonClicked(`removeValuesButton`) =>  {
      removeFromSelected()
    }

  }


}//RefMapEditor

/*
 object RefMapReader {

 /** Creates an refmap reader from a single refmap collection.
 */
 def apply(
 showIdField: Boolean,
 notifyOfStatus: Boolean,
 valueAdvice: String
 )
 : RefMapReader =
 {
 new RefMapReader(
 showIdField,
 notifyOfStatus,
 valueAdvice
 )
 }

 }//RefMapReader
 */
