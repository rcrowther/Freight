package freight.core.objects.mutable

/** A trait containing base builder ops.
*
 * @tparam To the type of destination the builder produces.
*/
trait BuilderOps[+To] extends Any {

  /** Clears the contents of this builder.
   *  After execution of this method the builder will contain no elements.
   */
  def clear()

  /** Produces a collection from the added elements.
   *  The builder's contents are undefined after this operation.
   *  @return a collection containing the elements added to this builder.
   */
  def result(): To

//mapResult?
}
