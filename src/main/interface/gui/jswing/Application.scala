package freight
package interface
package gui
package jswing

import scala.swing._



/** For simple applications.
  * 
  *  Overrides `SimpleSwingApplication`. Provides toplevel window access, notably the method `resizeToContents`, and the `closer` mthod.
  */
abstract class Application(
  windowTitle: String,
  closer: () => Boolean
)
    extends SimpleSwingApplication
{

  private var topWin: Option[Frame] = None

  /** Resizes the window to it's contents.
    *
    * Calls Swing method `pack` on the main window. Must be called in
    * the event loop.
    */
  def resizeToContents() {
    val w = topWin.getOrElse{
      error("resizeToContents called on invalid component")
    }
    w.pack()
  }

  /**
    * Calls `top`, packs the frame, and displays it.
    */
  override def startup(args: Array[String]) {
    val t = top
    t.title = windowTitle
    if (t.size == new Dimension(0,0)) t.pack()
    t.visible = true
    topWin = Some(t)
  }


  override def shutdown() {
    println("Outer frame closing")
    val w = topWin.getOrElse{
      error("shutdown called on invalid component")
    }
    w.close()
    if(!closer()) {
      error("Outer frame failed to close it's resources")
    }

    // super.shutdown()
  }


}//Application

