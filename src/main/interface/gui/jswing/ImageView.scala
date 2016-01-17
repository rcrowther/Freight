package freight
package interface
package gui
package jswing



import swing._
import event._

import jswing.generic.{
StatusLabel,
 CountDisplay,
 MorphingImage
}

import gui.generic._

import java.nio.channels.{
  ReadableByteChannel,
  WritableByteChannel,
  FileChannel
}

import java.nio.file.Path
import java.io.{FileInputStream, InputStream}
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import component.droppable.ImageField



/** Swing GUI for single image records.
  *
  * A freight-based view. Handles all actions.
  * 
  * @define obj image
  *
  * @param notifyOfStatus emit status signals.
  * @param reader callback to read an element.
  */
class ImageView(
  width: Int,
  height: Int,
  initialIcon: BufferedImage,
  mouseInIcon: BufferedImage,
  failImage: MorphingImage
)
    extends ImageField(
    width,
    height,
    initialIcon,
    mouseInIcon,
    failImage.asBufferedImage()
  )
{

  // TODO: Utility, may find use elsewhere?

  private def inputChannel(p: Path): Option[FileChannel] = {
try {
Some(new FileInputStream(p.toFile()).getChannel())
}
catch {
case e: Exception => {
log4("failed to read file path: $p")
None
}
}
}


  def getData(f: ((ReadableByteChannel, Long)) => Unit)
    : Boolean =
  {
   contentType match {

      case ImageField.default => {
        f(failImage.asChannel(), failImage.size)
        true
      }

      case ImageField.dropped => {
        val fSeq = get
        val cO = inputChannel(fSeq(0).toPath)
if (cO == None) false
else {
val c = cO.get
        f(c, c.size.toInt)
true
}        
      }

      case ImageField.external => {
        error("image in editor set externally - can not be retieved")
      }

    }
  }



/**
*/
def setData(g: (java.nio.channels.ReadableByteChannel, Long)) {
    set(g._1)
}








}//ImageView



object ImageView {

/** Creates an image reader with default icons.
*/
  def apply(
    width: Int,
    height: Int
  )
      : ImageView =
  {
    apply(
      width,
      height,
      initialIcon = grayTextImage(width, height, "Drop here", 24),
      mouseInIcon = mouseOverImage(width, height),
      failImage = new MorphingImage(missingImage(width, height), JPEG)
    )
  }

/** Creates an image reader from a single binary collection.
*/
  def apply(
    width: Int,
    height: Int,
    initialIcon: BufferedImage,
    mouseInIcon: BufferedImage,
    failImage: MorphingImage
  )
      : ImageView =
  {
    new ImageView(
      width,
      height,
      initialIcon,
      mouseInIcon,
      failImage
    )
  }

}//ImageView
