package freight
package core
package iface



/** Templates methods for receiving Freight field information.
  *  
  * Implementations of this class are given descriptive information
  * about freight fields.
  *
  * It is used to build object-specific data for interfaces, such as 
  * generic SQL queries, visual column display labels, etc.
  */
trait DescriptiveTaker
{
  def boolean(fieldName: String, description: String, typ: TypeMark)
  def short(fieldName: String, description: String, typ: TypeMark)
  def int(fieldName: String, description: String, typ: TypeMark)
  def long(fieldName: String, description: String, typ: TypeMark)
  def float(fieldName: String, description: String, typ: TypeMark)
  def double(fieldName: String, description: String, typ: TypeMark)
  def string(fieldName: String, description: String, typ: TypeMark)
  def text(fieldName: String, description: String, typ: TypeMark)
  def binary(fieldName: String, description: String, typ: TypeMark)
  def time(fieldName: String, description: String, typ: TypeMark)
  def timestamp(fieldName: String, description: String, typ: TypeMark)
  def locale(fieldName: String, description: String, typ: TypeMark)
}

