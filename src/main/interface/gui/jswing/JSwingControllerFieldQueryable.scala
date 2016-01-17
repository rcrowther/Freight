package freight
package interface
package gui
package jswing

import swing.Publisher

import jswing.event.StatusMessage



/** Wrap a controller for a jswing interface.
  *
  * Emits swing events when the controller is called. Changes the
  * parameters to optional, which is easer to use with jswing
  * components.
  */
class JSwingControllerFieldQueryable(
  notifyOfStatus : Boolean,
  ctl: TakableFieldQueryable,
  fieldIdxs: Seq[Int]
)
    extends JSwingController[Giver, Taker](
  notifyOfStatus,
  ctl
)
{





  def elemFields(
    oidO: Option[Long],
    f: (Giver) => Unit
  ) : Boolean =
  {
    var ok = validId(oidO)
    if (ok) {
      val oid = oidO.get
      if(!ctl.apply(fieldIdxs, oid, f)) {
        ok = false
      }
      // notify here as otherwise overwrites invalid id notifications.
      readNotify(ok)
    }
    // Win/loose, set the id
    // In some views, this duplicates action handling e.g. in the object view
    // the view reads te id anyway. But asserting here adds robustness
    // (e.g. modifying out of sync ids from data).
    
    ok
  }



}//JSwingControllerFieldQueryable
