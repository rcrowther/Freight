package freight
package interface
package gui
package jswing

import swing._
import event._

import component.{LineListWithId, ListRowWithId, GenTitleLineList}



/** Base trait for selectable titled views.
  * 
  * The signatures are known, so view methods can be realized.
  *
  * Needs `read' `create' methods to be realized.
  *
  * Emits `SelectedIdChanged`, `ObjectModified`, `StatusMessage`, and
  * `NeedsResizing`.
  */
trait FreightListReader
    extends FreightComponent
{



  //------------------
  //-- Data actions --
  //------------------

  /** Action for reading.
    *
    * Always the same signature, so can be templated here.
    */
  //def listReader: ((Long, String) => Unit) => Unit


  /** Action for element reading.
    *
    * Used for updates and appends.
    *
    * Always the same signature, so can be templated here.
    */
  //def elementReader: (Long) => Option[String]



  //NB: Still do not know the form of insert and append calls







}//FreightListReader
