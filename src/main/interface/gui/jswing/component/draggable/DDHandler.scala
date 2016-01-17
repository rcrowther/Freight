package freight
package interface
package gui
package jswing
package component
package draggable


import javax.swing._
import java.awt.datatransfer._



/** Tansfer handler for the table rows in this class.
  *  
  * The handler is designed to be added as a new handler to every row
  * where it is used.
  * 
  * @param findTableRow required to seek the row index of Scala
  *  components when given AWT components.
  * @param model required to perform actions on a sucessful drop.
  */
// TODO: Custom flavour
class DDHandler(
  findTableRow: (java.awt.Component) => Option[Int],
  emitRowMoved: (Int, Int, Int) => Unit
)
    extends TransferHandler
{

  // test drop
  override def canImport(info: TransferHandler.TransferSupport)
      : Boolean =
  {

    // Check for String flavor
    //(info.isDataFlavorSupported(DataFlavor.stringFlavor))

    // for the demo, we'll only support drops (not clipboard paste)
    if (!info.isDrop())  false
    else {
      // we only import Strings
      //info.isDataFlavorSupported(DataFlavor.stringFlavor)
      info.isDataFlavorSupported(IndexTransferable.indexFlavor)
    }
  }

  // import data on drop
  override def importData(info: TransferHandler.TransferSupport)
      : Boolean =
  {

    if (!canImport(info)) false
    else {
      //log(s"import data canImport:${canImport(info)}")
      findTableRow(info.getComponent()) match {
        case None => { log("no component matched?"); false }
        case Some(dstRow) => {
//getDropLocation()
//log("where:" + info.getDropLocation().getDropPoint())
//log("row pos:" + info.getComponent().location)
 val xPos = info.getDropLocation().getDropPoint().x
          // Fetch the Transferable and its data
          val t: Transferable = info.getTransferable()
          val data = t.getTransferData(IndexTransferable.indexFlavor)
          val srcRow = data.asInstanceOf[Int]
          //log(s"  move from: $srcRow to:$dstRow")
          emitRowMoved(srcRow, dstRow, xPos)
          true
	    }
      }
    }
  }

  // Test source Export actions
  override def getSourceActions(c: JComponent)
      : Int =
  {
    //log("source actions")
    TransferHandler.MOVE
  }


  // get source export data
  override def createTransferable(c: javax.swing.JComponent)
      : Transferable =
  {
    //val srcRowAsStr = findTableRow(tableColl, c).get.toString
    //log(s"  createTransferable:$srcRowAsStr")
    //?
    // new StringSelection(srcRowAsStr)
    val idxO = findTableRow(c)
    if (idxO == None) error("Component not found in table")
    else IndexTransferable(idxO.get)
  }


  // Export cleanup
  // Nothing. Cleanup.
  override def exportDone(c: JComponent, t: Transferable, action: Int)
  {
    //log("exportdone")
  }

}// DDHandler
