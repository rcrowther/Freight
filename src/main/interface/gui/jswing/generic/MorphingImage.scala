package freight
package interface
package gui
package jswing
package generic


import javax.swing.{ImageIcon}

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.{File, InputStream, ByteArrayOutputStream, ByteArrayInputStream}
import java.nio.channels.{Channels, ReadableByteChannel}



/** Polymorphic class for a missing image.
  *
  * Can return many of the Java mutations on the idea of an image.
  */
class MorphingImage(bi : BufferedImage, imageFormat: ImageFormat)
{

  val buffer = {
    val os = new ByteArrayOutputStream()
    ImageIO.write(bi, imageFormat.toString, os)
    os.toByteArray()
  }


  def asInputStream()
      : ByteArrayInputStream =
  {
    new ByteArrayInputStream(buffer)
  }

  def asChannel()
      : ReadableByteChannel =
  {
    Channels.newChannel(asInputStream)
  }

  def asBufferedImage()
      :  BufferedImage =
  {
    ImageIO.read(asInputStream)
  }

  def asIcon() : ImageIcon = new ImageIcon(bi)

  def size: Int = buffer.size

}//MorphingImage

