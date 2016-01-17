package freight
package core
package collection



/** Templates a connection to an inteface
  *
  */
trait Connection
{

  /** Returns a collection.
    */
  //TODO: Should return optional
  def collection(
    colName: String,
    meta: CompanionMeta
  ) : Generator

  /** Returns a collection with an empty Meta object.
    * 
    * These collections can acccept and return objects, but not pass
    * data directly to another external interface.
    */
  def collection(
    colName: String
  )
      : Generator =
  {
    collection(colName, CompanionMeta.empty)
  }

  /** Returns a refMap.
    *
    * @return if available, a refMap, else throws an error.
    */
  //TODO: Should return optional
  def refMap(
    colName: String
  ) 
: RefMap =
  {
    error(
      "refMaps not available for this connection connection: $conn"
    )
  }


  /** Returns a refMap.
    *
    * @return if available, a refMap, else throws an error.
    */
  //TODO: Should return optional
  def binaryCollection(
    colName: String
  ) 
: BinaryTakable =
  {
    error(
      "binary collections not available for this connection connection: $conn"
    )
  }


  /** Returns a field query collection.
    *
    * @return if available, a fieldQueryCollection, else throws an error.
    */
  //TODO: Should return optional
  def fieldQueryCollection(
    collectionName: String,
    meta: CompanionMeta
  )
      : FieldQueryable =
  {
    error(
      "field query extensions not available for this connection connection: $conn"
    )
  }

  /** Deletes a collection.
    *
    * Delete data and all support structures for a collection.
    * Similar in action to `clean` in Collection.
    */
  def -(
    collectionName: String
  ) : Boolean

  /** Closes this connection.
    *
    * This method should be called before any code using freight is
    * shut down. Failure to do this can leave the code connectors
    * connect to in an undertermined, and perhaps damaged, state.
    *
    * Recalling this method has no effect.
    *
    * After this method is called, the connector can not be reused. To
    * reconnect to the resource, a new connection is required.
    */
  def close() : Boolean


/** Defines a string representing the code backing the collection.
*
* Should be capitalized. Often concatenated with `klassName`. 
* Used in toString representations and error reports.
*
* @return a string representing the backing code of this $coll.
*/
def genreString : String

/** Defines a string representing the classname of the collection.
*
* Should be capitalized. Often concatenated with `genreString`. 
* Used in toString representations and error reports.
*
* @return a string representing the class name of this $coll.
*/
def klassString : String


  /** Appends information about this $conn connection to a string builder.
    *
    * The written text consists of string representations of
    * identifying fields in this collection. The fields are appended
    * in the format name, a colon, ',' separator.
    *
    * @param b the string builder to which elements are appended.
    */
  //TODO: Ought to have SepStringBuilder or the like in here someplace.
  def addString(b: StringBuilder) : StringBuilder


  /** Converts data about this connection to a string.
    * 
    * The string returned will not be a representation of the
    * collections within the connection, but data about the collection
    * object itself.
    *
    * @return a string representation of data about the connection.
    */
  override def toString()
      : String =
  {
    val b = new StringBuilder()
    b ++= genreString
    b ++= klassString
    b ++= "Connection("
    addString(b)
    b ++= ")"
    b.result()
  }

}//Connection
