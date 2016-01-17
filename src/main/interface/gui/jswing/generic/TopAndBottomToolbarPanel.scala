package freight
package interface
package gui
package jswing
package generic


import swing._
import event._



import gui.generic._



/** A list of items with delete button.
  *
  * Handles most of the button action and display. Will need a method
  * to populate, and `pack`ing on element append/remove.
  *
  * @param advice a message at the top of the list.
  * @param cmpt a component to place under the toolbar.
  */
class TopAndBottomToolbarPanel(
  advice: String,
  cpmt: Component
)
    extends BoxPanel(Orientation.Vertical)
{

  val topbar = new SimpleToolbar
  //val createButton = topbar.space.button("As New")

  private val adviceLabel = stockLabel(advice)
  adviceLabel.font = italicFont


  val bottombar = new SimpleToolbar
  //val deleteButton = bottombar.glue.button("Delete")

  //bottombar.border = debugBorder



  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------

  contents.append(topbar)
  contents.append(centred(adviceLabel))
  contents.append(centred(cpmt))
  contents.append(verticalSpacer)
  contents.append(bottombar)
  contents.append(verticalSpacer)


}//CreateDeletePanel



