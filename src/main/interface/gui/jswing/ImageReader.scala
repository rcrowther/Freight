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
  * The image view does not carry datam but references to data (Paths, default icons and items in collections). To recover collection images, the original, not the presented and resized icon, it takes a `reader` parameter. Thus the component is
  * somewhat unusual (other components do not need collection input). However,
  * the component still has the ability to both `readTo` and `loadFrom` [[Transferable]]
  * methods (the component keeps track of the last used id). 
  *
  * This carries an implication - any `loadFrom` plugs *must* be from the collection, or collection method, supplied as `reader`. To  enure this, the reader drops the read method and uses the id on the supplied `reader`method. Also, the component issues signals. In most circrcumstances of complex control, the controller issues relevant signals, and there is no need to connect to this component. 
  *  
  *
  * @define obj image
  *
  * @param notifyOfStatus emit status signals.
  * @param reader callback to read an element.
  */
class ImageReader(
  val showIdField: Boolean,
  val notifyOfStatus: Boolean,
  width: Int,
  height: Int,
  initialIcon: BufferedImage,
  mouseInIcon: BufferedImage,
  failImage: MorphingImage,
  val reader: (Long, ((ReadableByteChannel, Long)) => Unit) => Boolean
)
    extends BoxPanel(Orientation.Vertical)
    with FreightObjectReader
    with core.collection.PluggableBinaryReadable
    with core.collection.PluggableBinaryLoadable
{


  def selectedId: Option[Long] = idView.idAsLong
  def setSelectedId(oidO: Option[Long]) = idView.set(oidO)

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



  def writeTo(f: ((ReadableByteChannel, Long)) => Unit)
      : Boolean =
  {
    imageField.contentType match {

      case ImageField.default => {
        f(failImage.asChannel(), failImage.size)
        true
      }

      case ImageField.dropped => {
        val fSeq = imageField.get
        val cO = inputChannel(fSeq(0).toPath)
        if (cO == None) false
        else {
          val c = cO.get
          f(c, c.size.toInt)
          true
        }
      }

      case ImageField.external => {
        elErrorIf(
          (lastReadId == None),
          "tracking of lastReadId never became initialized, yet imagefield believes it has read data?"
        )
        // NB: (id == None) aside, this is protected
        reader(
          lastReadId.get,
          f
        )
      }

    }
  }

  protected def readData(oid: Long)
      : Boolean =
  {
    reader(oid, (params) =>{ imageField.set(params._1)})
  }

  protected def loadRaw(oid: Long)
      : Boolean =
  {
    var ok = true

    if(!readData(oid)) {
      clearNonId()
      ok = false
    }
    else  {
      val oidO = Some(oid)
      lastReadId = oidO
      setSelectedId(oidO)
    }

    readNotify(ok)

    ok
  }


  //deprecated
  def load(g: (java.nio.channels.ReadableByteChannel, Long)) {
    imageField.set(g._1)
  }

  def loadFrom(
    id: Long,
    f: (Long, ((ReadableByteChannel, Long)) => Unit) => Boolean
  )
      : Boolean =
  {
    // ditch the method and use the id only.
    // This ensures the same collection.
    loadRaw(id)
  }


  //----------------
  //-- Components --
  //----------------

  val idView = new IdView()

  val imageField = ImageField(
    width,
    height,
    initialIcon,
    mouseInIcon,
    failImage.asBufferedImage()
  )



  //------------------
  //-- Data actions --
  //------------------





  //--------------------
  //-- Action helpers --
  //--------------------

  /** Sets an action on the id field.
    *
    * A non-templated fudge which should not be relied upon
    * outside the jswing interface.
    */
  // NB: Todo this systemicly, the model must return the component, or
  // enable where it can?
  def idAction(f:() => Unit){
    idView.idAction(f)
  }



  //-------------
  //-- Methods --
  //-------------

  def clearNonId() {
    imageField.clear()
  }

  def clear() {
    imageField.clear()
    idView.clear()
  }



  //-----------------------------------------------------------
  // Main build
  //-----------------------------------------------------------
  
  if (showIdField) {
    idView.appendTo(contents)
  }
  contents.append(verticalSpacer)
  contents.append(imageField)
  contents.append(verticalSpacer)

}//ImageReader



object ImageReader {

  /** Creates an image reader with default icons.
    */
  def apply(
    showIdField: Boolean,
    notifyOfStatus: Boolean,
    width: Int,
    height: Int,
    collection: BinaryTakable
  )
      : ImageReader =
  {
    apply(
      showIdField,
      notifyOfStatus,
      width,
      height,
      initialIcon = grayTextImage(width, height, "Drop here", 24),
      mouseInIcon = mouseOverImage(width, height),
      failImage = new MorphingImage(missingImage(width, height), JPEG),
      collection
    )
  }

  /** Creates an image reader from a single binary collection.
    */
  def apply(
    showIdField: Boolean,
    notifyOfStatus: Boolean,
    width: Int,
    height: Int,
    initialIcon: BufferedImage,
    mouseInIcon: BufferedImage,
    failImage: MorphingImage,
    collection: BinaryTakable
  )
      : ImageReader =
  {
    new ImageReader(
      showIdField,
      notifyOfStatus,
      width,
      height,
      initialIcon,
      mouseInIcon,
      failImage,
      collection.apply
    )
  }

}//ImageReader
