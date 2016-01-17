package freight.core.objects.mutable




/** Templates additional features for string builders writing to interfaces.
  *
  * The methods record the id selected. It also records whether the builder believed, from the
* configuration given, if the build was for an insert or an update. 
  *
  */
//Somewhat annoying, but may find plenty usage R.C.
trait WritingStringBuilder
    extends GenStringBuilder[String]
{
  /** Reports if the build was for a new object.
    *
    * True for `insert` and `append`, false for `update`.
    */
  def wasNew: Boolean


  /** Reports the id used for insert or update.
    *
    * May not be the id requested, depending on overrides and
    * automatic id generation.
    */
  def generatedId : String

}//WritingStringBuilder
