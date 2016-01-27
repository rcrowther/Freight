package freight
package interface
package db

/** Simple enumeration of connection type to string.
*/
object ConnectionType {
val filebase = 0
val sqlitejdbc = 1

def toMachineString(connectionType: Int)
: String =
{
connectionType match {
case 0 => "filebase"
case 1 => "sqltjdbc"
}
}

def fromMachineString(connectionType: String)
: Int =
{
connectionType match {
case "filebase" => 0
case "sqltjdbc" => 1
case _ => error(s"Unrecognised machine string connectionType:$connectionType") 
}
}

def toString(connectionType: Int)
: String =
{
connectionType match {
case 0 => "Filebase"
case 1 => "SQLiteJDBC"
case _ => s"Unrecognised type enum connectionType:$connectionType" 
}
}
}//ConnectionType
