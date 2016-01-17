package freight.interface.html.servlet



/**
  * <http://www.iana.org/assignments/media-types/>
  */
// ...and so forth. Please only submit definitions *other* people will use.
sealed trait ContentType {
def mime : String
}

object TEXT extends ContentType {
def mime : String = "text/html"
}

object JPEG extends ContentType {
def mime : String = "image/jpeg"
}

object JPG extends ContentType {
def mime : String = "image/jpg"
}

object BMP extends ContentType {
def mime : String = "image/bmp"
}

object GIF extends ContentType {
def mime : String = "image/gif"
}

object PNG extends ContentType {
def mime : String = "image/png"
}

object SVG extends ContentType {
def mime : String = "image/svg"
}

object TIFF extends ContentType {
def mime : String = "image/tiff"
}

object ICO extends ContentType {
def mime : String = "image/ico"
}

object MPEG extends ContentType {
def mime : String = "audio/mpeg"
}

object MP4 extends ContentType {
def mime : String = "audio/mp4"
}

object OGG extends ContentType {
def mime : String = "audio/ogg"
}

object VORBIS extends ContentType {
def mime : String = "audio/vorbis"
}

/*
  val mimeMap = Map(
    "text/html" -> text,
    "image/jpeg" -> byteArray,
    "image/jpg" -> byteArray,
    "image/jpe" -> byteArray,
    "image/bmp" -> byteArray,
    "image/gif" -> byteArray,
    "image/pict" -> byteArray,
    "image/pic" -> byteArray,
    "image/pct" -> byteArray,
    "image/png" -> byteArray,
    "image/svg+xml" -> byteArray,
    "image/svg" -> byteArray,
    "image/tiff" -> byteArray,
    "image/tif" -> byteArray,
    "image/tiff-fx" -> byteArray,
    "image/x-icon" -> byteArray,
    "image/ico" -> byteArray,
    "image/x-portable-anymap" -> byteArray,
    "image/pnm" -> byteArray,
    "image/x-portable-bitmap" -> byteArray,
    "image/pbm" -> byteArray,
    "image/x-portable-graymap" -> byteArray,
    "image/pgm" -> byteArray,
    "image/x-portable-pixmap" -> byteArray,
    "image/ppm" -> byteArray,
    "image/x-quicktime" -> byteArray,
    "image/qtif" -> byteArray,
    "image/qti" -> byteArray,
    "image/x-rgb" -> byteArray,
    "image/rgb" -> byteArray,
    "image/x-xbitmap" -> byteArray,
    "image/xbm" -> byteArray,
    "image/x-xpixmap" -> byteArray,
    "image/xpm" -> byteArray,
    "image/x-xwindowdump" -> byteArray,
    "image/xwd" -> byteArray
  )

}//Mime
*/
