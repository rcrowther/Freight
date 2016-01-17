package freight
package interface
package db
package sqliteJDBC

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.charset.Charset
import core.iface.StringCanSpeak

import java.io.File



object GUI {

  /*
   import freight._
   import freight.interface.db.sqliteJDBC.GUI
   GUI.test
   */
  def test  {
    import freight.objects.Paper

    apply(
      new File("/home/rob/Code/scala/freight/src/test/interface/db/sqlite.db").toPath,
      false,
      "poems",
     // Paper.canTransform,
     // Paper.descriptiveGiver
Paper
    )
  }

  def apply(
    p: Path,
    inMemory: Boolean,
    colName: String,
 meta: CompanionMeta
  )
  {
    val conn = SyncConnection(p, inMemory)
    val coll = conn.fieldQueryCollection(
      colName,
      meta
    )

    interface.gui.jswing.SingleCollectionGUI(
    windowTitle = colName + " (SQLite JDBC)",
   coll,
    meta,
    conn.close
  )
}

}//GUI
