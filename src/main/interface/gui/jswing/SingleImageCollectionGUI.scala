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
  MorphingImage,
  TopAndBottomToolbarPanel,
  SimpleStatusbar,
CountDisplay
}

import jswing.event._




/** GUI to edit elements of an image collection.
  *
  * Factory methods can build from a binary collection.
  *
* Works on any binary image collection, one collection only.
*
  * Offers an adjustable id field.
  */
// TODO: Only difference to SingleImageCollectionGUI is the view and the
// collection?
object SingleImageCollectionGUI
{

  /*
   to test see file.fileBase.ImageGUI
   */

  /** Creates a GUI for editing elements of a collection.
    *
    * Full input parameters offer control over editing
    * multiple/complex collections.
    */
  def apply(
    windowTitle: String,
    coll: BinaryTakable,
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


val ctl = new SingleBinaryCollectionController(
      coll
)
        val notifier = new JSwingNotifier




        //-----------------------------------------------------------
        // Actions
        //-----------------------------------------------------------

        def append() {

          val idO: Option[Long] = ctl.+(dataView)

          notifier.runIfAppendOk(idO) {
            //NB: This will not reload, only update the field
            dataView.setSelectedId(idO)
            countView.inc()
            notifier.append(idO)
          }
        }

        def read(idO: Option[Long]) {
          notifier.runIfValidId(idO) {
            var ok = true
            
            ok = ctl(
              idO.get,
              dataView
            )

            deleteButton.enabled = ok
            updateButton.enabled = ok
            notifier.read(ok)
          }
        }

        def read(): Unit = read(dataView.selectedId)

        def update() {
          val idO = dataView.selectedId
          notifier.runIfValidId(idO) {
            val ok = ctl.~(idO.get, dataView)
            notifier.update(idO)
          }
        }

        def delete() {
          val idO = dataView.selectedId
          notifier.runIfValidId(idO) {
            val ok = coll.-(idO.get)
            if (ok) {
dataView.clear()
              countView.dec()
          deleteButton.enabled = false
          updateButton.enabled = false
            }
            notifier.delete(ok)
          }
        }



        //-----------------------------------------------------------
        // Components
        //-----------------------------------------------------------



   val dataView = ImageReader(
          showIdField = true,
          notifyOfStatus = true,
          width = 194,
          height = 194,
          collection = coll
        )
        // Make up the panel
        val dataPanel = new TopAndBottomToolbarPanel(
          "",
          dataView
        )

        // One panel GUI, so add a border
        dataPanel.border = panelBorder

        // Wire the id box for ENTER - a jswing speciality
        dataView.idAction(read)

        val readButton = dataPanel.topbar.button("Read")
        val updateButton = dataPanel.topbar.button("Update")
        val createButton = dataPanel.topbar.space.button("As New")
        val deleteButton = dataPanel.bottombar.glue.button("Delete")

	// To start, with no selection...
        updateButton.enabled = false
        deleteButton.enabled = false


        val statusBar = new SimpleStatusbar()
val countView = new CountDisplay()
 countView.appendTo(statusBar)
// Initialize the countView
countView.set(ctl.size)

        // pack all in a VBox
        val vBox = new BoxPanel(Orientation.Vertical)
        vBox.contents += dataPanel
        vBox.contents += statusBar








        //-----------------------------------------------------------
        // Main build
        //-----------------------------------------------------------

        contents = vBox

        listenTo(
          dataView,
          notifier,
          readButton,
          createButton,
          deleteButton,
          updateButton
        )

        reactions += {

          case ButtonClicked(`createButton`) =>  {
            append()
          }

         case ButtonClicked(`readButton`) =>  {
            read(dataView.selectedId)
          }

          case ButtonClicked(`updateButton`) =>  {
            update()
          }

          case ButtonClicked(`deleteButton`) =>  {
            delete()
          }

          case StatusMessage(level, msg) => {
            statusBar.display(level, msg)
          }

        }//Reactions


      }//MainFrame
    }//SwingApp

    ui.main(Array[String]())

  }
  




}//SingleImageCollectionGUI

