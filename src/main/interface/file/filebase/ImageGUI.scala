package freight
package interface
package file
package filebase


import swing._
import event._

import java.nio.file.Path

import  interface.gui.jswing._
//TopWinSwingApplication

//TODO: This should have a delete button too...
// ....and a visible id field.
// ...and it doesn't need the tabs
object ImageGUI {

  /*
   import freight._
   import freight.interface.file.fileBase.ImageGUI
   ImageGUI.test
   */
  def test  {

    val (conn, coll) = SyncBinaryCollection.testColl
    interface.gui.jswing.SingleImageCollectionGUI(
   windowTitle = "Test (binary base)",
    coll,
    conn.close
  )
  }


  def apply(
  conn: SyncConnection,
    collectionName: String
  )
  {



    val coll = conn.binaryCollection(collectionName)

    interface.gui.jswing.SingleImageCollectionGUI(
   windowTitle = " (binary base)",
    coll,
    conn.close
  )

}


}//ImageGUI
