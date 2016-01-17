package freight
package interface
package gui
package jswing
package component

import swing._

import java.awt.event.MouseAdapter
import java.awt.Color

import jswing.event.{
SelectedIdChanged,
SelectionChanged
}

/** A list row with id data.
  *
  * For use with a [[LineListWithId]].
  */
trait ListRowWithId[T]
    extends ListRow[T]
{
  self: T =>

  def id: Long

}
