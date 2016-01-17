package freight
package interface
package gui
package jswing
package component
package draggable

import java.awt.datatransfer._



/** Transfers freight ids through Java drag and drop.
  *
  * Ensures a little type-safety - can't drop strings from other
  * places, for example.
  *
  * Use `IdTransferable.indexFlavor` for identification of the flavor.
  *
  * The `Long` value will easily cast to `Int`, if necessary.
  */
class IndexTransferable(val idx: Int)
    extends Transferable
{

  def getTransferDataFlavors()
      : Array[DataFlavor] =
  {
    IndexTransferable.flavors
  }

  def isDataFlavorSupported(fl: DataFlavor)
      : Boolean =
  {
    fl == IndexTransferable.indexFlavor()
  }

  def getTransferData(fl: DataFlavor)
      : Object =
  {
    if (!isDataFlavorSupported(fl)) null
    else idx.asInstanceOf[Object]
  }

}//IndexTransferable



object IndexTransferable {

  private[this] val indexFlavorThing: DataFlavor = new DataFlavor(
    Int.getClass,
    "Swing Component Index"
  )
  
  def indexFlavor() : DataFlavor = indexFlavorThing

  val flavors: Array[DataFlavor] = Array[DataFlavor]( indexFlavor() )

  def apply(idx: Int)
    : IndexTransferable = new IndexTransferable(idx)

}//IndexTransferable
