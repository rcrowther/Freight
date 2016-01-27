package freight
package interface
package gui

import swing._
import java.awt.{
  Font,
  Dimension,
  Graphics,
  Color,
  BasicStroke,
  RenderingHints
}

import javax.swing.{ImageIcon, Icon}

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.{File, InputStream, ByteArrayOutputStream, ByteArrayInputStream}
import java.nio.channels.{Channels, ReadableByteChannel}
import java.awt.FontMetrics



/** Various swing GUIs.
  */
package object jswing {



  //-------------
  //-- Swing General Items --
  //-------------

  /** Halfway sane font.
    */
  val stockFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12)

  /** Italic font.
    */
  val italicFont = new Font(Font.SANS_SERIF, Font.ITALIC, 12)

  /** Hideous red border for Swing-garbage layout.
    */
  def debugBorder =
    javax.swing.BorderFactory.createLineBorder(java.awt.Color.red)

  def stockLabel(text: String): Label = {
    val l = new Label(text)
    l.font = stockFont
    l.border = labelBorder
    l.xLayoutAlignment = 0.0
    l
  }

  def stockItalicLabel(text: String): Label = {
    val l =  new Label(text)
    l.font = italicFont
    l.border = labelBorder
    l.xLayoutAlignment = 0.0
    l
  }

  def centreLabel(text: String): Label = {
    val l = stockLabel(text: String)
    l.maximumSize = xGluedYFixed
    l.horizontalAlignment = Alignment.Center
    l
  }

  def centred(c: Component): BoxPanel = {
    new BoxPanel(Orientation.Horizontal) {
      xLayoutAlignment = 0.0
      contents += Swing.HGlue
      contents += c
      contents += Swing.HGlue
    }
  }


  def stockLabel(): Label = stockLabel("")

  /*
   import freight.interface.gui.jswing.component.draggable._

   jswing.icon(classOf[DraggableList], "draggable_gray.png")

   */



  //---------------
  //-- Resources --
  //---------------

  /** Returns a resource relative to the jswing package object.
    */
  def resourceFromClassloader(path: String): java.net.URL =
    this.getClass.getResource(path)



  //--------------------
  //-- Prebuild Images --
  //--------------------

  /** Returns a buffered image representing a missing image.
    *
    * The icon has a black background, with a small red cross on a
    * 32x32 rectangle. The icon can be any size greater than 32x32.
    */
  def missingImage(width: Int, height: Int)
      : BufferedImage =
  {

    elErrorIf(
      (width < 32 || height < 32),
      "requested missingIcon sizes must be be > 32 width:$width height:$height"
    )

    val bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    val g2d = bi.createGraphics()
    val xPos: Int = (width/2) - 16
    val yPos: Int = (height/2) - 16

    g2d.setColor(Color.BLACK)
    g2d.fillRect(0, 0, width, height)

    g2d.setColor(Color.WHITE)
    g2d.fillRect(xPos, yPos, 32, 32)

    g2d.setStroke(new BasicStroke(1))
    g2d.setColor(Color.GRAY)
    g2d.drawLine(xPos + 32, yPos, xPos + 32, yPos + 32)
    g2d.drawLine(xPos, yPos + 32, xPos + 32, yPos + 32)

    g2d.setColor(Color.LIGHT_GRAY)
    g2d.drawLine(xPos, yPos, xPos, yPos + 32)
    g2d.drawLine(xPos, yPos, xPos + 32, yPos)

    g2d.setColor(Color.RED)
    g2d.setStroke(new BasicStroke(4))
    g2d.drawLine(xPos + 10, yPos + 10, xPos + 22, yPos + 22)
    g2d.drawLine(xPos + 10, yPos + 22, xPos + 22, yPos + 10)
    g2d.dispose()
    bi
  }

  private[this] val missingIconThing: ImageIcon =
    new ImageIcon(missingImage(32, 32))

  /** Stock icon representing missing image.
    *
    * The icon is 32x32 pixels large.
    */
  def missingIcon(): ImageIcon = missingIconThing

  /** Stock icon representing missing image.
    */
  def missingIcon(width: Int, height: Int): ImageIcon =
    new ImageIcon(missingImage(width, height))


  /** Returns a buffered image representing a mouse over image.
    *
    * The icon is three concentric black rectangles. The icon can be
    * any size greater than 32x32.
    */
  def mouseOverImage(width: Int, height: Int)
      : BufferedImage =
  {

    elErrorIf(
      (width < 32 || height < 32),
      "requested missingIcon sizes must be be > 32 width:$width height:$height"
    )

    val bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val stroke = new BasicStroke(4)
    val g2d = bi.createGraphics()

    g2d.setColor(Color.WHITE)
    g2d.fillRect(0, 0, width, height)

    val xStep: Int = (width/2)/3
    val yStep: Int = (height/2)/3

    g2d.setColor(Color.BLACK)
    g2d.setStroke(stroke)

    var xStart: Int = 2
    var yStart: Int = 2
    var rWidth: Int = width - 4
    var rHeight: Int = height - 4
    g2d.drawRect(xStart, yStart, rWidth, rHeight)

    xStart += xStep
    yStart += yStep
    rWidth -= xStep << 1
    rHeight -= yStep << 1
    g2d.drawRect(xStart, yStart, rWidth, rHeight)

    xStart += xStep
    yStart += yStep
    rWidth -= xStep << 1
    rHeight -= yStep << 1
    g2d.drawRect(xStart, yStart, rWidth, rHeight)

    g2d.dispose()
    bi
  }

  /** Returns a buffered image with centered gray text.
    *
    * If the text width exceeds the image width, an exception will be
    * thrown.
    */
  def grayTextImage(width: Int, height: Int, text: String, fontSize: Int)
      : BufferedImage =
  {

    val bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val g2d = bi.createGraphics()

    g2d.setColor(Color.LIGHT_GRAY)
    g2d.fillRect(0, 0, width, height)



    val font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize)
    g2d.setFont(font)
    val fm: FontMetrics = g2d.getFontMetrics


    val textWidth = fm.stringWidth(text)

    elErrorIf(
      textWidth > width,
      "String rendered in font is too wide for given width stringwidth: $textWidth width: $width"
    )

    val x = (width - textWidth) /2
    val y = height >> 1

    val LO_GRAY = new Color(96, 96, 96)
    val HI_GRAY = new Color(240, 240, 240)

    g2d.setColor(LO_GRAY)
    g2d.drawString(text, x - 1, y - 1)

    g2d.setColor(HI_GRAY)
    g2d.drawString(text, x + 1, y + 1)

    g2d.setColor(Color.LIGHT_GRAY)
    g2d.drawString(text, x, y)

    g2d.dispose()
    bi
  }



  //-------------
  //-- Images --
  //-------------

  /** Returns a buffered image from a string path to a file.
    *
    */
  def bufferedImage(path: String)
      : BufferedImage =
  {
    val imageURL = resourceFromClassloader(path)
    //val imageURL = f.getAbsoluteFile().toURL
    try {
      ImageIO.read(imageURL)
    } catch {
      case _: Exception =>
        error(s"Unable to load URL as buffered image path:$path url:$imageURL")
    }
  }

  /** Returns a buffered image from a file.
    *
    */
  def bufferedImage(f: File)
      : BufferedImage =
  {
    bufferedImage(f.toString)
  }


  /** Optionally returns a buffered image from a file.
    */
  def getBufferedImage(path: String)
      : Option[BufferedImage] =
  {
    val imageURL = resourceFromClassloader(path)
    try {
      val bi: BufferedImage = ImageIO.read(imageURL)
      Some(bi)
    } catch {
      case _: Exception =>
        None
    }
  }

  /** Optionally returns a buffered image from a string path to a file.
    */
  def getBufferedImage(f: File)
      : Option[BufferedImage] =
  {
    getBufferedImage(f.toString)
  }

  /** Optionally returns a buffered image from a string path to a file.
    *
    */
  /*
   def getBufferedImage(path: Path)
   : Option[BufferedImage] =
   {
   bufferedImage(path.toString)
   }
   */


  /** Returns an icon.
    *
    * Paths are relative to the jswing package object.
    */
  def icon(path: String)
      : Icon =
  {
    val imageURL = resourceFromClassloader(path)
    try {
      new ImageIcon(imageURL)
    } catch {
      case _: Exception =>
        log(s"Couldn't load icon image from path:${path.toString} url:${imageURL}")
        Swing.EmptyIcon
    }
  }

  /** Returns an icon from a string path to a file.
    *
    */
  def imageIcon(path: String)
      : ImageIcon =
  {
    val imageURL = resourceFromClassloader(path)
    try {
      val bi: BufferedImage = ImageIO.read(imageURL)
      new ImageIcon(bi)
    } catch {
      case _: Exception =>
        log(s"Couldn't load icon image from path:${path.toString} url:${imageURL}")
        missingIcon
    }
  }



  /** Optionally returns an icon from a file.
    *
    */
  def getIcon(f: File)
      : Option[ImageIcon] =
  {
    val imageURL = f.getAbsoluteFile().toURL
    try {
      val bi: BufferedImage = ImageIO.read(imageURL)
      Some(new ImageIcon(bi))
    } catch {
      case _: Exception =>
        None
    }
  }


  //--------------------
  //-- Image resizing --
  //--------------------

  /** Resize a buffered image.
    *
    * The image is stored in memory.
    */
  def  resizeImage(
    img: BufferedImage,
    width: Int,
    height: Int,
    typ: Int,
    highQuality: Boolean
  )
      : Option[BufferedImage] =
  {
    try {
      val newImg = new BufferedImage(width, height, typ)
      val g: Graphics2D = newImg.createGraphics()
      if (highQuality) {
        g.setRenderingHint(
          RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR
        )
      }
      else {
        g.setRenderingHint(
          RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        )
      }
      g.drawImage(img, 0, 0, width, height, null)
      g.dispose()
      Some(newImg)
    }
    catch {
      case e: Exception =>
        log(s"Couldn't resize image to width:$width height:$height")
        None
    }
  }

  /** Resize a buffered image to an RGB image.
    *
    * The image is stored in memory.
    */
  def resizedImage(
    img: BufferedImage,
    width: Int,
    height: Int,
    highQuality: Boolean
  )
      : Option[BufferedImage] =
  {
    resizeImage(
      img,
      width,
      height,
      BufferedImage.TYPE_INT_RGB,
      highQuality
    )
  }

  /** Returns a sized icon from a path to a local file.
    *
    */
  def resizedLocalImageIcon(
    f: File,
    width: Int,
    height: Int,
    highQuality: Boolean
  )
      : Option[ImageIcon] =
  {
    val imageURL = f.getAbsoluteFile().toURL
    try {
      val bi: BufferedImage = ImageIO.read(imageURL)
      val riO = resizedImage(
        bi,
        width,
        height,
        highQuality
      )
      if(riO == None) None
      else Some(new ImageIcon(riO.get))
    }
    catch {
      case _: Exception =>
        None
    }
  }

  /** Returns a sized icon from an input stream.
    *
    */
  def resizeAsIcon(
    img: BufferedImage,
    width: Int,
    height: Int,
    highQuality: Boolean
  )
      : Option[ImageIcon] =
  {
    try {
      val ii = resizedImage(
        img,
        width,
        height,
        highQuality
      )
      if (ii == None) None
      else Some(new ImageIcon(ii.get))
    }
    catch {
      case _: Exception =>
        None
    }
  }

  /** Returns a sized icon from an input stream.
    *
    */
  def resizeAsIcon(
    f: InputStream,
    width: Int,
    height: Int,
    highQuality: Boolean
  )
      : Option[ImageIcon] =
  {
    try {
      val bi: BufferedImage = ImageIO.read(f)
      val riO = resizedImage(
        bi,
        width,
        height,
        highQuality
      )
      if(riO == None) None
      else Some(new ImageIcon(riO.get))
    }
    catch {
      case _: Exception =>
        None
    }
  }



  //-----------------------
  //-- Swing Decorations --
  //-----------------------

  /** Return a rough limit on length by char.
    */
  def limitYFixed(x: Int) = new Dimension(x * 12, 20)


  /** Dimentions x to be unlimited y to be fixed to reasonable space.
    *
    * e.g. set maximumSize on input objects like textfields.
    */
  val xGluedYFixed = new Dimension(Int.MaxValue, 20)

  /** Return a rough limit on length by char.
    *
    * e.g. set maximumSize on input objects like textfields.
    */
  private def xCharLimitedYFixed(x: Int) = new Dimension(x * 12, 20)

  /** Place more appropriate space round a text label.
    *
    */
  val labelBorder = Swing.EmptyBorder(8, 0, 2, 0)

  val loGray = new java.awt.Color(155, 155, 155)
  val midGray = new java.awt.Color(177, 177, 177)
  val hiGray = new java.awt.Color(225, 225, 225)

  /** Warning, collapses button size.
    *
    * stock = [width=34,height=10]
    */
  /*
   def buttonBorder = Swing.EtchedBorder(
   Swing.Lowered,
   highlight = hiGray,
   shadow = midGray
   ) 
   */
  //def buttonBorder =  Swing.EmptyBorder(0, 24, 0, 24)

  def smallButton = new Dimension(64, 24)


  //------------
  //-- Swing Layout --
  //------------

  def buttonSpacer = Swing.RigidBox(new Dimension(20,0))

  /** Stops vertical boxes collapsing.
    */
  def horizontalBrace = Swing.RigidBox(new Dimension(1, 24))
  def tightHorizontalSpacer = Swing.RigidBox(new Dimension(8, 0))
  def largeHorizontalSpacer = Swing.RigidBox(new Dimension(80,0))

  def verticalSpacer = Swing.RigidBox(new Dimension(1, 8))
  def tightVerticalSpacer = Swing.RigidBox(new Dimension(0, 2))

  def panelBorder = Swing.EmptyBorder(12, 12, 8, 12)
  //def panelBorderNoSouth = Swing.EmptyBorder(12, 12, 8, 0)
  //val tightButtonBorder = Swing.EmptyBorder(12, 12, 8, 12)



  //----------------
  //-- Components --
  //----------------

  type DraggableTree = component.draggable.DraggableTree
  type DraggableList = component.draggable.DraggableList
  type FreightLineList = component.TitleLineList

}//package
