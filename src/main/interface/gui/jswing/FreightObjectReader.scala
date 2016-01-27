package freight
package interface
package gui
package jswing

import swing.Publisher

import jswing.event.StatusMessage



/** Base trait for views.
  * 
  * Templates notification helper methods for actions.
  *
  * At this point, the signature and sources for actions are 
  * undefined.
  *
  * The trait also allows status notifications to be switched off,
  * using `notifyOfStatus`. In that case, the only signal emitted is
  * the display signal `NeedsResizing`.
*
  * @define obj object
  */
// Needs underlying view rewriting to carry id'ed images
trait FreightObjectReader
    extends FreightComponent
{

/** Set the id field.
*
* Only sets the field, will not reload reader/view contents.
* intended for use where the id is known to have changed after code action (most likely, after an `append`).
*/
  protected def setSelectedId(oid: Option[Long])


  def getSelectedId: Option[Long] = selectedId

  /** Tracks the last id used for the data in the view.
    *
    * The source id can be different to the id
    * that code and users can set as a target. The
    * data may be also changed by dropping.
    *
    * This does not guarantee it refers to the data in the view. It
    * refers to the last read. Implementations will need to track if
    * data has been revised.
    *
    * Used for views which only carry references to data, which will
    * need to be retrieved on decisive action. Views which carry data
    * can use the data they hold.
    */
  //protected var lastReadId: Option[Long] = None





  //------------------
  //-- Data actions --
  //------------------
  // NB: The implementation of the action depends on the
  // form of data in the view, and the collection the data
  // works with. However, the id handling can be defined here,
  // and so the actions defined for all interfaces.
  //
  // will read the data, and modify the view

  /** Action for data reading.
    *
    */
  //protected def readData(oid: Long): Boolean




  //-------------
  //-- Actions --
  //-------------
  //NB: Can be defined here as the signature is known.

  /** Internal implementation of a read action.
    *
    * Must validate the id, then set the gui.
    */
/*
  protected def readRaw(oidO: Option[Long]): Boolean =
  {
    var ok = validId(oidO)
    if (ok) {
      val oid = oidO.get
      if(!readData(oid)) {
        clearNonId()
        ok = false
      }
      else  {
        lastReadId = oidO
        setSelectedId(oidO)
      }
      // notify here as otherwise overwrites invalid id notifications.
      readNotify(ok)
    }
    // Win/loose, set the id
    // In some views, this duplicates action handling e.g. in the object view
    // the view reads te id anyway. But asserting here adds robustness
    // (e.g. modifying out of sync ids from data).
    
    ok
  }


  /** Reads a $obj.
    *
    * Use to set the view by code.
    *
    * Failing to read produces a clear, as indication.
    */
  def read(oidO: Option[Long])
      : Boolean =
  {
    readRaw(oidO)
  }

  /** Reads a $obj from the view id.
    *
    */
  def read()
      : Boolean =
  {
    readRaw(selectedId)
  }
*/

  //-------------
  //-- Methods --
  //-------------

  /** Clear the view of data, excepting the id.
    *
    * Empties contents, but retains the id.
    *
    * Intended for an unsuccessful read action, but may find uses
    * elsewhere (so don't abuse without some thought).
    */
  def clearNonId(): Unit

  /** Clear the view of data.
    *
    * Empties the contents, sets the id and any internal tracking to
    * None.
    *
    * Intended for a successful delete action, but may find uses
    * elsewhere (so don't abuse without some thought).
    */
  def clear(): Unit

}//FreightObjectReader
