package freight
package interface
package complex
package set


import interface.gui.jswing.SingleCollectionTitleListGUI

import core.objects.Title

/*
 import freight._
 import interface.complex.set.MarksGUI

 MarksGUI.test()
 */


/** GUI for set marks.
  *
  */
object MarksGUI{

  def test() {
    val conn = new Connector(interface.db.sqliteJDBC.SyncConnection.testDB)
    apply(conn)
  }

  /** GUI to edit set marks.
    */
  def apply(
    conn: Connector
  )
  {

    SingleCollectionTitleListGUI(
    "Set Marks",
    conn.marks,
    Title,
    titleFieldIdx = 1,
    conn.close
  )
  }

}//MarksGUI

