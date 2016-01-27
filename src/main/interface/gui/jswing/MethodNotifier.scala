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
trait MethodNotifier
{

  this: Publisher =>


  /** Publishes a create message.
    */
  protected def createNotify()
  = publish(StatusMessage.info("new object!"))

  /** Publishes a read message.
    */
  protected def readNotify()
  = publish(StatusMessage.info("loaded"))


  /** Publishes an update message.
    */
  protected def updateNotify()
  = publish(StatusMessage.info("update succeded"))




  /** Publishes a delete message.
    */
  protected def deleteNotify()
  = publish(StatusMessage.info("deleted"))


  protected def opFailNotify(method: String)
  {
    method match {
      case "insert" => {
        publish(StatusMessage.warning(s"new object creation failed?"))
      }
      case "append" => {
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
      case "delete" => {
        publish(StatusMessage.warning(s"delete failed?"))
      }
    }
  }

}//JSwingNotifier
