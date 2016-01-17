package freight
package core
package util

import java.util.{TimeZone, Date}
import java.text.SimpleDateFormat

/** Utilities for handling time.
 */
object DateTime{
  
  /** Format a Date.
   */
  def formatDate(
date: Date,
 format: String,
 timeZone: TimeZone = TimeZone.getTimeZone("GMT")
)
 {
    val df = new SimpleDateFormat(format)
    df.setTimeZone(timeZone)
    df.format(date)
  }

  /** Format a Date assuming GMT.
   */
  def formatDate(
date: Date,
 format: String
)
 {
    val df = new SimpleDateFormat(format)
    df.setTimeZone(TimeZone.getTimeZone("GMT"))
    df.format(date)
  }

}//DateTime
