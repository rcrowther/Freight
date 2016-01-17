package freight
package core
package collection




/** Templates a collection with field query methods.
  *
  * The trait templates some field-based queries.
  *
  * Despite field selection, the queries are very general, usually
  * applying to a whole column or object. The queries are mainly
  * selections, intended for user interfaces. No creates or deletes
  * are implemented (use [[Collection]]). Some one-field updates are
  * templated, which target specific ids.
  * 
  * Some external interfaces, usually those with advanced query
  * operations, can query fields naturally, but others can not (they
  * can only load or recieve a whole object). The interface should
  * usually only be implemented for collections with natural
  * field-based querying (such as databases).
  *
  * Field based queries make no sense on any other collection tahn those handling [[Giver]] and [[Taker]]. For example, the trait will not be found in [[BinaryTakable]].
*
  * @define coll collection
  */
trait GenFieldQueryable
{


  def meta: freight.CompanionMeta

  /** Selects fields to transfer data between givers and takers.
    *
    * Should be defined from `meta` as
    * [[core.objects.generic.VariantStringFieldSelector]] or
    * [[core.objects.generic.VariantMultiFieldSelector]], depending on
    * the natural handling of the collection.
    */
  def variantFieldSelector :  core.objects.generic.VariantFieldSelector

  /** Applies a giver function to specific fields at an index.
    *
    * The function will require a custom field selector or hand
    * dissembly.
    *
    * The base implementation derives from the multi-field selecting
    * method. If a querybuilding interface can implement single
    * record/object retrieval more efficiently, then the
    * implementation should be overriden.
    *
    * @param fieldIdxs indexes of the fields to select
    * @param idx the id of the element to select.
    * @param f the function to apply to the selected data.
    */
  def apply(
    fieldIdxs: Seq[Int],
    idx: Long,
    f: (Giver) => Unit
  ) : Boolean


  /** Applies a giver function to specific fields and indices.
    *
    * The function will require a custom field selector or hand
    * dissembly.
    *
    * @param fieldIdxs indexes of the fields to select
    * @param idxs the ids of the elements to select.
    * @param f the function to apply to the selected data.
    */
  def apply(
    fieldIdxs: Seq[Int],
    idxs: Seq[Long],
    f: (Giver) => Unit
  ) : Boolean 


  /** Applies a taker to specific field and indices.
    *
    * The taker must accept the specified field types (if necessary, in order).
    *
    * @param fieldIdxs indexes of the fields to select
    * @param idx the id of the element to select.
    * @param f the function to apply to the selected data.
    */
  def apply(
    fieldIdxs: Seq[Int],
    idxs: Seq[Long],
    t: Taker
  )
      : Boolean =
  {
    val fieldData : Seq[(String, TypeMark)] = fieldIdxs.map( meta.columnTypeMap(_))

    def func(g: Giver) {
      variantFieldSelector(
        fieldData,
        t, g
      )
    }
    apply(
      fieldIdxs,
      idxs,
      f = func _
    )
  }



  /** Selects by a string field value then applies a giver function to fields.
    *
    * The function will require a custom field selector or hand
    * dissembly.
    *
    * @param fieldIdxs indexes of the fields to select
    * @param searchField the ids of the field to search in.
    * @param searchData the data to search for.
    * @param f the function to apply to the selected data.
    */
  def findString(
    fieldIdxs: Seq[Int],
    searchFieldIdx: Int,
    searchData: String,
    f: (Giver) => Unit
  ) : Boolean

  /** Selects by a long field value then applies a giver function to fields.
    *
    * The function will require a custom field selector or hand
    * dissembly.
    *
    * @param fieldIdxs indexes of the fields to select
    * @param searchField the ids of the field to search in.
    * @param searchData the data to search for.
    * @param f the function to apply to the selected data.
    */
  def findLong(
    fieldIdxs: Seq[Int],
    searchFieldIdx: Int,
    searchData: Long,
    f: (Giver) => Unit
  ) : Boolean

  /** Produces a seq of all indices in this collection.
    *
    * Ordering is always ASC. To get descending order, use Scala
    * `reverse` on the return.
    *
    * The intention is mainly to construct chains of ids from which
    * batches and trees can be derived, and which can be efficiently
    * cached.
    *
    * This method
    * should be overridden in many implementations, as more efficient
    * implementations will exist.
    * 
    * @return optionally return all indices sorted according to the
    * column given. If errors occur, None.
    */
  def sortedIndices(sortFieldIdx: Int) 
: Option[Seq[Long]] =
  {
    val b = Seq.newBuilder[Long]
    foreach(
      fieldIdxs = Seq(0),
      f = (g: Giver) ⇒ { b += g.long }
    )
    Some(b.result)
  }




  /** Applies a giver method to specific fields.
    *
    * The method is mainly intended to provide human readable data
    * without costly calls loading all objects to memory. The method
    * can be used to transform data to new objects, but this requires
    * a field selector or hand dissembly.
    *
    * @param fieldIdxs a list of field indices to be included in the output.
    * @param sortFieldIdxs a seq of field indices to sort on.
    * @param f the function to be applied.  
    */
  def foreach(
    fieldIdxs: Seq[Int],
    sortFieldIdxs: Seq[Int],
    f: (Giver) ⇒ Unit
  ) : Boolean

  /** Applies a giver method to specific fields.
    *
    * The method is mainly intended to provide human readable data
    * without costly calls loading all objects to memory. The method
    * can be used to transform data to new objects, but this requires
    * a field selector or hand dissembly.
    *
    * @param fieldIdxs a list of field indices to be included in the output.
    * @param f the function to be applied.  
    */
  def foreach(
    fieldIdxs: Seq[Int],
    f: (Giver) ⇒ Unit
  )
      : Boolean =
  {
    foreach(fieldIdxs, Seq[Int](), f)
  }

  /** Applies a taker to specific fields.
    *
    * The method is mainly intended to provide human readable data
    * without costly calls loading all objects to memory. The method
    * can be used to transform data to new objects, but this requires
    * a field selector or hand dissembly.
    *
    * @param fieldIndices a list of field indices to be included in the output.
    * @param indices a list of collection indices to gather data from
    * @param b a taker  
    */
  def foreach(
    fieldIdxs: Seq[Int],
    sortFieldIdxs: Seq[Int],
    t: Taker
  ) 
      : Boolean =
  {
    val fieldData : Seq[(String, TypeMark)] = fieldIdxs.map( meta.columnTypeMap(_))

    def func(g: Giver) {
      variantFieldSelector(
        fieldData,
        t, g
      )
    }

    foreach(
      fieldIdxs,
      sortFieldIdxs,
      func _
    )
  }

  /** Updates a specific int field at indices.
    *
    * @param fieldIdx index of the field to update.
    * @param data sequence of ids paired with the data to update.
    */ 
  def updateInt(
    fieldIdx: Int,
    data: Seq[(Long, Int)]
  ) : Boolean

  /** Updates a specific string field at indices.
    *
    * @param fieldIdx index of the field to update.
    * @param data sequence of ids paired with the data to update.
    */ 
  def updateString(
    fieldIdx: Int,
    data: Seq[(Long, String)]
  ) : Boolean

}//GenFieldQueryable
