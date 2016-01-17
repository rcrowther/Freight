package freight
package interface
package gui
package jswing

import swing.Publisher

import jswing.event.StatusMessage



/** Wrap a controller for a jswing interface.
  *
  * Emits swing events when the controller is called. Changes the
  * parameters to optional, which is easer to use with jswing
  * components.
  */
class JSwingController[G, T](
  notifyOfStatus : Boolean,
  ctl: core.collection.Takable[G, T]
)
    extends Publisher
//with interface.generic.CollectionController[T]
{



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
  protected var lastReadId: Option[Long] = None


  //--------------------
  //-- Action helpers --
  //--------------------

  /** Tests if the view carries contents or a selected item.
    *
    * Note that object views should always have valid content, even if
    * this is a default such as emty text, default images, empty
    * soundfiles, and so forth.
    *
    * However, this method tests if the id is valid, and emits
    * messages on fail.
    *
    * The id can be set by the user, so invalid can mean the id is not
    * a number. An invalid id can be also be a None, dependng on the
    * view. A read may fail (this should usually leave a valid id) or,
    * more likely, a delete leave a GUI with valid content but no
    * id. Invalid can also mean the number is out of range (a `Long`).
    *
    * On failure, the method emits status messages.
    *
    * @return if the id is valid, true, else false.
    */
  // NB: Not working, as the idToLong defends it?
  protected def validId(idO: Option[Long])
      : Boolean =
  {
    if(idO == None)  {
      if (notifyOfStatus) {
        publish(StatusMessage.warning("not a valid id"))
      }
      false
    }
    else {
      val id = idO.get
      if (id < 1 || id > Long.MaxValue) {
        if (notifyOfStatus) {
          if (id < 1) {
            publish(StatusMessage.warning("id must be > 0"))
          }
          else {
            publish(StatusMessage.warning("id must be < Long max"))
          }
        }
        false
      }
      else true
    }
  }


  /** Actions on a create.
    *
    * This method may be used for appending new elements, or inserting
    * elements to be referenced to elements in other collections. The
    * form of insert cannot be known, as it may depend on other views.
    *
    * Generally, nothing much is done on a failed create.  The id
    * remains the same, and the user is allowed to press buttons
    * again.
    *
    * The method emits signals for either case.
    */
  protected def createNotify(ok: Boolean) {
    if(!ok) {
      if (notifyOfStatus) {
        publish(StatusMessage.error(s"new object creation failed?"))
      }
    }
    else {
      if (notifyOfStatus) {
        publish(StatusMessage.info("new object!"))
      }
    }
  }

  /** Actions on a read.
    *
    * This method likely needs no other action.
    */
  protected def readNotify(
    ok: Boolean
  )
  {

    if(!ok) {
      if (notifyOfStatus) {
        publish(StatusMessage.error(s"load failed"))
      }
    }
    else {
      if (notifyOfStatus) {
        publish(StatusMessage.info("loaded"))
      }
    }
  }



  /** Actions on an update.
    *
    * The form of update cannot be known, as it may depend on other
    * views.
    *
    * Generally, nothing much is done on a failed create. The id
    * remains the same, and the user is allowed to press buttons
    * again.
    *
    * The method emits signals for either case.
    */
  protected def updateNotify(ok: Boolean) {

    if(!ok) {
      if (notifyOfStatus) {
        publish(StatusMessage.error(s"update failed?"))
      }
    }
    else {
      if (notifyOfStatus) {
        publish(StatusMessage.info("update succeded"))
      }
    }
  }




  protected def deleteNotify(ok: Boolean) {
    {
      if(!ok) {
        if (notifyOfStatus) {
          publish(StatusMessage.warning("delete failed?"))
        }
      }
      else {
        if (notifyOfStatus) {
          publish(StatusMessage.info("deleted"))
        }
      }
    }

  }

  /////////////////////////////

  def insert(
    oidO: Option[Long],
f: ((G) => Unit) => Unit
  )
      : Option[Long] =
  {
    var ok = validId(oidO)
    var ret: Option[Long] = None
    if (ok) {
      val oid = oidO.get
    f((g:G) => {ok = ctl.^(oid, g)} )
      if(ok) {
        val newOidO = Some(oid)
        ret = newOidO
      }
    }

    createNotify(ok)
    ret
  }

  def append(f: ((G) => Unit) => Unit)
      : Option[Long] =
  {
    var ok = true
    var ret: Option[Long] = None
    var newOid: Long = 0

    f((g:G) => {newOid = ctl.+(g)} )
    //log(s"append newOid $newOid")
    ok &= (newOid != NullID)
    if(ok) {
      ret = Some(newOid)
    }

    createNotify(ok)
    ret
  }

  def read(oidO: Option[Long], f: (G) => Unit)
: Boolean =
  {
    var ok = validId(oidO)
    if (ok) {
      val oid = oidO.get
      if(!ctl(oid, f)) {
        //clearNonId()
        ok = false
      }
      else  {
        lastReadId = oidO
        //setSelectedId(oidO)
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




// NB: Update is boosted to take an id.
// Not necessary for Anyval colls (where the controller ignores the parameter),
// it is necessary for Binary colls.
  def update(
    oidO: Option[Long],
f: ((G) => Unit) => Unit
)
      : Option[Long] =
  {
    var ret: Option[Long] = None

    var ok = validId(oidO)
    if (ok) {
      val oid = oidO.get
    var newOid: Long = 0
    f((g:G) => {ok = ctl.~(oid, g)} )
    ok = (newOid != NullID)
    if(ok) {
      ret = Some(oid)
    }
    updateNotify(ok)
}
    ret
  }

  def delete(
    oidO: Option[Long]
  )
      : Option[Long] =
  {
    var ok = validId(oidO)
    var ret: Option[Long] = None
    if (ok) {
      val oid = oidO.get
      if(!ctl.-(oid)) {
        ok = false
      }
      else {
        val newOidO = Some(oid)
        ret = newOidO
      }
    }

    deleteNotify(ok)
    ret
  }



  def foreach(f: (G) => Unit)
  {
    ctl.foreach(f)
  }

/** Size of the collection.
*
*  For consistency, this is forwarded, but emits no signals.
*/
  def size() : Long  = ctl.size

}//JSwingController
