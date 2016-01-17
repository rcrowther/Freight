package freight
package interface
package file
package filebase

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.charset.Charset
import core.iface.StringCanSpeak



object GUI {

  /*
   import freight._
   import freight.interface.file.base.GUI
   GUI.test
   */
  def test  {
    import freight.objects.Paper

    apply(
      "/home/rob/Code/scala/freight/src/test/interface/file",
"base",
      StandardCharsets.UTF_8,
      Paper,
      freight.canspeak.JSON
    )
  }


  def apply(
    p: Path,
 collectionName: String,
    charset: Charset,
    meta: CompanionMeta,
    speak: StringCanSpeak
  )
  {
    val coll = new SyncCollection(p, collectionName, charset, meta, speak)
/*
    interface.gui.jswing.CollectionGUI(
   "Filebase:",
    coll,
     coll.close,
    meta
  )
*/
  }

}//GUI
