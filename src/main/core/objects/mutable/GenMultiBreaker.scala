package freight
package core
package objects
package mutable


/** Templates a breaker guaranteeing methods for reading Freight types.
 *  
 * A breaker deconstructs some source incrementally, by taking a source, then extracting elements using various methods.
 *  
 * @tparam From the type of source accepted.
 */
trait GenMultiBreaker[-From]
//extends MultiGiver
extends GiverForMulti
with BreakerOps[From]

