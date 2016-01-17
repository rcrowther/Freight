package freight
package interface
package gui
package jswing
package event


import swing._
import event._

/** An event carrying a status message.
*/
object StatusMessage {
  def print(msg: String): StatusMessage = new StatusMessage(0, msg)
  def info(msg: String): StatusMessage = new StatusMessage(1, msg)
  def warning(msg: String): StatusMessage = new StatusMessage(2, msg)
  def error(msg: String): StatusMessage = new StatusMessage(3, msg)

  def unapply(a: StatusMessage) : Option[(Int, String)] = Some((a.level -> a.msg))
}

class StatusMessage(val level: Int, val msg: String) extends Event
