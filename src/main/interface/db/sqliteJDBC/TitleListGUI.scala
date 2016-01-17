package freight
package interface
package db
package sqliteJDBC

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.charset.Charset
import core.iface.StringCanSpeak

import java.io.File



object TitleListGUI {

  /*
   import freight._
   import freight.interface.db.sqliteJDBC.TitleListGUI
   TitleListGUI.test
   */
  def test  {
    import freight.objects.Paper

    apply(
      new File("/home/rob/Code/scala/freight/src/test/interface/db/sqlite.db").toPath,
      false,
      "poems",
     2,
Paper
    )
  }

  def apply(
    p: Path,
    inMemory: Boolean,
    colName: String,
    titleFieldIdx: Int,
 meta: CompanionMeta
  )
  {
    val conn = SyncConnection(p, inMemory)
    val coll = conn.fieldQueryCollection(
      colName,
      meta
    )
/*
    interface.gui.jswing.CollectionTitleListGUI(
   colName + " (SQLite JDBC)",
    coll,
    titleFieldIdx,
    conn.close _,
    meta
  )
*/
    interface.gui.jswing.SingleCollectionTitleListGUI(
    windowTitle = colName + " (SQLite JDBC)",
    coll,
    meta,
   titleFieldIdx,
    conn.close _
  )
}

}//TitleListGUI
