package freight
package interface
package complex
package generic

import core.objects.Title
import scala.collection.mutable.ArrayBuffer


import core.collection.FieldQueryable



/** Templates a connector for complex collections.
  *
  * Includes some utilties and prebuilt marks collection.  One
  * connection is provided, more can be added if necessary.
  *
  *  
  * Will need at least one,
  *  {{{
  *    def close : () => Boolean
  * 
  *  // Delete the collection referred to by a mark.
  *  def deleteCollection(
  *      collectionId: Long
  *    )
  *  }}}
  * methods defining.
  *
  */
trait GenConnector {
  def conn: Connection



  /** Represents the class name as a string.
    *
    * Should be the name of the complex collection and capitalized
    * e.g. 'Set'
    */
  def classString : String

  /** Represents the class name as a string.
    *
    * Should be the name of the complex collection lowercase, no
    * separator characters e.g. 'set'.
    *
    * Used to construct names for the interface, e.g. 'f_set_marks' 
    */
  def machineClassString : String


/** Logs if the collection is new.
*/
  protected def isNewNotify(isNew: Boolean) {
    if (isNew)
    {
      log(
        //"new Set marks collection collectionName:${coll.collectionName}"
        s"new $classString marks collection"
      )
    }
  }

  /** Names a collection by id.
    *
    * e.g. 'f_map_5'
    */
  protected def idCollectionName(collectionId: Long)
      : String =   s"f_${machineClassString}_$collectionId"
  
  /** Names a collection by string name.
    *
    * e.g. 'f_map_marks'
    */
  protected def namedCollectionName(collectionName: String)
      : String = s"f_${machineClassString}_$collectionName"

  /** Names a collection by name and id.
    *
    * e.g. 'f_map_keys5'
    */
  protected def namedIdCollectionName(
    collectionName: String,
    collectionId: Long
  )
      : String = s"f_${machineClassString}_$collectionName$collectionId"

  protected def fieldQueryableCollection(
    idName: String,
    meta: CompanionMeta
  )
      : TakableFieldQueryable =
  {
    val coll = conn.fieldQueryCollection(
      idName,
      meta
    )

    isNewNotify(coll.isNew)

    coll
  }





}//GenConnector
