package freight
package interface
package gui
package jswing

import swing.Publisher

import jswing.event.StatusMessage



/** Stock methods for jswing notifying.
  *
  * The methods emit swing events, all of which are notifying events intended for user interfaces.
*
* There is no need to use this class and it's methods. Sometimes custom relies may be more appropriate and/or helpful. However, use of the class will make interface reactions consistent with other interfaces. 
  */
class JSwingNotifier2
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


  /////////////////////////////
def runIfValidId(oidO: Option[Long])(act: => Unit)
{
if (validId(oidO) ) { act }
}

def runIfAppendOk(oidO: Option[Long])(act: => Unit)
{
          if(oidO == None) opFailNotify("create")
           else act
}


  /** Publishes a create message.
    */
  def create()
  = publish(StatusMessage.info("new object!"))

  /** Publishes a read message.
    */
  def read()
  = publish(StatusMessage.info("loaded"))


  /** Publishes an update message.
    */
  def update()
  = publish(StatusMessage.info("update succeded"))




  /** Publishes a delete message.
    */
  def delete()
  = publish(StatusMessage.info("deleted"))


 def opFailNotify(method: String)
  {
    method match {
      case "insert" => {
        publish(StatusMessage.warning(s"new object creation failed?"))
      }
      case "append" => {
        publish(StatusMessage.warning(s"new object creation failed?"))
      }
      case "create" => {
        publish(StatusMessage.warning(s"new object creation failed?"))
      }
      case "read" => {
        publish(StatusMessage.warning(s"load failed"))
      }
      case "upsert" => {
        publish(StatusMessage.warning(s"upsert failed?"))
      }
      case "update" => {
        publish(StatusMessage.warning(s"update failed?"))
      }
      case "foreach" => {
        publish(StatusMessage.warning(s"foreach read failed?"))
      }
      case "delete" => {
        publish(StatusMessage.warning(s"delete failed?"))
      }
case _ => {
error(s"Unrecognised string representation of method method: $method")
}
    }
  }



}//JSwingNotifier
