package freight
package core
package collection




/** Templates a giver collection with field query methods.
  *
This trait implements a proxy for field-queryable collections. It is a [[TakableFieldQueryable]] collection. It forwards all calls to a [[FieldQueryable]] collection.
  *
  *
* The purpose of the proxy is to make building of internal APIs easier and cleaner. Any [[FieldQueryable]] collection, usually an interface collection, can be added to the trait. The trait is templated to a [[TakableFieldQueryable]] (which has less methods than [[FieldQueryable]], but the same overall functionality - it lacks addittional methods for various datatypes, so is for internal use). The trait forwards all calls (all calls which make sense to the reduced number of methods).  
  *
* For example, two collections need to be controlled, one with labels, one with data.
* Mostly, the collection will deal in labels, so proxy that connection. Now override proxy methods with side effect code, such as adding data with new labels, and deleting data with deleted labels. This is easier with the reduced method set of [[TakableFieldQueryable]], and also easier because the proxy is handling most of the method calls, the coder only need adapt those with side-effects.
* 
  * @define coll proxy
  */
trait TakableFieldQueryableProxy
extends TakableFieldQueryable
{
 def coll: TakableFieldQueryable

  // Members declared in freight.core.collection.GiverQueryable
  def apply(fieldIdxs: Seq[Int],idxs: Seq[Long],f: freight.Giver => Unit): Boolean = coll.apply(fieldIdxs,idxs,f)
  def apply(fieldIdxs: Seq[Int],idx: Long,f: freight.Giver => Unit): Boolean = coll.apply(fieldIdxs,idx,f)
  def findLong(fieldIdxs: Seq[Int],searchFieldIdx: Int,searchData: Long,f: freight.Giver => Unit): Boolean = coll.findLong(fieldIdxs,searchFieldIdx,searchData,f)
  def findString(fieldIdxs: Seq[Int],searchFieldIdx: Int,searchData: String,f: freight.Giver => Unit): Boolean = coll.findString(fieldIdxs,searchFieldIdx,searchData,f)
  def foreach(fieldIdxs: Seq[Int],sortFieldIdxs: Seq[Int],f: freight.Giver => Unit): Boolean = coll.foreach(fieldIdxs,sortFieldIdxs,f)
  def updateInt(fieldIdx: Int,data: Seq[(Long, Int)]): Boolean = coll.updateInt(fieldIdx,data)
  def updateString(fieldIdx: Int,data: Seq[(Long, String)]): Boolean = coll.updateString(fieldIdx,data)
  def variantFieldSelector: freight.core.objects.generic.VariantFieldSelector = coll.variantFieldSelector

  // Members declared in freight.core.collection.GiverTakable
  def fieldBridge: freight.FieldBridge = coll.fieldBridge
  def meta: freight.CompanionMeta = coll.meta

  // Members declared in freight.core.collection.Transferable
  def -(id: Long): Boolean = coll.-(id)
  def +(g: freight.core.iface.Giver): Long = coll.+(g)
  def ~(id: Long,g: freight.core.iface.Giver): Boolean = coll.~(id,g)
  def ^(id: Long,g: freight.core.iface.Giver): Boolean = coll.^(id,g)
  def addString(b: StringBuilder): StringBuilder = coll.addString(b)
  def apply(id: Long,f: freight.core.iface.Giver => Unit): Boolean = coll.apply(id,f)
  def clear(): Boolean = coll.clear()
  def foreach(f: freight.core.iface.Giver => Unit): Boolean = coll.foreach(f)
  def genreString: String = coll.genreString
  def klassString: String = coll.klassString

override def collectionName : String = "TakableFieldQueryableProxy"

 def isNew: Boolean = coll.isNew
  def size(): Long = coll.size()

}//TakableFieldQueryableProxy
