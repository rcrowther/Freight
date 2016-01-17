package freight
package interface
package html
package servlet

//import file.base.SyncCollection
import db.sqliteJDBC._
import freight.objects.Paper
import core.objects.WeighedTitleString
import java.io.{OutputStream, PrintWriter}

//localhost:8080
//http://localhost:8080/freight/paper/display/4
class FreightDemo
    extends Connection
{
  //override val developmentMode = false

  //val coll = SyncCollection.testSyncCollection
  val coll = SyncConnection.testCollection
  val db = SyncConnection.testDB

  // Paper
  val paperColl = FieldQueryCollection.testFieldQueryCollection(db)

// Images
 //import freight.interface.file.binaryBase._
  val (imageConn, imageColl) = freight.interface.file.filebase.SyncBinaryCollection.testColl()

  // Menu
/*
  val menuColl = complex.array.SortedCacheCollection(
    db,
    1,
    WeighedTitleString,
    WeighedTitleString.take
  )
*/

  // Taxonomy
  val txmy = complex.taxonomy.Collection.testCollection
  val paperTable = complex.sortedTable.Collection.testCollection

  //TODO: A clash. See Connection
  // NB: the link href can be absolute or relative. An absolute href will
  // ground in the container server e.g. http://localhost:8080/<href>
  // which may be useful for some purposes, but is global and not flexible.
  // A relative URL (no slash or "./") will be handled by the `notFound`
  // through staticResource
  //
  // Trying to use web.xml is possible, but has problems - see the note in the demo
  // web.xml file.
  //
  // Likely the best oveerall solution is to use an absolute references (which will go to server root) then initialize with a configurable appname. That should cover reconfiguration.
  //
  val mkPage2 = pageBuilder.PreparedHTML5(
    base = "",
    links = """<link rel="stylesheet" href="/freight/src/main/css/reset.css"></link>
<link rel="stylesheet" href="/freight/css/paper.css"></link>
""",

    scripts = ""
  )
  // type=\"text/css\"
  //  <link href="/assets/css/bootstrap.css" rel="stylesheet" />



  //---------------
  //-- Utility --
  //---------------

  get("testpath/[:long:]") {
    "query params: " + wrequest.urlExtractor.query.toString +
    "<br>url: " + wrequest.url +
    "<br>urlExtractor.path: " + wrequest.urlExtractor.path +
    "<br>urlExtractor.servletPathString: " + wrequest.urlExtractor.servletPathString +
    "<br>urlExtractor.path: " + wrequest.urlExtractor.path
  }


  //---------------
  //-- Menu --
  //---------------

  def menu(b: StringBuilder)
  {
    b ++= "<nav>\n<ul>\n"


    val ob = new interface.html.ObjectAnchorBuilder(
      b,
      objectName = "title-elem",
      anchorTextIdx = 0,
      hrefBase = "..",
      hrefPathIdx = 2
    )

    // TODO: Can do much better here?
/*
    menuColl.foreach(title => {
      ob.newObject()
      //ob.long("id", title.id)
      ob.string("title", title.title)
      ob.string("description", title.description)
      ob.string("value", title.value)
    })
*/
    ob.result()

    b ++= "</ul>\n</nav>\n"

  }

  //---------------
  //-- Objects --
  //---------------

  get("paper/display", Paper, coll)

  get("paper/list") {
    val b = new StringBuilder()

    menu(b)

    b ++= "<h3>Index</h3>\n<div class=\"poems\">\n"

    val bl = new interface.html.ObjectAnchoredFieldBuilder(
      b,
      objectName = "poem",
      anchoredFieldIdx = 2,
      hrefBase = "../paper/display"
    )

    // id language title description...
    paperColl.foreach(fieldIdxs = Seq(0, 1, 2, 3), Seq(2), bl)

    bl.result()

    b ++= "</div>\n"

    println(b)
    mkPage2(title="Index", language="EN", description="find a poem", b.result())
  }



  //---------------
  //-- taxonomy --
  //---------------


  private def titledata(idxs: Seq[Long])
      : Seq[(Long, String, Option[String])]=
  {
    // TODO: A lot better than this...
    //paperColl.select2Strings(2, Some(3), idxs)
    val b = Seq.newBuilder[(Long, String, Option[String])]
    def func(g: Giver) {
      b += Tuple3(g.long, g.string, Some(g.string))
    }

    paperColl(Seq(0, 2, 3), idxs, func _)
    b.result
  }


  get("term/[:long:]") {
    val tid = wrequest.urlExtractor.path(2).toLong
    val b = new StringBuilder()

    TaxonomyBuilder.termHeadline(b, txmy, tid, level = 4)

    TaxonomyBuilder.termObjects(
      b,
      txmy,
      titledata,
      tid,
      "../paper/display"
    )
    mkPage2(title="Term", language="EN", description="An Index Term", b)
  }

  get("taxonomy") {
    val b = new StringBuilder()

    TaxonomyBuilder.termHeadline(b, txmy, 0, level = 3)

    TaxonomyBuilder.treeTermDiv(
      b,
      txmy,
      withObjects = true,
      titledata,
      "paper/display"
    )
    mkPage2(title="Index", language="EN", description="Map of the poems", b)
  }



  //---------------
  //-- images --
  //---------------

  get("image/[:long:]") {
    val id = wrequest.urlExtractor.path(2).toLong
    renderStream((s: OutputStream) => { imageColl.apply(id, s) }, JPEG)
  }

}//FreightDemo
