package freight
package interface
package file


/** Crude database API for files in directories.
  * 
  * FileBase is a crude persistent disc-based data storage device. It
  * stores files, one file per record, with the freight id as the
  * name. To read, it opens files and draws file data as a
  * string. FileBase is usually configured with some human-readable string
  * representation such as XML or JSON.
  * 
  * Imitating a database, FileBase has a [[SyncConnection]]. This mimics
* the database concept of `tables`. On disc, the parent folder
* is given to the connection, and `tables`, or `collections` are allocated
* to folders below the connection folder. There is no imitation of the
* database concept of allocating multiple `databases`, as requiring knowledge
* of file structure three levels deep adds little to the concept, and limits
* the ability of FileBase to lock onto any folder, even those it has not created itself. 
*
  * Compared to a database FileBase is slow (perhaps two orders slower
  * than a dedicated database) and the OS calls consume memory. But FileBase is not
  * as bad as appears. The collection classes write data on their state, so
  * startup is no more expensive than a file read, and the code can handle
  * incrementing object ids. Concurrency for a FileBase is
  * controlled by a granular lock, so the syncronized collections will never write when other code
  * is reading. And, even if not at the level of databases, most
  * filesystems offer some sort of journaling (backup).
  *
  * FileBase stores regular files; human-readable, easy to port, edit,
  * and backup. If there is no great need for speed, if data is
  * cached elsewhere (e.g. a Squid server fronting an HTML output), or highly portable human-readable data is required,
  * then the flexibilty may beat the speed payoff.
  *
  * == In comparison ==
  *
  * A conservative sort of estimate is that FileBase can handle 2 hits a second.
  *
  * Use FileBase when it would be advantageous to have data stored in
  * files. Files are universally transportable and easy to handle.
  *
  * Do not use FileBase where security is required. By using disk file operations,
  * Base relies on the filesystem for locks, which is not reliable.
  *
  * == Alternatives ==
  *
  * [[freight.interface.db.sqliteJDBC]] is much more fully-rounded then FileBase. It
  * recovers multiple records at speeds that outdo FileBase by multiple
  * orders (it will hold the single base file open). [[freight.interface.db.sqliteJDBC]] can perform field queries. For retrieval of complete and random objects, [[freight.interface.db.sqliteJDBC]] is slower (FileBase does no journalling).
  *
  * If the need is to see files and recover in a singular manner,
  * e.g. photo storage or human-editable data, use
  * FileBase. Otherwise use SQLLite.
  *
  * There is no comparison between these two and big databases. Almost
  * always, one or the other is prefereable.
  *
  * == Refs ==
  *
  * FileBase sounds like FileMaker,
  *
  * [[https://en.wikipedia.org/wiki/FileMaker]]
  *
  * It isn't.
  *
  */
package object filebase {

}//package
