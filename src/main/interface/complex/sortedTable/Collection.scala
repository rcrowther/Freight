package freight
package interface
package complex
package sortedTable

import core.objects.WeighedTitle
import collection.mutable.ArrayBuffer



/*
 import freight._
 import interface.complex.sortedTable.Collection

 val st = Collection.testCollection
 st.columnIds("title", false)
 st.isValidBatchIdx(4, 3)
 val bld = new interface.html.ObjectBuilder()
 st.forSlice( fieldIndices = Seq(1,2,3), sortFieldIdx = 2, batchSize = 4, batchIdx = 1, b = bld)
 bld.result()
 */


/** Returns objects sorted by column and in batches.
  * 
  * Offers a simplified insert/delete interface, and extra methods to
  * retrieve the elements sorted by the colum in batches.
  * 
  * The table with data to be sorted is automatically created or
  * opened on instanciation of this class.
  *
  * @define obj object
  *
  * @param colName name of the collection to be opened.
  * @param sortCols names of the colums to be sortable (not all the
  *  columns are needed)
  */
//parentingColl = term:termId? termParent:termId...
// TODO: Rename as Collection?
class Collection(
  val collectionName:  String,
  val collection: FieldQueryable
)
{


  //----------
  //- Cache --
  //----------

  private val colDetails: Seq[(Int, String, String)] = collection.meta.columnMap
/*
  private val nameToColIdx: Map[String, Int] = colDetails.map{ colData =>
    (colData._2 -> colData._1)
  }.toMap
*/

  private val nCols: Int = colDetails.size
  //
  private val needsReloading = new Array[Boolean](nCols)
  // This sets all needsReloading elements to true
  clearCache()

  private val idCache = new Array[Seq[Long]](nCols)
  for(n <- 0 until nCols){
    idCache(n) = Seq.empty[Long]
  }


  /** Produces a seq of all ids in this collection, sorted by a column.
    *
    * Uses the id cache if possible. Tests the validity of colName.
    *
    * @param sortCol name of the column to be sorted on.
    * @param reverse if true, reverses the results (DESC) 
    */
  def columnIds(sortFieldIdx: Int, reverse: Boolean)
      : Seq[Long] =
  {
/*
    val colIdx = nameToColIdx.get(sortCol).getOrElse {
      error(s"Colname not in collection: sortCol $sortCol collection name: $collectionName")
    }
*/
log(s"needsReloading(sortFieldIdx):${needsReloading(sortFieldIdx)}")

    if (needsReloading(sortFieldIdx)) {
      val ids: Option[Seq[Long]] = collection.sortedIndices(sortFieldIdx)
      if (ids == None) {
        elErrorIf(ids == None, s"SortedIds requested from storage can not be found: sortFieldIdx $sortFieldIdx collection name: $collectionName")
      }
      else idCache.update(sortFieldIdx, ids.get)

      needsReloading.update(sortFieldIdx, false)
    }

    if (!reverse) idCache(sortFieldIdx)
    else idCache(sortFieldIdx).reverse
  }


  /** Clear the cache.
    *
    * Brute force, but guaranteed to work and return updated results
    * after Collection modifications e.g. `removeWeighedTitle`.
    */
  def clearCache() {
    for(n <- 0 until nCols){
      needsReloading.update(n, true)
    }
  }




  //------------
  //- Objects --
  //------------

  /** Tests if a batch index will return results.
    *
    * Can be used to test if GUIs offer forward/back buttons, etc.
    * Runs from zero upwards (-VE batchIdx is also rejected).
    */
  def isValidBatchIdx(
    batchSize: Int,
    batchIdx: Int
  )
      : Boolean =
  {
    (batchSize * batchIdx) < collection.size
  }

  /** Apply the builder to a slice of available elements.
    *
    * The parameter format here reflects the idea of 'paging' groups
    * of data e.g.
    * {{{
    * // html.ObjectBuilder constructs simple HTML
    * // for each element in the sliced list.  
    * val b = new html.ObjectBuilder()
    * forslice("poems", 8, 3, b)
    * b.result()
    * }}}
    * would be page three (eight items per page), of a slice of the
    * "poem" collection.
    *
    * @return true if anything was built, else false (e.g. if  `batchIdx` is out of range, or the db read fails). 
    */
  def forSlice(
fieldIndices: Seq[Int],
    sortFieldIdx: Int,
    batchSize: Int,
    batchIdx: Int,
    b: core.iface.ManyObjectsTaker
  )
      : Boolean =
  {
    val ok = isValidBatchIdx(batchSize, batchIdx)
//val colNames: Seq[String] = Seq("id", "language", "title")

    if (ok) {
      // Defo. a Seq - want to preserve order :)
      val idxs: Seq[Int] = columnIds(sortFieldIdx, false).slice(
        (batchSize * batchIdx),
        (batchSize * (batchIdx + 1))
      ).map(_.toInt)

      log(s"building idxs $idxs")
      collection.foreach(fieldIndices, idxs, b)
    }
    ok
  }


  //------------
  //- General --
  //------------

  override def toString()
      : String =
  {
    val b = new StringBuilder("Collection(collectionName:")
    b append collectionName
    b ++= ", object count:"
    b append collection.size
    b += ')'
    b.result
  }

}//Collection



object Collection {

  def testCollection: Collection ={
    import freight.interface.db.sqliteJDBC.SyncConnection

    val db = SyncConnection.testDB
    Collection(db, "poems", objects.Paper)
  }


  /** Creates a sorted table from an open human collection.
    */
  def apply(
    collectionName:  String,
    collection: FieldQueryable
    //sortCols : Seq[String]
  )
      : Collection =
  {

    new Collection(
      collectionName,
      collection
      //sortCols
    )
  }

  /** Creates a sorted table from an newly opened human collection.
    */
  def apply(
    conn: Connection,
    collectionName:  String,
    meta: CompanionMeta
    //sortCols : Seq[String]
  )
      : Collection =
  {
    val collection = conn.fieldQueryCollection(collectionName, meta)

    if (collection.isNew) {
      error(s"Underlying collection must have previously existed to use sortedTable.Collection  collectionName:$collectionName")
    }
    else {
      log(s"existing Collection $collectionName")
    }

    log(s"opening Collection $collectionName")
    new Collection(
      collectionName,
      collection
      //sortCols
    )
  }

}//Collection
