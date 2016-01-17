package freight
package interface
package file
package common


import java.nio.file.{Path}

import java.util.concurrent.atomic.AtomicBoolean
import freight.core.util.{ GranularReadWriteLock, File }

import java.nio.file.{Path, Files}

import java.io.IOException



/** Implementation of id collection for bases.
*
* Shared implementations betweeen binary bases and file bases. 
*/
trait IdCollection
//extends core.iface.IdCollection
{
  def p: Path
  def collectionName: String

  // Lock for full shutdown or drop
  protected val shutdown = new AtomicBoolean(false)

  //the lock
  protected val lock = GranularReadWriteLock.big()

  protected def details: SyncNamespaceHeader


  def -(id: Long)
      : Boolean =
  {
    elErrorIf(
      (id < InitialID),
      s"Id index out of range collectionName: $collectionName id: $id"
    )

    val idStr = id.toString

    if (shutdown.get) false
    else  {

      var ret = false
      val path = p.resolve(idStr)
      var prevSize: Long = 0

      try {
        lock.enterForWrite(id.toLong)

        // not racey operations, we're in the lock.
        if(Files.exists(path)) {

          // Not right, but should be near :)
          prevSize = Files.size(path)

          Files.delete(path)
          ret = true
        }

        lock.exit()


        // update header
        if(ret) {
          details.removeRecords(1, prevSize, id)
        }

        ret
      }
      catch {
        case e: IOException => error(s"Removing entry at $path")
          // Return a null id
          false
      }
    }
  }

  def clear()
      : Boolean =
  {
    var ret = false

    shutdown.set(true)

    // If anything needs a work unit, this
    if(lock.blockUntilDie(2000)) {
      File.deleteAll(p)
      details.reset()
      ret = true
    }
    else {
      error("SyncCollection clear() failed")
    }
    shutdown.set(false)
    ret
  }

  def clean()
      : Boolean =
  {
    shutdown.set(true)
    if(lock.blockUntilDie(2000)) {
      // If anything needs a work unit, this - db/commands/dropindexes
      File.deleteAllForcefully(p)
      true
    }
    else {
      severe("SyncCollection clean() failed")
      false
    }
  }

  // At the moment, this does nothing, especially as the mapped
  // namespace file can not be closed.
  def close() : Boolean = {
    // clear cache?
    true
  }



}//IdCollection
