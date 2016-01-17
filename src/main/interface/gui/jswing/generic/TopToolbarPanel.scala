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
  * @param cmpt a component to place under the toolbar.
  */
class TopToolbarPanel(
  cpmt: Component
)
    extends BoxPanel(Orientation.Vertical)
{

  val topbar = new SimpleToolbar


  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------

  contents.append(topbar)
  contents.append(centred(cpmt))
  contents.append(verticalSpacer)


}//CreateDeletePanel



