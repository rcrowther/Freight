package freight
package interface
package gui
package jswing
package component
package droppable


import swing._
import event._
import javax.swing.{Icon, ImageIcon}
import java.io.File

import java.awt.dnd.{
  DnDConstants,
  DropTarget,
  DropTargetListener,
  DropTargetDragEvent,
  DropTargetEvent,
  DropTargetDropEvent
}
import java.awt.datatransfer._


import javax.swing.SwingWorker
import java.util.{Timer, TimerTask}
import java.util.concurrent.TimeUnit
import collection.JavaConverters._
import collection.mutable.Buffer


/** Accepts file drops on an icon.
  *
  * Accepts file drops. Will take drops from from the natice sytem.
  *
  * The component is based in a JLabel.
  *
  * @param func func to run on recieved files.
  */
class FileDrop(
  val readyIcon: Icon,
  val mouseInIcon: Icon,
  icons: Seq[Icon],
  func: (File) => Unit
)
    extends Label
    with DropTargetListener
{
  //border = debugBorder
  opaque = true
  background = java.awt.Color.black


  icon = readyIcon



  // Timer for animation
  val animated = !icons.isEmpty
  val iconCount = icons.size
  var animationIdx = 0

  def nextFrame() {
    animationIdx =
      if (animationIdx < iconCount) animationIdx
      else 0

    icon = icons(animationIdx)
  }

  private final class FrameAdvance
      extends TimerTask
  {

    def run()
    {
      Swing.onEDT(nextFrame())
    }
  }

  private var t: Timer = new Timer()

  private def startAnimation(period: Long) {
    stopAnimation()

    // Always Daemon, for this
    t = new Timer(true)
    t.schedule(new FrameAdvance, period, period)
  }

  private def stopAnimation() {
    t.cancel()
    animationIdx = 0
  }



  // Working the function
  private final class Worker(buff: Buffer[File])
      extends SwingWorker[Boolean, Object]
  {
    
    override def doInBackground()
        : Boolean =
    {
      try {
	buff.foreach{ f =>
	  func(f.getAbsoluteFile())
	}
        true
      }
      catch {
        case e: Exception => false
      }
    }

    override protected def done()
    {
      try {
        get()
        if (animated) stopAnimation()
      }
      catch {
        case e: Exception => {
        }
      }
    }
  }



  val dt = new DropTarget(
    this.peer,
    // Operations allowed
    DnDConstants.ACTION_COPY_OR_MOVE,
    this,
    // Accepting events?
    true
      //ImageTransferable
  )

  // Drop listener
  def dragEnter(e: DropTargetDragEvent)
  {
    val tr: Transferable = e.getTransferable()

    if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
      icon = mouseInIcon
    }
  }

  def dragExit(e: DropTargetEvent): Unit = {
    icon = readyIcon
  }

  def dragOver(e: DropTargetDragEvent): Unit = {}

  def drop(e: DropTargetDropEvent)
  {
    try{

      val tr: Transferable = e.getTransferable()

      if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
      {
	e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE: Int)

        // Likely the most butt-ugly conversion ever.
        // Not blaming Scala R.C.
	val buff: Buffer[File] =
          tr.getTransferData(DataFlavor.javaFileListFlavor).asInstanceOf[java.util.ArrayList[File]].asScala

        if (animated) startAnimation(200)
          (new Worker(buff)).execute()
      }
    }
    catch {
      case ex: Exception => {
        ex.printStackTrace()
	e.rejectDrop()

      }
    }
    finally {
      icon = readyIcon
    }

  }

  def dropActionChanged(e: DropTargetDragEvent): Unit = {}

}//FileDrop



object FileDrop {

  /*
   import interface.gui.jswing.component.draggable.DraggableList
   */
  def testFunc(f: File) {
    log(s"f: ${f.toString}")
  }

  def test()
      : FileDrop =
  {
    new FileDrop(
      jswing.icon("images/drop.png"),
      jswing.icon("images/drop_blur_in.png"),
      Seq[Icon](
      jswing.icon("images/drop_1.png"),
      jswing.icon("images/drop_2.png"),
      jswing.icon("images/drop_1.png")
),
      testFunc
    )
  }

  def apply(
    readyIcon: Icon,
    mouseInIcon: Icon,
    icons: Seq[Icon],
    f: (File) => Unit
  )
      : FileDrop =
  {
    new FileDrop(readyIcon, mouseInIcon, icons, f)
  }
  

}//FileDrop

