package freight
package core
package collection




/** Templates a giver collection with field query methods.
  *
  * The trait templates field-based queries on a
  * [[GiverTakable]].
  *
  * This trait does not template for object generation. This means it can be used for collection APIs with no interest in the handling of class objects.
  *
* This giver and taker only version of a queryable interface can be used
* to implement internal APIs. If field-based querying is not possible,
* use [[GiverTakable]].
  *
  * @define coll collection
  */
trait TakableFieldQueryable
extends GiverTakable
with GenFieldQueryable
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


}//GiverQueryable
