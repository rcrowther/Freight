package freight
package core
package objects
package generic



/** Templates methods for poping an external interface to another external interface.
  * 
  * @define obj enabled object
  */
trait CompanionMeta
{

  /** Defines a string title for this $obj.
    *
    * Used around interfaces for titling purposes - such
    * as in auto-building GUIs.
    */
  def titleString : String

  private def mkColumnMap(descriptiveGiver: DescriptiveGiver)
      : Seq[(Int, String, String)] =
  {
    val b = new FieldMetaDataBuilder()
    descriptiveGiver(b)
    b.result()
  }

  /** Maps column position to field name and description.
    *
    * Presented as a seq, so it is reliably iterable. Use `toMap` on
    * the result if the data is used but there is no neeed to iterate.
    *
    * @return a seq of tuples of column position (from zero) and
    *  column/field name.
    */
  lazy val columnMap: Seq[(Int, String, String)] = mkColumnMap(descriptiveGiver)

  private def mkColumnTypeMap(descriptiveGiver: DescriptiveGiver)
      : Seq[(String, TypeMark)] =
  {
    val b = new FieldNameTypeBuilder()
    descriptiveGiver(b)
    b.result()
  }

  /** Maps column field name to type.
    *
    * Presented as a seq, so it is reliably iterable. Use `toMap` on
    * the result if the data is used but there is no neeed to iterate.
    *
    * @return a seq of tuples of column position (from zero) and
    *  column/field name.
    */
  lazy val columnTypeMap: Seq[(String, TypeMark)] = mkColumnTypeMap(descriptiveGiver)

  private def mkFieldNameSeq(descriptiveGiver: DescriptiveGiver)
      : Seq[String] =
  {
    val b = new FieldNameBuilder()
    descriptiveGiver(b)
    b.result()
  }

  /** Seq of field names.
    *
    * Presented as a seq, so it is reliably iterable.
    *
    * @return a seq of column/field names.
    */
  lazy val fieldNames: Seq[String] = mkFieldNameSeq(descriptiveGiver)

  /*
   /** Defines the prefix of this object's `toString` representation.
   *
   *  @return  a string representation which starts the result of `toString`
   *           applied to this $coll. By default the string prefix is the
   *           simple name of the collection class $coll.
   */
   def stringPrefix : String = {
   var string = repr.getClass.getName
   val idx1 = string.lastIndexOf('.' : Int)
   if (idx1 != -1) string = string.substring(idx1 + 1)
   val idx2 = string.indexOf('$')
   if (idx2 != -1) string = string.substring(0, idx2)
   string
   }
   */

  /** A method giving field metadata about this $obj.
    */
  def descriptiveGiver: DescriptiveGiver

  /** Selects fields and types for this $obj from a string taker.
    *
    * Allows one interface to be plugged directly into another.
    */
  def stringFieldBridge : (Taker, Giver) => Unit

  /** Selects fields and types for this $obj from a multi taker.
    *
    * Allows one interface to be plugged directly into another.
    */
  def multiFieldBridge : (Taker, Giver) => Unit

  
}//CompanionMeta



object CompanionMeta {

  private val emptyThing = new CompanionMeta {
    def titleString : String = "empty Meta"
    def descriptiveGiver: freight.DescriptiveGiver =
      throw new FreightException("descriptiveGiver Method on empty meta disabled")
    def multiFieldBridge: (freight.Taker, Giver) => Unit =
      throw new FreightException("multiTransformer Method on empty meta disabled")
    def stringFieldBridge: (freight.Taker, Giver) => Unit =
      throw new FreightException("stringTransformer Method on empty meta disabled")
  }

  /** An empty meta.
    *
    * Useful, as an interface can still convert to and from 
    * objects without a CompanionMeta.
    */
  def empty: CompanionMeta = emptyThing

}//CompanionMeta
