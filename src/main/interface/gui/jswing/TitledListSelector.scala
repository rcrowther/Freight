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
class TitledListSelector(
  advice: String
)
    extends BoxPanel(Orientation.Vertical)
{

  private var maybeValues = scala.collection.mutable.Buffer.empty[(Long, String)]




  //---------
  //-- API --
  //---------

  def idDataResponse(id: Long, values: Seq[Long])
  {
clearSelection()
var listIndex = 0
maybeValues.foreach{ case(id, title) =>
if (values.contains(id)) {
    maybeValueView.selection.indices += (listIndex)
}
listIndex += 1
}
  }


   // publish(NeedsResizing2())


  //----------------
  //-- Components --
  //----------------


  private val maybeValueView = new ListField[String](
    multipleSelect = true
  )


  //----------------------
  //-- Internal Methods --
  //----------------------

  private def renderMaybeValues() {
    val vals = maybeValues.map(_._2)
    maybeValueView.listData = vals
  }





  //-------------
  //-- Methods --
  //-------------
  def selectedValues
: Seq[Long] =
{
maybeValueView.selectionIdxs.map{idx => maybeValues(idx)._1}
}

def clearSelection()
{
maybeValueView.clearSelection()
}

 def resetMaybeValues(data: Seq[(Long, String)]) {
//clearSelection()
    maybeValues.clear()
    maybeValues ++= data
    renderMaybeValues()
  }

  def clear() {
//clearSelection()
    maybeValues.clear()
    maybeValueView.listData = Buffer.empty[String]
  }



  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------


  if (advice != "") {
    val adviceLabel = stockItalicLabel(advice)
    contents.append(adviceLabel)
    contents.append(verticalSpacer)
  }

  contents.append(maybeValueView)
  contents.append(verticalSpacer)



}//TitledRefMapEditor


