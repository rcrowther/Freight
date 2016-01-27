package freight
package interface
package gui
package jswing
package event


import swing._
import event._


object NeedsResizing2 {
def apply() = new NeedsResizing2()
}

/** Emitted whenever a component changes size.
*
* Very simple. The class carried will likely be
* the class of the component, but this is irrelevant.
*/
class NeedsResizing2() extends Event
