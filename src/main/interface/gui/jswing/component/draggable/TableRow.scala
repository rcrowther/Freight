package freight
package interface
package gui
package jswing
package component
package draggable

import java.awt.event.MouseAdapter
import java.awt.Color
import javax.swing.{TransferHandler, JComponent}
import swing._



class TableRow(
  icon: javax.swing.Icon,
  val id: Long,
  val title: String,
  var indent: Int,
  findTableRow: (java.awt.Component) => Option[Int],
  emitRowMoved: (Int, Int, Int) => Unit,
  protected val onUserSelect: (TableRow) => Unit
)
    extends BoxPanel(Orientation.Horizontal)
    with ListRowWithId[TableRow]
{
  xLayoutAlignment = 0.0

  val indentCompontent = new Dimension(0, 0)

  val iconLabel = stockLabel()
  iconLabel.icon = icon


  setIndent(indent)



  /** Produces a tuple of the id and indent.
    */
  def data(): (Long, Int) = {
    (id, indent)
  }

  /** Set the indent of the draggable handle and title.
    */
  def setIndent(i: Int){
    indent = i
    indentCompontent.setSize(new Dimension(i * 20, 0))
    // NB: All in one revalidation doesn't work...
    // do it here
    revalidate()
  }
  




  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------

  peer.setTransferHandler(new DDHandler(findTableRow, emitRowMoved))

  val th = peer.getTransferHandler()

  val dt = peer.getDropTarget()
  //log(s"drop target isActive: ${dt.isActive()}")

  peer.addMouseListener(new MouseAdapter() {
    override def mousePressed(e: java.awt.event.MouseEvent) {
      val c : JComponent =  e.getSource().asInstanceOf[JComponent]
      val th: TransferHandler = c.getTransferHandler()
      th.exportAsDrag(c, e, TransferHandler.MOVE)
      println("pressed!")
    }

  })



  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------


  contents += buttonSpacer
  contents += Swing.RigidBox(indentCompontent)
  contents += iconLabel
  contents += stockLabel(title)
  contents += Swing.HGlue
  contents += buttonSpacer
  contents += selectLabel
  contents += buttonSpacer

}//TableRow


