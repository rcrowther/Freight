package freight
package interface
package db

import java.nio.file.{Path, Files}
import java.io.File



object ConnectionRouter {

  //TEMP
  val filebasePath = "/home/rob/Code/scala/freight/src/test/interface/file"
  val filebaseSpeak = freight.canspeak.Ini
  val sqlitejdbcPath = "/home/rob/Code/scala/freight/src/test/interface/db/sqlite.db"

  private val filebaseDB = file.filebase.SyncConnection.utf8(
    p = new File(filebasePath).toPath,
    speak = filebaseSpeak
  )

  private val sqlitejdbcDB = db.sqliteJDBC.SyncConnection(
    p = new File(sqlitejdbcPath).toPath
  )

  val connections = Map[Int, core.collection.Connection](
    (ConnectionType.filebase -> filebaseDB),
    (ConnectionType.sqlitejdbc -> sqlitejdbcDB)
  )

  def connection(connectionType: Int)
      : core.collection.Connection =
  {
    connections.get(connectionType).getOrElse{
      error(s"database connection requested but not in Router map connectionType: $connectionType")
    }
  }


}//Router
