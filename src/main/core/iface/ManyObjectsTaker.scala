package freight
package core
package iface

import java.util.Locale 



/** Templates methods for receiving Freight types.
 *  
 * Implementations of this class are given descriptive information and
 * typed data from freight fields.
 *
 */
//was GenGatherer
trait ManyObjectsTaker
extends Taker
with ManyObjects

