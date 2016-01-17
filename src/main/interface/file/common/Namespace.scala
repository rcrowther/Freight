package freight
package interface
package file
package common


import java.nio.file.{Path, Files}
import freight.core.util.File


/** Synchronised version of a namespace header.
*
* See [[NamespaceHeader]]
  *
  */
// TODO: Base virtual 'Name'? And Freight type?
// defo: Canspeak!?
trait Namespace
{

  protected val p: Path

  val details: SyncNamespaceHeader


  /** Attempts a repair of the SyncCollection by guessing an NS file.
    *
    * Not locked?
    *
    * @param topId  current highest id in the SyncCollection. If not
    * supplied, very lengthy to find.
    */
  // TODO: Restart after this, or make static?
  def repairNS() {
    // Assert it's there, even if held elsewhere
    val nsBuff = NamespaceFile.openOrCreate(p)


    // Get the filecount from the directory
    val (maxId, n) = File.countIdFilenames(p)

    // Same thing with byteSize
    File.dirByteSize(p, """\d{1,19}""") match {
      case None => log("Unable to determine size of files in the SyncCollection $p, is an unexpected file in the folder?")
      case Some(sz) => {
        details.reset(
          n,
          maxId,
          sz
        )
      }
    }
  }

  /** Size of the SyncCollection counting bytes in files.
    */
  def byteSize = details.byteSize

  /** Returns the next (incrementing) id.
    * 
    * The id is stored in the namespace file.
    *
    * Used to inform builders when performing inserts and updates.
    *
    * Must be called in a lock.
    */
// TODO: This is not loced, and that is BAAAAD.
  def nextId() : Long = {
    val topId = details.topId + 1
    errorIf((topId >= Long.MaxValue), "top id exceeding long max")
    topId
  }

  def size() : Long = {
    details.recordCount
  }

}//Namespace

