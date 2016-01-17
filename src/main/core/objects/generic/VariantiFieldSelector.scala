package freight
package core
package objects
package generic





trait VariantFieldSelector {

/** Selects fields from a giver to give to a taker.
*
* @param fieldData a seq of field name to field type tuples.
* @param b a taker to revieve data.
* @param g a giver from which to take data.
*/
  def apply(fieldData: Seq[(String, TypeMark)], b: Taker, g: Giver)

}//VariantFieldSelector
