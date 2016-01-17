package freight
package interface
package db





/** Connector for the SQLite database.
  *
  * This connector uses the SQLiteJDBC driver from,
  * [[https://github.com/xerial/sqlite-jdbc]]
  *
  * == In comparison ==
  *
  * A conservative sort of estimate is that SQLite can handle 2 hits
  * a second.
  *
  * Use SQLite when it would be advantageous to have data stored in a
  * portable file, using few resources with easy access. SQLite is
  * widely used in embedded software, and graphical tools, so use it
  * if you need to port, or are building for such tools.
  *
  * Do not use SQLite where high concurrency is required, guarantees
  * on locking (especially for security), or data may exceed a few
  * Gigabytes. By using a disk file, SQLite relies on fthe filesystem
  * for locks, which is not reliable. SQLite can write 140 Terabyte
  * file, but handling the file is difficult, it may become
  * unreadable.
  *
  * "A good rule of thumb is to avoid using SQLite in situations where
  * the same database will be accessed directly (without an
  * intervening application server) and simultaneously from many
  * computers over a network."
  *
  *
  * == Alternatives ==
  *
  * [[freight.interface.db.base]] is slower than SQLite, but for singular queries is
  * faster. It stores files in human-readable form, and allows
  * multiple writers. If the need is to see and preserve files,
  * e.g. photo storage or human-editable data, use
  * [[freight.interface.db.base]]. Otherwise use SQLite.
  *
  * There is no comparison between these two and big databases. Almost
  * always, one or the other is prefereable.
  *
  * == Setup ==
  * Download the connector,
  *
* The driver should be downloaded and placed somewhere in the
* Java compile path (scala/lib works, if not ideal). Recompile.
  *
  */
package object sqliteJDBC {

}
