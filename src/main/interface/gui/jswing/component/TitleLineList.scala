package freight
package interface
package gui
package jswing
package component

import swing._



/** A list shown as labels stacked in a vertical box.
  *
  * Sets the list to be a simple list with titles. The form of the
* listings is known, so an `append` can be added.
  *
  * Emits `SelectionChanged`.
  *
  * @define cpnt title list
  */
class TitleLineList
    extends GenTitleLineList

