package freight
package interface
package gui
package jswing

import swing._
import swing.event._


import scala.collection.mutable.Buffer
import java.nio.channels.{
  ReadableByteChannel
}

import jswing.generic.{
  StatusLabel,
  MorphingImage,
  TopAndBottomToolbarPanel,
  SimpleStatusbar,
  CountDisplay
}

import jswing.event._

import freight.interface.db._
import freight.interface.gui.generic.{WireConnector, WireConnectorLocal}

/** GUIs to edit elements of a collection.
  *
  * Offers an adjustable id field.
  *
  * See also [[CollectionTitleListGUI]]
  */
// TODO: What this needs now is a titled id field. Later.
object SingleRefMapTitledGUI
{

  /*
   to test see sqliteJDBC.GUI
   */

  /*
   import freight._
   import freight.interface.gui.jswing.SingleRefMapTitledGUI
   SingleRefMapTitledGUI.test
   */


  def test() {
    val connector: WireConnector = new WireConnectorLocal()

    val cr = freight.interface.db.CollectionRoute(
      connectionType = freight.interface.db.ConnectionType.sqlitejdbc,
      collectionType = CollectionType.refmap,
      collectionName = "f_test_refmap"
    )

    val cr2 = freight.interface.db.CollectionRoute(
      connectionType = freight.interface.db.ConnectionType.sqlitejdbc,
      collectionType = CollectionType.fieldquery,
      collectionName = "poems"
    )

    apply(
      windowTitle = "RefmapTest",
      connector,
      cr,
      cr2,
      titleFieldIdxs = Seq[Int](0, 2, 3),
      closer = () => {log("temp close..."); true}
    )
  }

  /** Creates a GUI to edit elements of a collection.
    *
    * Full input parameters offer control over editing
    * multiple/complex collections.
    */
  def apply(
    windowTitle: String,
    connector: WireConnector,
    refmapCollectionRoute: CollectionRoute,
    titlesCollectionRoute: CollectionRoute,
    titleFieldIdxs: Seq[Int],
    closer: () => Boolean
  )
  {

    val ui = new Application(
      windowTitle,
      closer
    )
    {

      def top = new MainFrame {


        //-----------------------------------------------------------
        // Control
        //-----------------------------------------------------------

        val notifier = new JSwingNotifier2


        val ctl = connector.refmap(refmapCollectionRoute)

        val titleCtl = connector.fieldQuery(titlesCollectionRoute)


        //-----------------------------------------------------------
        // Actions
        //-----------------------------------------------------------

        def upsert() {
          val idO = idView.idAsLong
          notifier.runIfValidId(idO) {
            ctl.upsertRequest(idO.get, refmapView.selectedValues)
          }
        }

        def read() {
          val idO = idView.idAsLong
          notifier.runIfValidId(idO) {
            ctl.idDataRequest(idO.get)
          }
        }

        def delete() {
          val idO = idView.idAsLong
          notifier.runIfValidId(idO) {
            ctl.deleteRequest(idO.get)
          }
        }

        def idDataResponse(id: Long, values: Seq[Long])
        {
          refmapView.idDataResponse(id, values)
          deleteButton.enabled = true
          notifier.read()
        }


        def upsertResponse(id: Long)
        {
          publish(StatusMessage.info("new/updated key values!"))
          // NB: Since we do not know if update or insert
          // we must reread the size.
          ctl.distinctKeySizeRequest
        }

        def deleteResponse(id: Long)
        {
          // NB: Can not add an alternative selected id,
          // so clear selection values only.
          refmapView.clearSelection()
          publish(StatusMessage.info("key deleted"))
          countView.dec()
        }

        def opFailResponse(method: String)
        {
          //NB: Otherwise to 'read' do no ajustments to this view,
          // Its only as selector.
          if(method == "read")  {
            deleteButton.enabled = false
            refmapView.clearSelection()
          }
          notifier.opFailNotify(method)
        }

        private def foreachTitleResponse(data: Seq[(Long, String, String)])
        {
          val filteredData: Seq[(Long, String)] = data.map{param => (param._1, param._2)}
          refmapView.resetMaybeValues(filteredData)
        }



        //-----------------------------------------------------------
        // Components
        //-----------------------------------------------------------


        private val idView = new IdView()
        // Wire the id box for ENTER - a jswing speciality
        idView.idAction(read)


        val refmapView = new TitledListSelector(
          advice = "(select values)"
        )
        ctl.registerIdDataResponse(idDataResponse)
        ctl.registerUpsertResponse(upsertResponse)
        ctl.registerDeleteResponse(deleteResponse)
        ctl.registerOpFailResponse(opFailResponse)

        // Populate the selector with possible ids and titles
        titleCtl.registerForeachResponse(foreachTitleResponse)
        titleCtl.foreachRequest(titleFieldIdxs)


        val panelContents = new BoxPanel(Orientation.Vertical)
        panelContents.contents +=  stockItalicLabel("select/edit a key:")
        panelContents.contents += idView
        panelContents.contents += refmapView



        // Make up the panel
        val dataPanel = new TopAndBottomToolbarPanel(
          "",
          panelContents
        )

        val readButton = dataPanel.topbar.button("Read")
        //val updateButton = dataPanel.topbar.button("Update")
        val upsertButton = dataPanel.topbar.space.button("Update|Insert")
        val deleteButton = dataPanel.bottombar.glue.button("Delete")

	// To start, with no selection...
        //updateButton.enabled = false
        deleteButton.enabled = false


        val statusBar = new SimpleStatusbar()
        val countView = new CountDisplay()
        ctl.registerDistinctKeySizeResponse(countView.set)
        countView.appendTo(statusBar)
        // Initialize the countView
        ctl.distinctKeySizeRequest


        // pack all in a VBox
        val vBox = new BoxPanel(Orientation.Vertical)

        vBox.contents += dataPanel
        vBox.contents += statusBar

        // One panel GUI, so add a border
        vBox.border = panelBorder






        //-----------------------------------------------------------
        // Main build
        //-----------------------------------------------------------

        contents = vBox

        listenTo(
          refmapView,
          notifier,
          readButton,
          upsertButton,
          deleteButton
            //updateButton
        )

        reactions += {

          case ButtonClicked(`upsertButton`) =>  {
            upsert()
          }

          case ButtonClicked(`readButton`) =>  {
            read()
          }

          case ButtonClicked(`deleteButton`) =>  {
            delete()
          }

          case _: NeedsResizing2 => {
            resizeToContents()
          }

          case StatusMessage(level, msg) => {
            statusBar.display(level, msg)
          }

        }//Reactions

      }//MainFrame

    }//SwingApp

    ui.main(Array[String]())

  }
  




}//SingleRefMapTitledGUI

