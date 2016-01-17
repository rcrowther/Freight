package freight
package core
package collection




/** Templates a collection with field query methods.
  *
  * The trait templates some field-based queries on a
  * [[Generator]].
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
  *
* This is the full API interface. It should be used for external interfaces.
* If field-based querying is not natural, prefer [[Generator]].
*
  * @define coll collection
  */
trait FieldQueryable
extends Generator
with TakableFieldQueryable
{

/*
  // Members declared in freight.core.collection.GenCollection
  def addString(b: StringBuilder): StringBuilder = ???
  def clear(): Boolean = ???
  def genreName: String = ???
  def isNew: Boolean = ???
  def size(): Long = ???

  // Members declared in freight.core.collection.GenFieldQueryable
  def apply(fieldIdxs: Seq[Int],idxs: Seq[Long],f: freight.Giver => Unit): Boolean = ???
  def apply(fieldIdxs: Seq[Int],idx: Long,f: freight.Giver => Unit): Boolean = ???
  def findLong(fieldIdxs: Seq[Int],searchFieldIdx: Int,searchData: Long,f: freight.Giver => Unit): Boolean = ???
  def findString(fieldIdxs: Seq[Int],searchFieldIdx: Int,searchData: String,f: freight.Giver => Unit): Boolean = ???
  def foreach(fieldIdxs: Seq[Int],sortFieldIdxs: Seq[Int],f: freight.Giver => Unit): Boolean = ???
  def meta: freight.CompanionMeta = ???
  def updateInt(fieldIdx: Int,data: Seq[(Long, Int)]): Boolean = ???
  def updateString(fieldIdx: Int,data: Seq[(Long, String)]): Boolean = ???
  def variantFieldSelector: freight.core.objects.generic.VariantFieldSelector = ???

  // Members declared in freight.core.collection.Generator
  def append[A](o: A)(implicit ms: freight.MultiTakerFieldSelect[A]): Long = ???
  def insert[A](id: Long,o: A)(implicit ms: freight.MultiTakerFieldSelect[A]): Boolean = ???
  def update[A](id: Long,o: A)(implicit ms: freight.MultiTakerFieldSelect[A]): Long = ???

  // Members declared in freight.core.collection.GiverTakable
  def fieldBridge: freight.FieldBridge = ???

  // Members declared in freight.core.collection.Transferable
  def -(id: Long): Boolean = ???
  def +(g: freight.core.iface.Giver): Long = ???
  def ~(id: Long,g: freight.core.iface.Giver): Long = ???
  def ^(id: Long,g: freight.core.iface.Giver): Boolean = ???
  def apply(id: Long,f: freight.core.iface.Giver => Unit): Boolean = ???
  def foreach(f: freight.core.iface.Giver => Unit): Boolean = ???
*/

}//FieldQueryable
