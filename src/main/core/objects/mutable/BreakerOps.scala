package freight.core.objects.mutable

/** A trait containing base breaker ops.
*
 * @tparam From the type of source the breaker accepts.
*/
// TODO: Add the To return
trait BreakerOps[-From] extends Any {

  /** Reset the readers in this breaker.
   * After execution of this method the breaker will restart reading from the source.
* (Some sources may disallow a reset, so this method will be disabled).
   */
 // def reset()

  /** Replaces the source in this breaker.
   * After execution of this method the breaker will contain a new source.
   * @param src the source to be inserted.
   */
  def reload(src: From)
//: To

//mapResult?
}
