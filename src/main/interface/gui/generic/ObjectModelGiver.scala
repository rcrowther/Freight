package freight
package interface
package gui
package generic


/** Gets a freight object from a model.
  *
  */
//NB:  The model can be a giver, but we keep it clean.
final class ObjectModelGiver(
  model: Array[FieldModifiableComponent]
)
    //extends StringGiver
    extends GiverForString
{
  private var i = 0

  private def get : String = {
    val v =  model(i).get
    i += 1
    v
  }

  def booleanStr : String = get
  

  def shortStr : String = get

  def intStr : String = get

  def longStr : String = get

  def floatStr : String = get

  def doubleStr : String = get

  def stringStr : String = get

  def binaryStr : String = get

  def textStr : String = get

  def timeStr : String = get

  def timestampStr : String = get

  def localeStr : String = get

  def reset() { i = 0 }

}//ObjectModelGiver
