package freight
package interface
package gui
package jswing


/** Enumeration for image formats.
  *
  * Object items can be compared,
  */
sealed trait ImageFormat

case object JPEG extends ImageFormat {
  override def toString = "JPEG"
}

case object PNG extends ImageFormat {
  override def toString = "PNG"
}

case object GIF extends ImageFormat {
  override def toString = "GIF"
}

case object BMP extends ImageFormat {
  override def toString = "BMP"
}

case object WBMP extends ImageFormat {
  override def toString = "WBMP"
}


