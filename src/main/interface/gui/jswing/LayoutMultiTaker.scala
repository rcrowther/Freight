package freight
package interface
package gui
package jswing


import collection.mutable.ArrayBuffer

import swing._
import java.awt.Font

import gui.generic._



/** Builds a swing gui from a description.
  */
final class LayoutMultiTaker(
  showIdField : Boolean
)
    extends DescriptiveTaker
{
  private val b = ArrayBuffer.empty[Component]
  private val m = ArrayBuffer.empty[FieldModifiableComponent]

  private var firstLong = true

  private val debugBorder = javax.swing.BorderFactory.createLineBorder(java.awt.Color.red)
  //private val labelBorder = Swing.EmptyBorder(8, 0, 2, 0)

  private val limitXGlueYFixed = new Dimension(Int.MaxValue, 20)



  private def label(text: String)
  {
    val txt = Character.toUpperCase(text.charAt(0)) + text.substring(1)
    val l = new Label(txt)
    l.font = stockFont
    l.border = labelBorder
    l.xLayoutAlignment = 0.0
    b += l
  }

  def boolean(fieldName: String, description: String, typ: freight.TypeMark) {
    // RadioButtons don't emit the SelectionChanged event. They do however emit ButtonClicked.
    val f = new CheckBox(fieldName)
    f.maximumSize = limitXGlueYFixed
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent(f)
    b += f
  }

  def short(fieldName: String, description: String, typ: freight.TypeMark) {
    label(fieldName)
    val f = new TextField(5)
    f.maximumSize = limitYFixed(5)
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent.numericField(f, "0")
    b += f
  }

  def int(fieldName: String, description: String, typ: freight.TypeMark) {
    label(fieldName)
    val f = new TextField(10)
    f.maximumSize = limitYFixed(10)
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent.numericField(f, "0")
    b += f
  }

  def long(fieldName: String, description: String, typ: freight.TypeMark) {
    if(firstLong) {
      val f = new TextField(20)
      f.maximumSize = limitYFixed(20)
      f.xLayoutAlignment = 0.0
      // Only put the textfield and label on the showable components
      // if requested
      if (showIdField) {
        label(fieldName)
        b += f
      }
      // The textfield is on the model always
      m += FieldModifiableComponent.numericField(f, "0")

      firstLong = false
    }
    else {
      label(fieldName)
      val f = new TextField(20)
      f.maximumSize = limitYFixed(20)
      f.xLayoutAlignment = 0.0
      m += FieldModifiableComponent.numericField(f, "0")
      b += f
    }
  }

  def float(fieldName: String, description: String, typ: freight.TypeMark) {
    label(fieldName)
    val f = new TextField(10)
    f.maximumSize = limitYFixed(10)
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent.numericField(f, "0")
    b += f
  }

  def double(fieldName: String, description: String, typ: freight.TypeMark) {
    label(fieldName)
    val f = new TextField(20)
    f.maximumSize = limitYFixed(20)
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent.numericField(f, "0")
    b += f
  }

  def string(fieldName: String, description: String, typ: freight.TypeMark) {
    label(fieldName)

    //l.maximumSize = limitXGlueYFixed
    //f.preferredSize = limitXGlueYFixed
    //l.horizontalAlignment = Alignment.Left
    //f.setAlignmentX(Component.CENTER_ALIGNMENT)
    //l.xLayoutAlignment = 0.0
    val f = new TextField(20)
    f.maximumSize = limitXGlueYFixed
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent.numericField(f, "0")
    b += f
  }

  //TODO: Make just a label not *binary here*?
  // But not usable in model then...
  def binary(fieldName: String, description: String, typ: freight.TypeMark) {
    label(fieldName)
    val f = new TextField(20)
    f.maximumSize = limitXGlueYFixed
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent.numericField(f, "0")
    b += f
  }

  def text(fieldName: String, description: String, typ: freight.TypeMark) {
    label(fieldName)
    val f = new TextArea(5, 30){text = "write here"}
    // NB: Yes (and should be by default)
    f.wordWrap = true
    // *No height limit for text*
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent(f)
    b += f
  }

  def time(fieldName: String, description: String, typ: freight.TypeMark) {
    label(fieldName)
    val f = new TextField(20)
    f.maximumSize = limitYFixed(20)
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent.numericField(f, "0")
    b += f
  }

  def timestamp(fieldName: String, description: String, typ: freight.TypeMark) {
    label(fieldName)
    val f = new TextField(20)
    f.maximumSize = limitYFixed(20)
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent.numericField(f, "0")
    b += f
  }

  def locale(fieldName: String, description: String, typ: freight.TypeMark) {
    b += new Label(fieldName)
    //val f =new ComboBox(Seq("IT","EN","GB"))
    // TEMP
    //val f = new TextField(8)
    val f = new LocaleComboBox
    //f.maximumSize = limitYFixed(8)
    f.xLayoutAlignment = 0.0
    m += FieldModifiableComponent(f)
    b += f
  }


  def widgets() : ArrayBuffer[Component] = b.result
  def model() : ObjectModel = new ObjectModel(m.toArray)

}//LayoutMultiTaker



object LayoutMultiTaker {

 def label(text: String)
: Label =
  {
    val txt = Character.toUpperCase(text.charAt(0)) + text.substring(1)
    val l = new Label(txt)
    l.font = stockFont
    l.border = labelBorder
    l.xLayoutAlignment = 0.0
    l
  }

}//LayoutMultiTaker
