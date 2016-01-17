package freight
package interface
package gui
package jswing
package generic


import swing._

import scala.swing.TabbedPane.Page




/** Simple component wrap with mainframe and tabs.
  *
  * The main panel contains a vertical BoxPanel, which is also
  * accessible. The TabbedPane is the only component in the panel.
  */
//TOCONSIDER: why is this in avertical box?
class TabbedBox
    extends  BoxPanel(Orientation.Vertical)
{

  // Unless otherwise
  xLayoutAlignment = 0.0

  //-----------------------------------------------------------
  // Components
  //-----------------------------------------------------------


  /** Select the tabbed plane.
    */
  val tabPlane = new TabbedPane()
  // Unless otherwise
  tabPlane.xLayoutAlignment = 0.0

  // Please stop *that* (folding tabs)
  tabPlane.tabLayoutPolicy = TabbedPane.Layout.Scroll




  //-----------------------------------------------------------
  // Methods
  //-----------------------------------------------------------


  /** Adds a new page.
    *
    * Used where the main intent is display (so maximum area
    * preferred).
    */
  def page(title: String, c: Component) {
    tabPlane.pages += new Page(title, c)
  }

  /** Adds a new page with a border on the component.
    *
    * Used where there are active components (so spacing the GUI from
    * others aids usability).
    */
  def pageWithBorder(title: String, c: Component) {
    c.border = panelBorder
    tabPlane.pages += new Page(title, c)
  }

  /** Add page with horizontal glue.
    *
    * Used where the main intent is display (so maximum area
    * preferred).
    */
  def centredPage(title: String, c: Component)  {
    tabPlane.pages += new Page(title, centred(c))
  }

 /** Add page with horizontal glue.
    *
    * Used where there are active components (so spacing the GUI from
    * others aids usability).
    */
  def centredPageWithBorder(title: String, c: Component)  {
    val ctr = centred(c)
    ctr.border = panelBorder
    tabPlane.pages += new Page(title, ctr)
  }

  /** Returns the tab index of a component.
    *
    * The component must be a tab. This component would be usually be
    * provided by a `SelectionChanged` event.
    *
    * @param component the tab to find the index.
    */
  def tabIndex(component: Component): Int = {
    val tPane: TabbedPane = component.asInstanceOf[TabbedPane]
    tPane.selection.index
  }

  contents += tabPlane

}//TabbedBox

