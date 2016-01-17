package freight
package interface
package html

import complex.taxonomy.Collection

/*

 import freight._
 import interface.complex.taxonomy.Collection

 val t = Collection.testCollection

 import freight.interface.db.sqliteJDBC._
 import freight.objects.Paper

 //import freight.canspeak.XML
 val db = SyncConnection.testDB
 val c = FieldQueryCollection.testFieldQueryCollection(db)

 // Read
 val b = new interface.html.TaxonomyBuilder(t, c.get)
 b.termChildren(0).result

 b.termObjects(1, "node")

 val b2 = EmptyPot.testEmptyPot()
 b2 += b
 b2.result
 */



/** Builds neutral (x)html from a taxonomy.
  *
  * Most data is surrounded with a `div` element, but times and dates
  * are surrounded with a `time` element.
  */
object TaxonomyBuilder {

  def testTaxonomyBuilder()
  {
    import freight._
    import interface.complex.taxonomy.Collection

    val t = Collection.testCollection

    import freight.interface.db.sqliteJDBC._
    val db = SyncConnection.testDB
    val c = FieldQueryCollection.testFieldQueryCollection(db)

    // Read
    // val b = new interface.html.TaxonomyBuilder(t, c.get)
    //interface.html.TaxonomyBuilder.tree(b, t,"term", false, c.get, "object")
  }

  /** Generates an anchor surrounded by a list item.
    *
    * Will not work properly for lists of nested items.
    */
  private def listItemAnchor(
    b: StringBuilder,
    id: Long,
    hrefRoot: String,
    txt: String
  )
  {
    b ++= "\n<li>\n<a href=\""
    b ++= hrefRoot
    b += '/'
    b append id
    b ++= "\">"
    b ++= txt
    b ++= "</a>"
    b ++= "\n</li>"
  }

  /** Generates an list item open and and a fully closed anchor.
    * 
    * The list item needs closing.
    */
  private def termListOpen(
    b: StringBuilder,
    id: Long,
    hrefRoot: String,
    txt: String
  )
  {
    b ++= "\n<li>\n<a href=\""
    b ++= hrefRoot
    b += '/'
    b append id
    b ++= "\">"
    b ++= txt
    b ++= "</a>"
  }


  def termHeadline(
    b: StringBuilder,
    t: Collection,
    tid: Long,
    level: Int
  )
      : StringBuilder =
  {
    val l = level.toString
    b ++= "<h"
    b ++= l
    b += '>'
//if(tid == 0) {
//}
//else 
    b ++= t.term(tid).title
    b ++= "</h"
    b ++= l
    b += '>'
    b
  }


  /** Generates an html anchor list of immediate child terms to a term.
    */
  def termChildren(
    b: StringBuilder,
    t: Collection,
    tid: Long
  )
      : StringBuilder =
  {
    val children = t.children(tid)
    if (!children.isEmpty) {
      b ++= "\n<ul id=\"anchorlist term"
      b append tid
      b ++= "\">"
      children.foreach { id =>
        listItemAnchor(b, tid, "term", t.term(id).title)
      }
      b ++= "\n</ul>"
    }
    b
  }

  /** Generates an html anchor list of objects attached to a term.
    */
  def termObjects(
    b: StringBuilder,
    t: Collection,
    objectDisplay: (Seq[Long]) => Seq[(Long, String, Option[String])],
    tid: Long,
    hrefRoot: String
  )
      : (String, StringBuilder) =
  {
    val refIds = t.readRefs(tid)
    if (!refIds.isEmpty) {
      b ++= "\n<ul id=\"anchorlist term"
      b append tid
      b ++= "\">"
      objectDisplay(refIds).foreach{ case (id, title, description) =>
        listItemAnchor(b, id, hrefRoot, title)
      }
      b ++= "\n</ul>"
    }
    (t.term(tid).title, b)
  }

  private def treeRecursive(
    b: StringBuilder,
    t: Collection,
    tid: Long,
    maxDepth: Int,
    currentDepth: Int,
    termHrefRoot: String,
    withObjects: Boolean,
    objectDisplay: (Seq[Long]) => Seq[(Long, String, Option[String])],
    objectHrefRoot: String
  )
  {
    val children = t.children(tid)
    if (!children.isEmpty && currentDepth < maxDepth) {
      b ++= "\n<ul id=\"anchorlist term\">"
      children.foreach { id =>
        termListOpen(b, id, termHrefRoot, t.term(id).title)
        if(withObjects) {
          termObjects(
            b,
            t,
            objectDisplay,
            id,
            objectHrefRoot
          )
        }
        treeRecursive(
          b,
          t,
          id,
          maxDepth,
          currentDepth + 1,
          termHrefRoot,
          withObjects,
          objectDisplay,
          objectHrefRoot
        )
        b ++= "\n</li>"
      }
      b ++= "\n</ul>"
    }
  }

  /** Generates an html anchor list of all child terms to a term.
    *
    * @param termHrefRoot the base url for term links.
    * @param withObjects if true, include oject links, else the
    *  tree is only terms.
    * @param objectHrefRoot the base url for object links.
    */
  //TODO: Add counts!
  def tree(
    b: StringBuilder,
    t: Collection,
    tid: Long,
    depth: Int,
    termHrefRoot: String,
    withObjects: Boolean,
    objectDisplay: (Seq[Long]) => Seq[(Long, String, Option[String])],
    objectHrefRoot: String
  )
      : StringBuilder =
  {
    treeRecursive(
      b,
      t,
      tid,
      depth,
      0,
      termHrefRoot,
      withObjects,
      objectDisplay,
      objectHrefRoot
    )
    b
  }

  /** Generates an html anchor list of all child terms to a term.
    *
    * Couple of defaults - start from the root, go to max depth.
    */
  def tree(
    b: StringBuilder,
    t: Collection,
    termHrefRoot: String,
    withObjects: Boolean,
    objectDisplay: (Seq[Long]) => Seq[(Long, String, Option[String])],
    objectHrefRoot: String
  )
      : StringBuilder =
  {
    tree(
      b,
      t,
      0,
      Int.MaxValue,
      termHrefRoot,
      withObjects,
      objectDisplay,
      objectHrefRoot
    )
    b
  }


  private def treeTermDivRecursive(
    b: StringBuilder,
    t: Collection,
    tid: Long,
    maxDepth: Int,
    currentDepth: Int,
    withObjects: Boolean,
    objectDisplay: (Seq[Long]) => Seq[(Long, String, Option[String])],
    objectHrefRoot: String
  )
  {
    val children = t.children(tid)
    if (!children.isEmpty && currentDepth < maxDepth) {
      b ++= "\n<ul id=\"anchorlist term\">"
      children.foreach { id =>
        b ++= "\n<li>\n<div\">"
        b ++= t.term(id).title
        b ++= "</div>"

        if(withObjects) {
          termObjects(
            b,
            t,
            objectDisplay,
            id,
            objectHrefRoot
          )
        }
        treeTermDivRecursive(
          b,
          t,
          id,
          maxDepth,
          currentDepth + 1,
          withObjects,
          objectDisplay,
          objectHrefRoot
        )
        b ++= "\n</li>"
      }
      b ++= "\n</ul>"
    }
  }

  /** Generates an html anchor list of all child objects.
    *
    * The term is listed as a DIV-enclosed display. This is useful
    * when the display is large enough to fit on a screen, and
    * clicking through to a term listing of a few items would be
    * waering for the user (which, in current web design, is often).
    *
    * @param termHrefRoot the base url for term links.
    * @param withObjects if true, include oject links, else the
    *  tree is only terms.
    * @param objectHrefRoot the base url for object links.
    */
  def treeTermDiv(
    b: StringBuilder,
    t: Collection,
    tid: Long,
    depth: Int,
    withObjects: Boolean,
    objectDisplay: (Seq[Long]) => Seq[(Long, String, Option[String])],
    objectHrefRoot: String
  )
      : StringBuilder =
  {
    treeTermDivRecursive(
      b,
      t,
      tid,
      depth,
      0,
      withObjects,
      objectDisplay,
      objectHrefRoot
    )
    b
  }

  /** Generates an html anchor list of all child objects.
    *
    * The term is listed as a DIV-enclosed display. This is useful
    * when the display is large enough to fit on a screen, and
    * clicking through to a term listing of a few items would be
    * waering for the user (which, in current web design, is often).
    *
    * Couple of defaults - start from the root, go to max depth.
    */
  def treeTermDiv(
    b: StringBuilder,
    t: Collection,
    withObjects: Boolean,
    objectDisplay: (Seq[Long]) => Seq[(Long, String, Option[String])],
    objectHrefRoot: String
  )
      : StringBuilder =
  {
    treeTermDiv(
      b,
      t,
      0,
      Int.MaxValue,
      withObjects,
      objectDisplay,
      objectHrefRoot
    )
    b
  }

}//TaxonomyBuilder
