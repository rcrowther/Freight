package freight.core.objects.mutable



/** Templates a breaker guaranteeing methods for reading Freight types.
 *  
 * A breaker deconstructs some source incrementally, by taking a source, then extracting elements using various methods.
 *  
 * @tparam From the type of source accepted.
 */
// unused, but may have uses in resettabe lexers, for example
trait GenResetable
{
  /** Reset the readers in this breaker.
   * After execution of this method the breaker will restart reading from the source.
* (some sources may disallow a reset, so this method will be disabled).
   */
  def reset()
}

