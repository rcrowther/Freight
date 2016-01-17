package freight
package interface
package gui
package jswing

import swing._



/** A view handling a freight id.
  *
  * Handles id data with the same methods as an [[ObjectView]].
  *
  * The class can append itself, and a label, into any scala-wrapped swing container.
*
* Strings recovered or given need to be validated. If a `Long` is needed, `idAsEither` is usually an appropriate method.
*
  * Used in GUIs which need to handle non text-based data.
  */
//   * @param editMethod a method to be fired when the field is edited
//  *  (ENTER pressed). This is passed the untreated string from the id field.
class IdView()
    extends  TextField(20)
    with gui.generic.FieldModifiableComponent
{
  maximumSize = limitYFixed(20)
  xLayoutAlignment = 0.0

  def get: String = {text}

  def idAsLong: Option[Long] = idAsOption(get)

  def set(v: String) = {text = v}

  def set(id: Option[Long]) = {
    if(id == None) clear()
    else text = id.get.toString
  }

  def clear() = {text = ""}

  def enable = {enabled = true}

  def disable = {enabled = false}

  /** Sets an action on the id field.
    *
    * A non-templated fudge which should not be relied upon
    * outside the jswing interface.
    */
  // NB: Todo this systemicly, the model must return the component, or
  // enable where it can?
  def idAction(f:() => Unit){
    val action: Action = new Action("id edit done") {
      def apply(): Unit = f()
    }

    this.action=(action)
  }

/** Append the field and a label to a scala swing container.
*
  * @param contents of a scala-swing panel.
*/
  def appendTo(contents: collection.mutable.Buffer[Component]){
    contents += LayoutMultiTaker.label("id")
    contents += this
  }

}//IdView




