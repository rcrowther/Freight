package freight
package interface
package gui
package jswing
package generic

import swing._
import collection.mutable.ArrayBuffer

import java.awt.Font
import java.awt.Color


import collection.mutable.Buffer

/** abstract model of a count.
*/
//TODO: Should be the label, really?
class CountDisplay()
{
  private var count: Long = 0


 def set(sz : Long) {
count = sz
    countDisplay.text = count.toString
}

  def inc(sz : Long) {
    count += sz
    countDisplay.text = count.toString
  }

  def inc(): Unit = inc(1)

  def dec(sz : Long) {
    count -= sz
    countDisplay.text = count.toString
  }

  def dec(): Unit = dec(1)



  val countDisplay = new Label(count.toString)

def contentsAppend(contents: Buffer[Component])
{
    contents.append( new Label("n:") )
    contents.append(countDisplay)
}


def appendTo(sb: SimpleStatusbar)
{
    sb += tightHorizontalSpacer
    sb += new Label("n:")
    sb += countDisplay
    sb += tightHorizontalSpacer
}
}//CountDisplay
