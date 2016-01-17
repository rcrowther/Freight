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
class JSwingNotifier
    extends Publisher
{


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
        publish(StatusMessage.warning("not a valid id"))
      false
    }
    else {
      val id = idO.get
      if (id < 1 || id > Long.MaxValue) {
          if (id < 1) {
            publish(StatusMessage.warning("id must be > 0"))
          }
          else {
            publish(StatusMessage.warning("id must be < Long.MaxValue?!"))
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
        publish(StatusMessage.error(s"new object creation failed?"))

    }
    else {

        publish(StatusMessage.info("new object!"))

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

        publish(StatusMessage.error(s"load failed"))

    }
    else {

        publish(StatusMessage.info("loaded"))

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

        publish(StatusMessage.error(s"update failed?"))

    }
    else {

        publish(StatusMessage.info("update succeded"))

    }
  }




  protected def deleteNotify(ok: Boolean) {
    {
      if(!ok) {

          publish(StatusMessage.warning("delete failed?"))

      }
      else {

          publish(StatusMessage.info("deleted"))

      }
    }

  }

  /////////////////////////////
def runIfValidId(oidO: Option[Long])(act: => Unit)
{
if (validId(oidO) ) { act }
}

def runIfAppendOk(oidO: Option[Long])(act: => Unit)
{
          if(oidO == None) createNotify(false)
           else act
}

def insert(oidO: Option[Long]) = createNotify(oidO != None)
def append(oidO: Option[Long]) = createNotify(oidO != None)
def read(ok: Boolean) = readNotify(ok)
def update(oidO: Option[Long]) = updateNotify(oidO != None)
def delete(ok: Boolean) = deleteNotify(ok)



}//JSwingNotifier
