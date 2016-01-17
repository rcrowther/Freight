package freight
package core
package objects
package mutable



/** Templates a builder guaranteeing methods for writing Freight types.
*
 * A builder constructs some destination incrementally, by inserting elements using various methods, then returning data using `result`.
 *  
 * @tparam To the type of result produced.
 */
//unused?
trait GenMultiBuilder[+To]
//extends MultiTaker
extends TakerForMulti
with BuilderOps[To]

