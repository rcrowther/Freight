package freight
package interface
package complex
package imageMap

import swing._
import event._

import interface.gui.jswing.SingleCollectionTitleListGUI


import gui.jswing._
import gui.jswing.event._

import gui.jswing.generic.{
  StatusLabel,
  TabbedBox,
  TopAndBottomToolbarPanel,
  SimpleStatusbar,
  CountDisplay
}

/*
 import freight._
 import interface.complex.imageMap.GUI

 GUI.test()
 */


/** GUI for set marks.
  *
  */
object GUI{

  import core.objects.WeighedTitleString

  def test() {
    val cr = new Connector(
      interface.db.sqliteJDBC.SyncConnection.testDB,
      interface.file.filebase.SyncConnection.testDB
    )
    apply(cr, 1, WeighedTitleString, 1)
  }


  /** GUI to edit image map elements.
    */
  def apply(
    cr: Connector,
    collectionId: Long,
    meta: CompanionMeta,
    titleFieldIdx: Int
  )
  {


    val ui = new Application(
      s"ImageMap$collectionId",
      cr.close _
    )
    {


      def top = new MainFrame {

        //-----------------------------------------------------------
        // Control
        //-----------------------------------------------------------


        val ctl = cr.collection(
          collectionId,
          meta
        )

        val rawImageColl = cr.imageBaseCollection(
          collectionId
        )

        val rawDataColl = cr.dataBaseCollection(
          collectionId,
          meta
        )

        val notifier = new JSwingNotifier


        //-----------------------------------------------------------
        // Actions
        //-----------------------------------------------------------

        def append() {

          val idO: Option[Long] = ctl.+(dataView, imageView)

          notifier.runIfAppendOk(idO) {
            //NB: This will not reload, only update the field
            dataView.setSelectedId(idO)
            imageView.setSelectedId(idO)
            listView.appendElement(idO.get)
            countView.inc()
            notifier.append(idO)
          }
        }

        def read(idO: Option[Long]) {
          notifier.runIfValidId(idO) {
            var ok = true
            
            ok = ctl(
              idO.get,
              dataView,
              imageView
            )

            deleteButton.enabled = ok
            updateButton.enabled = ok
            notifier.read(ok)
          }
        }

        def update() {
          val idO = listView.selectedId
          notifier.runIfValidId(idO) {
            val ok = ctl.~(idO.get, dataView, imageView)
            if (ok) {
              listView.updateElement(idO.get)
            }
            notifier.update(idO)
          }
        }

        def delete() {
          val idO = listView.selectedId
          notifier.runIfValidId(idO) {
            val ok = ctl.-(idO.get)
            if (ok) {
              listView.deleteElement(idO.get)
              countView.dec()
              // Ask the list component.
              // If it selected something, replace the view
              // else clear
              if (listView.selectedId == None) {
                dataView.clear()
                imageView.clear()
              }
              else read(listView.selectedId)

            }
            notifier.delete(ok)
          }
        }



        //-----------------------------------------------------------
        // Components
        //-----------------------------------------------------------

        val listView = LineListReader(
          notifyOfStatus = false,
          rawDataColl,
          titleFieldIdx
        )
        // Make up the panel
        val listPanel = new TopAndBottomToolbarPanel(
          advice = "select to edit",
          listView
        )

        val deleteButton = listPanel.bottombar.glue.button("Delete")


        val dataView = ObjectReader(
          showIdField = false,
          notifyOfStatus = true,
          meta,
          collection = rawDataColl
        )
        // Make up the panel
        val dataPanel = new TopAndBottomToolbarPanel(
          "",
          dataView
        )
        // One panel GUI, so add a border
        dataPanel.border = panelBorder

        val updateButton = dataPanel.topbar.button("Update")
        val createButton = dataPanel.topbar.space.button("As New")

        val imageView = ImageReader(
          showIdField = false,
          notifyOfStatus = true,
          width = 194,
          height = 194,
          collection = rawImageColl
        )
        // Make up the panel
        val imagePanel = new TopAndBottomToolbarPanel(
          "",
          imageView
        )
        // One panel GUI, so add a border
        imagePanel.border = panelBorder


        val statusBar = new SimpleStatusbar()
        val countView = new CountDisplay()
        countView.appendTo(statusBar)
        // Initialize the countView
        countView.set(ctl.size)

	// pack all in a tabbed box
	val tabbedBox = new TabbedBox
        tabbedBox.centredPageWithBorder("List", listPanel)
        tabbedBox.centredPageWithBorder("Data", dataPanel)
        tabbedBox.centredPageWithBorder("Edit", imagePanel)
        tabbedBox.contents += statusBar




        // initialize the list
        listView.readList()


        //-----------------------------------------------------------
        // Main build
        //-----------------------------------------------------------

        contents = tabbedBox

        listenTo(
          listView,
          notifier,
          createButton,
          updateButton,
          deleteButton
        )

        reactions += {
          case ButtonClicked(`createButton`) =>  {
            append()
          }



          case ButtonClicked(`updateButton`) =>  {
            update()
          }

          case ButtonClicked(`deleteButton`) =>  {
            delete()
          }

          case SelectedIdChanged(idO) => {
            read(idO)
          }

          case NeedsResizing(duff) => {
            println(s"llvd resize!")
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
  

}//GUI

