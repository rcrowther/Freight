package freight
package interface
package gui
package jswing

import swing.Publisher



/** Base trait for views.
  * 
  * Templates notification helper methods for actions.
  *
  * At this point, the signature and sources for actions are 
  * undefined.
  *
  * The trait also allows status notifications to be switched off,
  * using `notifyOfStatus`. In that case, the only signal emitted is
  * the display signal `NeedsResizing`.
  */
trait FreightView
    extends Publisher
{

  /** Switches status notifications on.
    *
    * If false, the only signal published is `NeedsResizing`.
    */
  def notifyOfStatus : Boolean



  protected def selectedId: Option[Long]



  //--------------------
  //-- Action helpers --
  //--------------------




}//FreightView
