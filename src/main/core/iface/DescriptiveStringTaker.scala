package freight
package core
package iface



/** Templates methods for receiving Freight types as strings.
 *  
 * Implementations of this trait are given descriptive information and
 * stringified data from freight fields.
 * 
 * Inheriting classes are often builders.
 *
 */
trait DescriptiveStringTaker
extends Any
{
  def boolean(fieldName: String, description: String, v: String)
  def short(fieldName: String, description: String, v: String)
  def int(fieldName: String, description: String, v: String)
  def long(fieldName: String, description: String, v: String)
  def float(fieldName: String, description: String, v: String)
  def double(fieldName: String, description: String, v: String)
  def string(fieldName: String, description: String, v: String)
  def text(fieldName: String, description: String, v: String)
  def binary(fieldName: String, description: String, v: String)
  def time(fieldName: String, description: String, v: String)
  def timestamp(fieldName: String, description: String, v: String)
  def locale(fieldName: String, description: String, v: String)
}
