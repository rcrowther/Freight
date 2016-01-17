package freight
package interface
package gui
package generic


import core.iface.TakerForString

/** Sets a model from freight objects.
  *
  */
//NB:  The model can be a taker, but we keep it clean.
class ObjectModelTaker(m: Array[FieldModifiableComponent])
    //extends StringTaker
extends TakerForString
{

  var i = 0

  def booleanStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def shortStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def intStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def longStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def floatStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def doubleStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def stringStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def textStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def binaryStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def timeStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def timestampStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }

  def localeStr(fieldName: String, v: String) {
    m(i).set(v)
    i += 1
  }


  def reset() { i = 0 }

}//ObjectModelTaker
