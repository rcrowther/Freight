package freight
package interface
package gui
package jswing
package generic

import java.util.{Timer, TimerTask}
import java.util.concurrent.TimeUnit

import swing._
import collection.mutable.ArrayBuffer

import java.awt.Font
import java.awt.Color




/** A label to output messages.
  *
  * Can display messages continuously or with a timed clearance.
  */
class StatusLabel
    extends Label
{
  private var t: Timer = new Timer()

  // auto setup  of formatting
  font = new Font(Font.SANS_SERIF, Font.PLAIN, 12)



  private final class ClearTextTask
      extends TimerTask
  {

    def run()
    {
      Swing.onEDT(clearText())
    }
  }

  private def clearText() {
    text = ""
  }


  private def clearTextAfter(timeout: Long) {
    // Always Daemon, for this
    t = new Timer(true)
    t.schedule(new ClearTextTask, timeout)
  }

  private def timedText(txt: String, warningLevel: Int, timeout: Int) {
    // Stop whatever was going on, for sure (does nothing if thread done)
    t.cancel()
    val outText =
      warningLevel match {
        case 1 => txt
        case 2 => "Warning: " + txt
        case 3 => "Error: " + txt
      }
    text = outText
    clearTextAfter(timeout)
  }




  /** Displays a message, no timeout.
    */
  def apply(txt: String) {
    t.cancel()
    text = txt
  }

  /** Print text with stock timeouts.
    *
    * Text is timed out for levels above 0.
    */
  def apply(warningLevel: Int, txt: String) {
    if(warningLevel == 0) print(txt)
    else timedText(txt, warningLevel, 2400)
  }

  def info(text: String, timeout: Int) {
    timedText(text, 1, timeout)
  }

  def info(text: String) {
    timedText(text, 1, 2400)
  }

  def warning(text: String, timeout: Int) {
    timedText(text, 2, timeout)
  }

  def warning(text: String) {
    timedText(text, 2, 2400)
  }

  def error(text: String, timeout: Int) {
    timedText(text, 3, timeout)
  }

  def error(text: String) {
    timedText(text, 3, 2400)
  }

}//StatusLabel



object StatusLabel {
  val print = 0
  val info = 1
  val warning = 2
  val error = 3

}//StatusLabel

