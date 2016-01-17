package freight
package interface
package complex
package generic

import core.objects.Title
import scala.collection.mutable.ArrayBuffer


import core.collection.FieldQueryable



/** Templates a connector for complex collections.
  *
  * Uses the builtin connection.
  *
  */
trait GenConnectorWithMarks
extends GenConnector
 {



  /** Provides the collection controlling the marks.
    *
    * Internal, used by the controlling collection.
    */
  def marksBaseCollection()
      : TakableFieldQueryable =
  {
    fieldQueryableCollection(
      namedCollectionName("marks"),
      Title
    )
  }

  /** Provides the marks collection itself.
    *
    * Used for gui and rendering purposes.
    */
  def marks() : TakableFieldQueryable


}//GenConnectorWithMarks
