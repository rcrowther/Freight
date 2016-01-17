package freight
package interface
package gui
package jswing
package component


import swing._



/** A realised line list row with id and title.
*/
// NB: Very like the one in draggable...
class TitleListRow(
  val id: Long,
  val title: String,
  protected val onUserSelect : (TitleListRow) => Unit
)
    extends BoxPanel(Orientation.Horizontal)
    //with ListRow[TitleListRow]
    with ListRowWithId[TitleListRow]
{



  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------





  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------


  contents += buttonSpacer

  contents += stockLabel(title)
  contents += Swing.HGlue
  contents += selectLabel
  contents += buttonSpacer

}//TitleListRow


