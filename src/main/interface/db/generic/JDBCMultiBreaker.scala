package freight
package interface
package db
package generic

import java.util.Locale
import java.sql.ResultSet
import core.objects.mutable.GenMultiBreaker

//import freight.core.objects.mutable.GenMultiBuilder


/** Breaker suitable for use with JDBC result sets.
*/
// TODO: Blob not sorted at all
class JDBCMultiBreaker(var rs: ResultSet)
    extends GenMultiBreaker[ResultSet]
    //extends GiverForMulti
{
// Yup, indexing starts at 1
  var i = 0

  def boolean() : Boolean= {
    i += 1
    rs.getBoolean(i)
  }

  def short() : Short ={
    i += 1
    rs.getShort(i)
  }

  def int() : Int = {
    i += 1
    rs.getInt(i)
  }

  def long() : Long = {
    i += 1
    // TODO: Rescue rowId?
    rs.getLong(i)
  }

  def float() : Float= {
    i += 1
    rs.getFloat(i)
  }

  def double() : Double= {
    i += 1
    rs.getDouble(i)
  }

  def string() : String= {
    i += 1
    rs.getString(i)
  }

// TODO: proper text/varchar?
  def text() : String = {
    i += 1
    rs.getString(i)
  }

  def binary() : Array[Byte] = {
    i += 1
    val blob = rs.getBlob(i)
val len = blob.length.toInt
val ret = blob.getBytes(1, len)
blob.free()
ret
  }

  def time() : Long = {
    i += 1
    rs.getLong(i)
  }

  def timestamp() : Long = {
    i += 1
    rs.getLong(i)
  }

  def locale() : Locale = {
    i += 1
    val s = rs.getString(i).split('_')
    new Locale(s(0), s(1), s(2))
    
  }

def reload(s: ResultSet)
//: JDBCMultiBreaker = 
{
 i = 0
rs = s
//this
}

}//JDBCMultiBreaker
