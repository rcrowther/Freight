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
// TODO: Only difference to SingleImageCollectionGUI is the view and the
// collection?
object SingleRefMapGUI
{

  /*
   to test see sqliteJDBC.GUI
   */

  /*
   import freight._
   import freight.interface.gui.jswing.SingleRefMapGUI
   SingleRefMapGUI.test
   */


  def test() {
    val connector: WireConnector = new WireConnectorLocal()

        val cr = CollectionRoute(
          connectionType = ConnectionType.sqlitejdbc,
          collectionType = CollectionType.refmap,
          collectionName = "f_test_refmap"
        )

    apply(
      windowTitle = "RefmapTest",
      connector,
      cr,
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
    collectionRoute: CollectionRoute,
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



        val ctl = connector.refmap(collectionRoute)



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
          // NB: Since we do not knfow if update or insert
          // we must reread the size.
          ctl.sizeRequest
        }

        def deleteResponse(id: Long)
        {
          refmapView.clear()
          publish(StatusMessage.info("key deleted"))
          countView.dec()
        }

        def opFailResponse(method: String)
        {
          refmapView.clear()
          if(method == "read")  deleteButton.enabled = false
          notifier.opFailNotify(method)
        }


        //-----------------------------------------------------------
        // Components
        //-----------------------------------------------------------


        private val idView = new IdView()
        // Wire the id box for ENTER - a jswing speciality
        idView.idAction(read)


        val refmapView = new IdListSelector(
          valueAdvice = "current values:"
        )
        ctl.registerIdDataResponse(idDataResponse)
        ctl.registerUpsertResponse(upsertResponse)
        ctl.registerDeleteResponse(deleteResponse)
        ctl.registerOpFailResponse(opFailResponse)

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
        val upsertButton = dataPanel.topbar.space.button("Update|Insert")
        val deleteButton = dataPanel.bottombar.glue.button("Delete")

	// To start, with no selection...
        deleteButton.enabled = false


        val statusBar = new SimpleStatusbar()
        val countView = new CountDisplay()
        ctl.registerSizeResponse(countView.set)
        countView.appendTo(statusBar)
        // Initialize the countView
        ctl.sizeRequest


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
  




}//SingleRefMapGUI

