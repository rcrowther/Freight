package freight
package interface
package gui
package jswing
package generic


import swing._




/** Simple status bar with a display to the left.
*
* Add extra components and spacing with calls to the underlying class [[SimpleToolbar]]
*/
class SimpleStatusbar()
    extends SimpleToolbar
{

  val display = new StatusLabel



    //-----------------------------------------------------------
    // Main build
    //-----------------------------------------------------------

    // NB: All of the following needed to prevent collapse,
    // if not making Swing displays sensible...
    this += horizontalBrace
    this.space()
    this += display
    this.glue()



}//SimpleStatusbar

