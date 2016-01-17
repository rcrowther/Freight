package freight
package interface
package complex
package taxonomy

import collection.mutable.ArrayBuffer
import core.objects.WeighedTitle


/*
 import freight._
 import interface.complex.taxonomy.Collection

 val t = Collection.testCollection

 */


/** Collects object references into a user or coder defined collection.
  * 
  * The tables for storing Collection data are automatically created or
  * opened on instanciation of this class.
  *
  * Once inserted, terms can not be removed from the Collection exxcept
  * by deletion. This is deliberate, because trying to unlink a
  * taxonomy heirarchy becomes complicated by residual links.
  *
  * @param collectionId name of the collection to be opened.
  */
//parentingColl = term:termId? termParent:termId...
// TODO: Rename as Collection?
//TODO: Needs full write-through, and table-splitting
class Collection(
  collectionId: Long,
  initRootTerm: WeighedTitle,
  private val rootColl: FieldQueryable,
  private val termColl: FieldQueryable,
  private val parentColl: RefMap,
  private val objectColl: RefMap
)
{



  
  //----------
  //- Cache --
  //----------

  // NB: Terms and parents should always be in sync.
  // To not be is a critical error.
  var rootTermCache: WeighedTitle = initRootTerm
  // Put the root term on. Always do this (it's in a seperate collection, and can not be deleted)
  val termCache = collection.mutable.Map.empty[Long, WeighedTitle]
  termCache += (0L -> initRootTerm)
  val parentCache = collection.mutable.Map.empty[Long, ArrayBuffer[Long]]
  val childCache = collection.mutable.Map.empty[Long, ArrayBuffer[Long]]

  def rootTerm
      : WeighedTitle =
  {
    if (rootTermCache == WeighedTitle.empty) {
      rootTermCache = rootColl.get[WeighedTitle](collectionId).get
    }
    rootTermCache
  }

  def collectionName: String = rootTerm.title

  def term(id: Long)
      : WeighedTitle =
  {

    if (!termCache.contains(id))
    {
      val t: Option[WeighedTitle] = termColl.get(id)
      elErrorIf(t == None, s"Term requested from storage which can not be found id: $id")
      termCache += (id -> t.get)
    }

    termCache(id)
  }

  // NB: Never put the root term here (never parent tid = 0)
  def parents(tid: Long)
      : ArrayBuffer[Long] =
  {
    if (!parentCache.contains(tid))
    {
      val parents = parentColl(tid)
      parentCache += (tid -> parents)
    }
    parentCache(tid)
  }

  def children(tid: Long)
      : ArrayBuffer[Long] =
  {
    if (!childCache.contains(tid)) {
      val children = parentColl.keysByVal(tid)

      val sortedChildren = children.sortWith( (a, b) => {term(a).weight < term(b).weight })

      childCache += (tid -> sortedChildren)
    }
    childCache(tid)
  }

  /** Clear the cache.
    *
    * Brute force, but guaranteed to work and return updated results
    * after Collection modifications e.g. `removeTerm`.
    */
  def clearCache() {
    rootTermCache = WeighedTitle.empty
    termCache.clear()
    termCache += (0L -> rootTerm)
    parentCache.clear()
    childCache.clear()
  }

  /** Clear the cache of parents and children.
    *
    * Brute force, but guaranteed to work and return updated results
    * after Collection modifications e.g. direct to table modifications,
    * or `mergeParents`.
    */
  def clearParentingCache() {
    parentCache.clear()
    childCache.clear()
  }


  //----------
  //- Terms --
  //----------

  /** Reads a term using a giver method.
    */
/*
  def termRead(id: Long,  f: (Giver) => Option[Unit])
      : Boolean =
  {
    val ret =  termColl(id)(f)
    (ret != None)
  }
*/
  def termRead(id: Long, t: Taker)
      : Boolean =
  {
     termColl(id, t)
  }

  /** Update term data.
    * 
    * Update a Term. Allows nullId updating. The Term id must be
    * correct. Terms are reparented with the given parents.
    *
    * This method is intended for GUIs with no explicit parenting
    * stage, which replace term data and parenting as one action.
    *
    * @param newParents if an empty List is expanded to Array(0)
    *  meaning 'top level term'
    */
  //
  def updateTerm(t: WeighedTitle, newParents: TraversableOnce[Long])
      : Boolean =
  {
    val tid = termColl.update(t.id, t)

    if(tid != NullID) {
      val ret = mergeParents(t.id, Traversable.empty[Long])
      // Works or not, doesn't matter
      termCache.remove(t.id)
      ret
    }
    else false

  }

  def updateRootTerm(id: Long, t: WeighedTitle)
      : Boolean =
  {
    val tid = rootColl.update(id, t)
    rootTermCache = WeighedTitle.empty
    (tid != NullID)
  }

  /** Update term data.
    * 
    * Update a Term. The Term id must be
    * correct. Parenting and reference connections remain unaltered.
    *
    */
  def updateTerm(id: Long, g: Giver)
      : Long =
  {
    log(s"Update Term")
    val ok = termColl.~(id, g)
    if(ok) {
      // Works or not, doesn't matter
// oops, wheres the id?
      //termCache.remove(tid)
id
}
else 0L
  }

  def updateRootTerm(id: Long, g: Giver)
      : Boolean =
  {
    log(s"Update Root Term")
    val ok = rootColl.~(id, g)
    if(ok) {
      // Works or not, doesn't matter
      rootTermCache = WeighedTitle.empty
true
}
     else false
  }

  /** Appends a new term to the collection.
    * 
    * Also inserts the term in the parent hierachy, at root. The id in
    * the object is overridden.
    *
    * @param term the term to be appended.
    * @return the insertion id of the term.
    */
  def appendTerm(g: Giver)
      : Long =
  {
    val tid: Long = termColl+(g)

    if (tid != NullID) {
      mergeParents(tid, Traversable.empty[Long])
      log("New Term id: ${tid}")
    }
    tid
  }

  private def singleParentChildren(tid: Long)
      : ArrayBuffer[Long] =
  {
    children(tid).filter { childId =>
      (parents(childId).length == 1)
    }
  }


  private def deleteTermRecursive(tid: Long)
      : Boolean =
  {

    // Remove any Term children to which this is a single parent.
    val orphans = singleParentChildren(tid)
    log("orphans: " + orphans)

    // Delete parent links
    parentColl-(tid)
    // Delete object links
    objectColl-(tid)
    // Delete the Term
    termColl-(tid)
    termCache.remove(tid)
    modelCount.remove(tid)

    // Recurse, freeing dependant (orphan) children
    orphans.foreach(deleteTermRecursive(_))
    // TODO: If the above recurses, this return is unfortunate.
    true
  }


  def deleteTerm(tid: Long)
      : Boolean =
  {
    elErrorIf(tid == 0, "Cannot delete root term collectionId: $collectionId")
    val ret = deleteTermRecursive(tid)
    // TOCONSIDER: Could be more fine-graned, this is destruction?
    clearParentingCache()
    ret
  }

  /** Returns a representation of all terms as displayable data.
    */
// Give up on this - use external appends?
  def termToDisplay()
      : Seq[(Long, String, Option[String])] =
  {
    // Don't load cache for reasons of a display
    // go to source.
    //termColl.select2Strings(1, Some(2))
val b = Seq.newBuilder[(Long, String, Option[String])]

    def func (g: Giver) {
b += Tuple3(g.long, g.string, Some(g.string))
}

    termColl.foreach(Seq(0, 1, 2), func _)
b.result
  }

  /** Returns a representation of the root term as displayable data.
    */
  def rootTermToDisplay()
      : (Long, String, String) =
  {
    new Tuple3(rootTerm.id, rootTerm.title, rootTerm.description)
  }

  /** Counts terms in the taxonomy.
    * 
    * The return includes a count for the root term.
    */
  def countTerms(): Long = {
    termColl.size() + 1
  }



  //-----------------
  //- Term chains --
  //-----------------

  // Currrently unused, an efficient way to derive Drupal's
  // 'active link' code (test and cache against the tid)
  private def ancestorChainsRecursive(chainRoot: List[Long])
      : List[List[Long]] =
  {
    val p = parents(chainRoot.head).toList
    if (p != Nil) {
      val currentChains = p.map { parent => parent :: chainRoot}
      currentChains.flatMap { chain => ancestorChainsRecursive(chain)}
    }
    else List[List[Long]](chainRoot)
    //else List[List[Long]](chainRoot.reverse)
  }

  /** Get ancestors of a tid.
    *
    * Each list runs from wherever parentage stopped, to the original
    * supplied id.  An id with no parents returns the id, e.g. id = 5,
    * returns List(List(5)
    * 
    * @return A flat tree list of lists of term ids
    * (i.e. each branching parent has a new chain)
    */
  def ancestorsTerm(tid: Long)
      : List[List[Long]] = { ancestorChainsRecursive(List[Long](tid)) }


  private def descendantChainsRecursive(chainRoot: List[Long])
      : List[List[Long]] =
  {
    val p = children(chainRoot.head).toList
    if (p != Nil) {
      val currentChains = p.map { parent => parent :: chainRoot}
      currentChains.flatMap { chain => descendantChainsRecursive(chain)}
    }
    else List[List[Long]](chainRoot)
  }

  /** Get the decendants of a tid.
    *
    * The lists run leaf to root. Each list runs to depth.
    * 
    * @return A flat tree list of lists of term ids
    * (i.e. each branching child has a new chain). 
    */
  def descendantChains(tid: Long)
      : List[List[Long]] = { descendantChainsRecursive(List[Long](tid)) }



  //-------------------
  //- Term parentage --
  //-------------------

  /** Selects a seq of parents and their titles.
    *
    */
  //parentNames
  def parentsToDisplay(tid: Long)
      : Seq[(Long, String, String)] =
  {
    // Terms and parents hould always map...
    parents(tid).map{ tid =>
      val t = term(tid)
      (tid,  t.title, t.description)
    }
  }




  /** Merges parents to a tid.
    *
    * The tid will be given the parents stated. If no parents are
    * given, the tid is parented to `nullID`.
    *
    * The method is protected against parenting a `nullID`.
    *
    * @param tid the id of the term to gain parents.
    * @param parents must be a valid set of tids.
    * @return true if no errors, else false
    */
  def mergeParents(tid: Long, parents: Traversable[Long])
      : Boolean =
  {
    // Do not parent NullID terms
    // ....or cause an endless loop
    // ....or cause an endless loop...
    elErrorIf(tid == 0, "Cannot parent root term collectionId: $collectionId")

    // Explicitly parent with 0 if no parents
    val requestedParents =
      if (parents.size == 0) IndexedSeq[Long](0)
      else parents

    // Delete existing parent links
    parentColl-(tid)

    // Add new parents
    // It is possible that the tids are not valid, but
    // that is the reponsibility of calling code.
    val ret = parentColl+(tid, requestedParents)

    // Kill parenting cache
    clearParentingCache()
    ret
  }


  private def foreachChildRecursive(
    tid: Long,
    f: (WeighedTitle, Int) => Unit,
    depth: Int
  )
  {
    children(tid).foreach{child =>
      f(term(child), depth)
      foreachChildRecursive(child, f, depth + 1)
    }
  }

  /** Applies a function f to all elements of this Collection tree.
    *
    * Terms not in the tree, ie. not parented, will not have the
    * function applied.
    *
    * Terms are visited depth-first. The term for the given id is
    * included in the foreach.  Mainly intended for visual displays.
    *
    * @param tid the term to start building on. Must be valid.
    * @param f the function to be applied. It supploies the Term
    *  itself, and the depth of the term in the hierarchy.
    */
  // Why, if we can get titles?
// For now, until treeview has a giver?
   def foreachChild(tid: Long, f: (WeighedTitle, Int) => Unit) {
   // catch term 0 (root)
   if (tid == 0) f(rootTerm, 0)
   else f(term(tid), 0)
   foreachChildRecursive(tid, f, 1)
   }




  //----------------------
  //- Object References --
  //----------------------

  // Mapped term -> object

  /** Get the ids of all terms associated with an object id.
    */
  // A key function for TermModelChild
  def readTermsByRef(id: Long)
      : ArrayBuffer[Long] =
  {
    objectColl.keysByVal(id)
  }

  /** Selects terms and their titles attached to a reference.
    *
    */
  def refTermNames(oid: Long)
      : Seq[(Long, String)] =
  {
    // Terms and parents should always map...
    readTermsByRef(oid).map{ tid =>
      (tid,  term(tid).title)
    }
  }

  /** Selects references to objects by term.
    *
    * @param the term on which to select references.
    * @return a list of ids of objects.
    */
  // get all the models associated with a term
  def readRefs(tid: Long)
      : ArrayBuffer[Long] =
  {
    objectColl(tid)
  }


  // Unecessary, see merge
  /*
   def appendRefs(parents: TraversableOnce[Long], oid: Long)
   : Boolean =
   {
   if (parents.size != 0) {
   parents.map { parentId =>
   // Protection against non-existent parents
   val t = term(parentId)
   if (t != None) {
   objectColl+(parentId, oid)
   }
   }

   true
   }
   else false
   }
   */

  /** Removes references to an object.
    *
    * @param the id of the object to remove. 
    */
  def removeRefs(id: Long)
      : Boolean =
  {
    val ret = objectColl.removeByVal(id)
    // Too difficult to find individually
    if(ret) modelCount.clear()
    ret
  }

  /** Merges term to object references.
    *
    * The object id will be parented by the terms stated. If no terms are
    * given, the object id is removed from the map.
    *
    * @param oid the object id to gain term parents.
    * @param tids must be a valid set of term ids.
    * @return true if no errors (even if nothing changed or the object id
    * is removed), else false
    */
  def mergeRefs(oid: Long, tids: Seq[Long])
      : Boolean =
  {
    // remove the existing term parenting
    objectColl.removeByVal(oid)
    // log(s"mergeRefs objectId:$oid tids:$tids")
    //TOCONSIDER: We dont have bulk append by value?
    var ret = true
    tids.map{ parentId =>
      log(s"  mergeRef $parentId ")
      objectColl+(parentId, oid)
      modelCount.remove(parentId)
    }

    ret
  }

  //cache of termid -> object count
  private val modelCount = collection.mutable.Map.empty[Long, Long]

  /** Count references to objects.
    *
    * @param the term on which to count references.
    */
  def countRefs (tid: Long)
      : Long =
  {
    if (!modelCount.contains(tid)) {
      modelCount += (tid -> objectColl.size(tid))
    }
    modelCount(tid)
  }

  // Tail recursive? One rainy morning...
  private def countRefsRecursive (tid: Long)
      : Long =
  {
    children(tid).map { cid =>
      countRefsRecursive(cid)
    }.sum
  }

  /** Count references to objects below a term.
    *
    * @param the term on which to count references.
    */
  def countRefsAll (tid: Long)
      : Long =
  {
    countRefs(tid) + countRefsRecursive(tid)
  }



  //------------
  //- General --
  //------------

  def clean() : Boolean = {
    log(s"removing Collection $collectionId")
    var res = true
    res &= rootColl-(collectionId)
    //res &= termColl.clean()
    //res &= parentColl.clean()
    //res  &= objectColl.clean()
    res
  }

  override def toString()
      : String =
  {
    val b = new StringBuilder()
    b ++= "Collection(collectionId:"
    b append collectionId
    b ++= ", collectionName: "
    b ++= collectionName
    b ++= ", term count:"
    b append termColl.size
    b += ')'
    b.result
  }

}//Collection



object Collection {

  def testCollection: Collection ={
    import freight.interface.db.sqliteJDBC.SyncConnection

    val db = SyncConnection.testDB
    Collection(db, 1)
  }

  /** Repair tables in the Collection.
    *
    * Restores tables to a complete set. Sometimes attempts to rebuild
    * tables e.g. A list of terms can be restored from parenting data,
    * but the new terms will loose information data (be empty).
    *
    * Logs what it has done and how well it suceeds.
    */
  //TODO: Likely can rescue more...
  def tableRepair (
    collectionId: Long,
    rootColl: FieldQueryable,
    termColl: FieldQueryable,
    parentColl: RefMap,
    objectColl: RefMap
  )
  {


    // If a table is lost...
    warning(
      s"Collections missing in the Collection $collectionId. Notifications and repairs to follow..",
      "Collection"
    )
    if (rootColl.isNew) {
      warning(s"The registering root table in $collectionId is missing. The table is restored.", "Collection")
    }

    if(!termColl.isNew ) {
      warning(s"The references to objects in this Collection $collectionId were not found. The table was restored. Object references have been lost.", "Collection")
      if(objectColl.isNew && !parentColl.isNew) {
        warning(s"The references to objects in this Collection $collectionId were not found. The table was restored. Object references have been lost.", "Collection")
      }

      if(objectColl.isNew && parentColl.isNew) {
        warning(s"The references to objects and heirarchy in this Collection $collectionId were not found. The tables were restored. Object references and heirarchy have been lost.", "Collection")
      }

      if(!objectColl.isNew && parentColl.isNew) {
        warning(s"The references to heirarchy in this Collection $collectionId were not found. The table was restored. Heirarchy has been lost.", "Collection")
      }
    }
    else {

      // Lost the terms table
      // Run over the parent table, inserting empty new terms
      if(!parentColl.isNew) {
        
        
        // Force insert at given id, with blank term
        // then reparent with exisiting parents
        // TODO defend against duplicates
        parentColl.foreach{ case(tid, parents) =>
// insert forces id this anyhow?
          termColl.insert(tid, WeighedTitle.newId(tid, WeighedTitle.empty))
        }
        // Umm, the heirarchy will be wrong here.
        warning("The terms of this Collection $table were not found. The table was restored from the parents. The new Terms have no content.",
          "Collection"
        )
      }
      /*
       // TODO: can be done, from object children...
       if(!objectColl.isNew) {
       objectColl.foreach{ case(k, v) =>
       t.mergeTerm(WeighedTitle.emptyNewId(k), Traversable.empty[Long])
       }
       warning("The terms of this Collection $table were not found. The table was restored. Terms in heirarchies have been replaced, but have no content.", "Collection")
       }
       */
    }
  }


  def apply(
    conn: Connection,
    collectionId: Long
  )
      : Collection =
  {
    val rootColl = conn.fieldQueryCollection(
"taxonomy",
 WeighedTitle
)
    val termColl = conn.fieldQueryCollection(
"taxonomy" + collectionId.toString + "_terms",
 WeighedTitle
)
    val parentColl = conn.refMap(
"taxonomy" + collectionId.toString + "_term_parents"
)
    val objectColl = conn.refMap(
"taxonomy" + collectionId.toString + "_term_refs"
)


    val isNew = (
      rootColl.isNew
        && termColl.isNew
        && parentColl.isNew
        && objectColl.isNew
    )


    // Ensure tables
    if (!isNew) {
      if (
        rootColl.isNew
          || termColl.isNew
          || parentColl.isNew
          || objectColl.isNew
      )
      {
        tableRepair(
          collectionId,
          rootColl,
          termColl,
          parentColl,
          objectColl
        )
      }
    }

    // Ensure root term
    val maybeRootTerm: Option[WeighedTitle] = rootColl.get(collectionId)
    val rootTerm: WeighedTitle =
      if (maybeRootTerm == None || isNew) {
        // lost root term
        warning("This Collection has lost the registering root term. The term is replaced with default content.", "Collection")
        val newRoot = WeighedTitle(collectionId, "root term", "new root term in a Collection", 0)
        rootColl.insert(
collectionId,
          newRoot
        )
        newRoot
      }
      else maybeRootTerm.get

    log(s"opening Collection $collectionId")
    //taxonomyColl
    val t = new Collection(
      collectionId,
      rootTerm,
      rootColl,
      termColl,
      parentColl,
      objectColl
    )
    t
  }

}//Collection
