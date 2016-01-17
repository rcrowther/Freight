package freight
package interface
package complex
package set


import interface.gui.jswing.SingleCollectionTitleListGUI


/*
 import freight._
 import interface.complex.set.ElementsGUI

 ElementsGUI.test()
 */


/** GUI for set marks.
  *
  */
object ElementsGUI{

import core.objects.WeighedTitleString 

  def test() {
    val conn = new Connector(interface.db.sqliteJDBC.SyncConnection.testDB)
    apply(conn, 1, WeighedTitleString, 1)
  }

  /** GUI to edit set elements.
    */
  def apply(
    conn: Connector,
    collectionId: Long,
    meta: CompanionMeta,
titleFieldIdx: Int
  )
  {

    SingleCollectionTitleListGUI(
    s"Set Elements $collectionId",
    conn.elements(collectionId, meta),
    meta,
    titleFieldIdx,
    conn.close
  )
  }

}//MarksGUI

