package freight
package core
package collection


/** Methods common to all collections.
*
* Mostly reporting.
*
  * @define coll collection
*/
trait GenCollection extends Any 
{

  /** Reports if this $coll was created on opening.
*/
  def isNew : Boolean

  /** Deletes all elements from this $coll.
    *
    * After this method, the interface will remain available.
    */
  def clear() : Boolean

  /** The size of this $coll.
    *
    * For some interfaces, `size` must be calculated, and can be
    * expensive to call. Note also that on some interfaces with
    * infinite sized collections, it may not terminate.
    *
    * @return the number of elements in this collection.
    */
  def size() : Long

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

/** Defines a name for the collection.
*
* Should be lowercase, short, and with no spaces (underscores are ok, but not advised). 
* 
* Used as the id of a collection for interacting with external APIs.
*
* @return a string representing the name of this $coll.
*/
def collectionName : String

  /** Appends information about this $coll to a string builder.
    *
    * The written text consists of string representations of
    * identifying fields in this collection. The fields are appended
    * in the format name, a colon, ',' separator.
    *
    * @param b the string builder to which elements are appended.
    */
  def addString(b: StringBuilder) : StringBuilder

  /** Converts data about this $coll to a string.
    * 
    * The string returned will not be a representation of the elements
    * within the collection, but data about the collection object
    * itself.
    *
    * @return a string representation of data about the collection.
    */
  override def toString()
      : String =
  {
    val b = new StringBuilder()
    b ++= genreString
    b ++= klassString
    b += '('
    addString(b)
    b += ')'
    b.result()
  }

}
