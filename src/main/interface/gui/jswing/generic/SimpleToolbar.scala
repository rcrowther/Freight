package freight
package interface
package gui
package jswing
package generic


import swing._
import event._


import gui.generic._



/** Simple toolbar for Swing.
  *
  */
class SimpleToolbar()
    extends BoxPanel(Orientation.Horizontal)
{
// Unless otherwise
  xLayoutAlignment = 0.0


  def button(text: String) : Button =
  {
    val b = new Button(text)
    contents.append(b)
    b
  }

  def space(): SimpleToolbar = {
    contents.append(buttonSpacer)
    this
  }

  def largeSpace(): SimpleToolbar = {
    contents.append(largeHorizontalSpacer)
    this
  }

  def glue(): SimpleToolbar = {
    contents.append(Swing.HGlue)
    this
  }

  def += (c: Component): SimpleToolbar = {
    contents.append(c)
    this
  }

}//SimpleToolbar

