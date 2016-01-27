package freight
package interface
package gui
package jswing

import swing._

import gui.generic._
import core.util.StringUtil


/** Returns Swing components for an object model.
  *
  * The fields should establish sensible defaults for empty data.
  */
object FieldModifiableComponent{

  def string(m:TextField) : FieldModifiableComponent = {
    new GTextField(m)
  }

  def numericField(m:TextField, default: String)
      : FieldModifiableComponent =
  {
    new GNumericField(m, default)
  }

  def apply (m:TextArea)
      : FieldModifiableComponent =
  {
    new GTextArea(m)
  }


  def apply (m:CheckBox) : FieldModifiableComponent = {
    new GCheckBox(m)
  }


   def apply(m: LocaleComboBox)
 : FieldModifiableComponent =
{
   new GLocaleComboBox(m)
   }

}


/** A field accepting text.
  */
class GTextField(c:TextField)
    extends FieldModifiableComponent
{
  def get: String = {c.text}
  def set(v: String) = {c.text = v}
  def enable = {c.enabled = true}
  def disable = {c.enabled = false}
}


/** A textfield accepting numeric entries.
  *
  * @param default the string to be used if the field is empty e.g. "0" or "1".
  */
class GNumericField(c:TextField, val default:String)
    extends FieldModifiableComponent
{
  def get: String = {
    val ret = c.text
    if (StringUtil.isBlank(ret)) default
    else ret
  }
  def set(v: String) = {c.text = v}
  def enable = {c.enabled = true}
  def disable = {c.enabled = false}
}


class GTextArea(c: TextArea)
    extends FieldModifiableComponent
{
  def get: String = { c.text }
  def set(v: String) = {c.text = v}
  def enable = {c.enabled = true}
  def disable = {c.enabled = false}
}


class GCheckBox(c: CheckBox)
    extends FieldModifiableComponent
{
  def get: String = { if(c.enabled) "t" else "f" }
  def set(v: String) = { c.enabled = (v == "t") }
  def enable = {c.enabled = true}
  def disable = {c.enabled = false}
}

class GBooleanComboBox()
    extends FieldModifiableComponent
{
  val c = new ComboBox(Seq("true", "false"))
  def get: String = { if(c.selection.index == 0) "t" else "f" }
  def set(v: String) = {c.selection.index = if (v == "t") 0 else 1 }
  def enable = {c.enabled = true}
  def disable = {c.enabled = false}
}

// TOCONSIDER: Maybe need separate anguage/country values? Later...
// TOCONSIDER: Not ideal, a custom component, but 
// Scala's wrap of ComboBox is not fond of swapping the model (it's a val)
// and I don't want to loop the values on R.C.
class LocaleComboBox
extends ComboBox(LocaleComboBox.localeStrings)

object LocaleComboBox {
  import java.util.Locale

  //TODO: Should this building be static?
   val locales: Array[Locale] =
    Locale.getAvailableLocales()
  private val sB = Array.newBuilder[String]
  private val b = new StringBuilder()
  private val displayStrings = locales.foreach{ l =>
    b.clear()
    b ++= l.toString
    b += ' '
    b ++= l.getDisplayName()
    sB += b.result
  }
  val localeStrings: Array[String] = sB.result()
}

// TODO: Finish this - it won't work as stands
// TODO: Won't work!
class GLocaleComboBox(c: LocaleComboBox)
    extends FieldModifiableComponent
{
  //import scala.collection.JavaConverters._
  import java.util.Locale

  def get: String = { LocaleComboBox.locales(c.selection.index).toString }

  def set(v: String) = {
    val locale = new Locale(v)

    val foundLocale: Int = LocaleComboBox.locales.indexWhere( (l: Locale) => {locale.equals(l)} )

    // NB: if failed to find, set box halfway down
    // TODO: (or sould it go to system locale?)
    c.selection.index =
      if (foundLocale == -1)  LocaleComboBox.localeStrings.size >> 1
      else foundLocale
  }

  def enable = {c.enabled = true}
  def disable = {c.enabled = false}
}


/*
 //http://docs.oracle.com/javase/tutorial/uiswing/examples/components/ComboBoxDemo2Project/src/components/ComboBoxDemo2.java
 class GDateComboBox()
 extends FieldModifiableComponent
 {
 val datePatterns = Seq(
 "dd MMMMM yyyy",
 "dd.MM.yy",
 "MM/dd/yy",
 "yyyy.MM.dd G 'at' hh:mm:ss z",
 "EEE, MMM d, ''yy",
 "h:mm a",
 "H:mm:ss:SSS",
 "K:mm a,z",
 "yyyy.MMMMM.dd GGG hh:mm aaa"
 )
 val c = new ComboBox(datePatterns)
 c.editable = true
 def get: String = { if(c.selection.item == 0) "t" else "f" }
 def set(v: String) = {c.selection.index = if (v == "t") 0 else 1 } 
 def enable = {c.enabled = true}
 def disable = {c.enabled = false}
 }
 */
/*
 class GComboBox(c: ComboBox)
 extends FieldModifiableComponent
 {
 def get: String = { if(c.enabled) "t" else "f" }
 def set(v: String) = { c.enabled = (v == "t") } 
 def enable = {c.enabled = true}
 def disable = {c.enabled = false}
 }
 */
