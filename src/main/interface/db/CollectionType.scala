package freight
package interface
package db


/** Simple enumeration of collection type to string.
*/
object CollectionType {
val giver = 0
val fieldquery = 1
val refmap = 3
val binary = 4


def toMachineString(collectionType: Int)
: String =
{
collectionType match {
case 0 => "gvr"
case 1 => "fdq"
case 2 => "rmp"
case 3 => "bin"
}
}

def fromMachineString(collectionType: String)
: Int =
{
collectionType match {
case "gvr" => 0
case "fdq" => 1
case "rmp" => 2
case "bin" => 3
case _ => error(s"Unrecognised machine string collectionType:$collectionType") 
}
}

def toString(collectionType: Int)
: String =
{
collectionType match {
case 0 => "Giver"
case 1 => "FieldQuery"
case 2 => "Refmap"
case 3 => "binaryTakable"
case _ => s"Unrecognised type enum collectionType:$collectionType" 
}
}
}//CollectionType
