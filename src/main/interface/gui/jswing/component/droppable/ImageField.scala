package freight
package interface
package gui
package jswing
package component
package droppable


import swing._
import event._
import javax.swing.{Icon, ImageIcon}
import java.awt.image.BufferedImage
import java.io.{File, InputStream}
import java.nio.file.{ Path }
import java.nio.channels.{
  ReadableByteChannel,
  Channels
}

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


/** Displays image files.
  *
  * Similar to a TextField, but for images. Displays the image if able
  * to do so. The image can not be edited but, like a TextField paste,
  * new files can be dropped onto the field. The ImageField can also
  * be loaded by code using the `set` methods.
  *
  * The imagefield can carry several files, but will only display the
  * first.
  *
  * The display is fixed in size. All supplied images, including the
  * images supplied for defaults, are resized to match the given
  * size. The image displays are low quality but fast to paint (this
  * is also cheap on memory usage). The display never scrolls.
  *
  * No attempt is made to retain source image data for futher use
  * (dropped files are tracked by their path). Code using the field
  * must keep track of sources placed using `set` methods. To help
  * code using the display, `contentType` will return the source of
  * the images.
  * 
  * The test for a dropped file being an image is by attempting a
  * read. This is ok for GUIs, but should not be relied upon for
  * extenal security.
  *
  * The component is based in a JLabel.
  *
  * @param initialImage the first image to be displyed. Can carry
  *  messages, and sets the size of the component.
  * @param mouseInImage an image shown when a user makes a valid
  *  drag over the component.
  * @param failImage an image shown if an image fails to load (the
  *  image source may have been provided by code or a user)
  */
// TOCONSIDER: May need to thread/animate? Code left in R.C.
class ImageField(
  width: Int,
  height: Int,
  initialImage: BufferedImage,
  mouseInImage: BufferedImage,
  failImage: BufferedImage
)
    extends Label
    with DropTargetListener
{
  //border = debugBorder
  opaque = true
  background = java.awt.Color.black

  private val mouseInIcon: ImageIcon = resizeAsIcon( mouseInImage, width, height, false).getOrElse(
    error("mouseInIcon failed to load")
  )
  private val failIcon: ImageIcon = resizeAsIcon(failImage, width, height,  false).getOrElse(
    error("failIcon failed to load")
  )

  // private var currentBuffer: ImageBuffer = initialIcon
  private var currentIcon: ImageIcon = resizeAsIcon(initialImage, width, height,  false).getOrElse(
    error("initialIcon failed to load")
  )
  private var currentFiles: Buffer[File] = Buffer()

  icon = currentIcon

  private var contentT: Int = ImageField.default

  /** Type of the contained image.
    *
    * See the enumeration in the object.
    */
  def contentType : Int = contentT
  /*
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
   */


  // Working the function
  /*
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
   finally {
   icon = currentIcon
   }
   }
   }

   */

  val dt = new DropTarget(
    this.peer,
    // Operations allowed
    DnDConstants.ACTION_COPY_OR_MOVE,
    this,
    // Accepting events?
    true
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
    icon = currentIcon
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
	val foundFiles =
          tr.getTransferData(DataFlavor.javaFileListFlavor).asInstanceOf[java.util.ArrayList[File]].asScala

        setFromDrop(foundFiles)
        //if (animated) startAnimation(200)
        // (new Worker(buff)).execute()
        contentT = ImageField.dropped
      }
    }
    catch {
      case ex: Exception => {
        ex.printStackTrace()
	e.rejectDrop()
        clear()
      }
    }

  }

  def dropActionChanged(e: DropTargetDragEvent): Unit = {}


  // Main methods
  def get() : Buffer[File] = {
    currentFiles
  }

  // NB: Only used internally for dropping.
  private def setFromDrop(fs: Buffer[File])
      : Boolean =
  {
    currentFiles = fs
    // NB: A dismal test that this is an image,
    // not some other binary data
    val ok = loadIconFromFile(fs(0))
    ok
  }

  def set(f: File)
      : Boolean =
  {
    currentFiles.clear()
    val ok = loadIconFromFile(f)
    if(ok) {
      contentT = ImageField.external
    }
    ok
  }

  def set(p: Path)
      : Boolean =
  {
    set(p.toFile)
  }

  /** Loads an image from an input stream.
    *
    * This component can not keep track of the image source - some
    * other code must do that.
    */
  def set(s: InputStream)
      : Boolean =
  {
    currentFiles.clear()
    val ok = loadIconFromStream(s)
    if(ok) {
      contentT = ImageField.external
    }
    ok
  }

  def set(c: ReadableByteChannel)
      : Boolean =
  {
    set(Channels.newInputStream(c))
  }

  private def loadIcon(iconO: Option[ImageIcon])
      : Boolean =
  {
    val ok = (iconO != None)
    val ikon =
      if (ok) {
        iconO.get
      }
      else {
        contentT = ImageField.default
        failIcon

      }
    currentIcon = ikon
    icon = currentIcon
    ok
  }

  private def loadIconFromFile(f: File)
      : Boolean =
  {
    // NB: false = low quality
    loadIcon(resizedLocalImageIcon(f, width, height, false))
  }

  private def loadIconFromStream(s: InputStream)
      : Boolean =
  {
    // NB: false = low quality
    loadIcon(resizeAsIcon(s, width, height, false))
  }



  def clear() {
    currentFiles.clear()
    currentIcon = failIcon
    contentT = ImageField.default
    icon = currentIcon
  }

}//ImageField



object ImageField {
  /** The current image is the default image built from `failImage`.
    */
  val default = 0

  /** The current image is built from a dropped file.
    *
    * The path of the image can be found in `get(0)`.
    */
  val dropped = 1

  /** The current image is built from externally supplied data.
    *
    * The `ImageField` has no knowledge of where this image was
    * sourced.
    */
// TODO: rename as loaded
  val external = 2


  def test()
      : ImageField =
  {
    val f = new ImageField(
      194,
      194,
      jswing.grayTextImage(194, 194, "Drop here", 24),
      jswing.mouseOverImage(194, 194),
      missingImage(194, 194)
    )
    f
  }

  def apply(
    width: Int,
    height: Int,
    initialIcon: BufferedImage,
    mouseInIcon: BufferedImage,
    failIcon: BufferedImage
  )
      : ImageField =
  {
    new ImageField(width, height, initialIcon, mouseInIcon, failIcon)
  }
  

}//ImageField

