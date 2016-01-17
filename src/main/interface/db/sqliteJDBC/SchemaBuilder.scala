package freight
package interface
package db
package sqliteJDBC



/** Generates sql statements suitable for use with SQLiteJDBC.
*
* NB: Id must be an integer id to trigger efficient autoincrement.
*
* See [[https://sqlite.org/autoinc.html]]
  */
final class SchemaBuilder(
  colName : String
)
    extends generic.JDBCSchemaBuilder(
  colName,
      s"CREATE TABLE $colName ( id integer PRIMARY KEY"
)

