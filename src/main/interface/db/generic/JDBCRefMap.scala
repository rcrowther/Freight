package freight
package interface
package db
package generic


import java.sql.Connection



/** Templates methods for use in JMDB reference maps.
*
* @define coll jmbc reference map
*/
//TODO: Threads, whatever.
//https://sqlite.org/autoinc.html
// NB: Currently a shell, but necessary for JDBCCollection,
// it's likely JDBC functionality
//will be moved here to be shared. But not now.
trait JDBCRefMap
    extends JDBCCollectionGeneric
with RefMap
{

}//JDBCRefMap

