package freight
package interface
package gui
package jswing

import swing._
import swing.event._



import java.nio.channels.{
  ReadableByteChannel
}

import jswing.generic.{
  StatusLabel,
  TabbedBox,
  TopAndBottomToolbarPanel,
  SimpleStatusbar,
CountDisplay
}

import jswing.event._




/** GUIs to edit elements of a collection.
  *
  * Offers an adjustable id field.
  *
  * See also [[CollectionTitleListGUI]]
  */
object SingleCollectionTitleListGUI
{

  /*
   to test see sqliteJDBC.TitleListGUI
   */

  /** Creates a GUI to edit elements of a collection.
    *
    * Full input parameters offer control over editing
    * multiple/complex collections.
    */
// And. collections and calls aside, this is now 
// the same as interface.complex.imageMap.GUI...
  def apply(
    windowTitle: String,
    coll: TakableFieldQueryable,
    meta: CompanionMeta,
    titleFieldIdx: Int,
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

val ctl = new SingleFieldQueryableController(
      coll
)
        val notifier = new JSwingNotifier



        //-----------------------------------------------------------
        // Actions
        //-----------------------------------------------------------

        def append() {

          val idO: Option[Long] = ctl.+(giverView)

          notifier.runIfAppendOk(idO) {
            //NB: This will not reload, only update the field
            giverView.setSelectedId(idO)
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
              giverView
            )

            deleteButton.enabled = ok
            updateButton.enabled = ok
            notifier.read(ok)
          }
        }


        def update() {
          val idO = listView.selectedId
          notifier.runIfValidId(idO) {
            val ok = ctl.~(idO.get, giverView)
          if (ok) {
            listView.updateElement(idO.get)
          }
            notifier.update(idO)
          }
        }

        def delete() {
          val idO = listView.selectedId
          notifier.runIfValidId(idO) {
            val ok = coll.-(idO.get)
            if (ok) {
            listView.deleteElement(idO.get)
              countView.dec()
              // Ask the list component.
              // If it selected something, replace the view
              // else clear
              if (listView.selectedId == None) {
                giverView.clear()
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
          coll,
          titleFieldIdx
        )
        // Make up the panel
        val listPanel = new TopAndBottomToolbarPanel(
          advice = "select to edit",
          listView
        )
// Initialize the listView
listView.readList()


        val deleteButton = listPanel.bottombar.glue.button("Delete")


        val giverView = ObjectReader(
          showIdField = false,
          notifyOfStatus = true,
          meta,
          collection = coll
        )
        // Make up the panel
        val giverPanel = new TopAndBottomToolbarPanel(
          "",
          giverView
        )
        // One panel GUI, so add a border
        giverPanel.border = panelBorder

        // Wire the id box for ENTER - a jswing speciality
        //editView.idAction(read)

        val updateButton = giverPanel.topbar.button("Update")
        val createButton = giverPanel.topbar.space.button("As New")


	// To start, with no selection...
        updateButton.enabled = false
        deleteButton.enabled = false


        val statusBar = new SimpleStatusbar()
val countView = new CountDisplay()
 countView.appendTo(statusBar)
// Initialize the countView
countView.set(ctl.size)

	// pack all in a tabbed box
	val tabbedBox = new TabbedBox
        tabbedBox.centredPageWithBorder("List", listPanel)
        tabbedBox.centredPageWithBorder("Edit", giverPanel)
        tabbedBox.contents += statusBar





        //-----------------------------------------------------------
        // Main build
        //-----------------------------------------------------------

        contents = tabbedBox

        listenTo(
          listView,
          giverView,
          notifier,
          createButton,
          deleteButton,
          updateButton
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
  

}//SingleCollectionTitleListGUI

