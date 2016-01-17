package freight.core.objects.mutable


import freight.core.iface.ManyObjectsTakerForString




/** Templates a builder guaranteeing methods for writing Freight types.
*
 * A builder constructs some destination incrementally, by inserting elements using various methods, then returning data using `result`.
 *  
 * @tparam To the type of result produced.
 */
trait GenManyObjectsStringBuilder[+To]
extends ManyObjectsTakerForString
with BuilderOps[To]
