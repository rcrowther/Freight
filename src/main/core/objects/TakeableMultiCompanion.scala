package freight
package core
package objects


import scala.language.experimental.macros

import iface._


/** A trait adding takers to user data-carrying classes.
  * 
  * To implement a concrete class, you need to provide an
  * implementation of the following two methods:
  * {{{
  * def apply(g: MultiGiver) : OBJ
  * def apply(g: StringGiver) : OBJ
  * }}}
  *
  * e.g,
  * {{{
  *   def apply(g: MultiGiver)
  *     : Paper = 
  *   {
  *     new Paper (
  *       g.long(),
  *       g.string()
  *     )
  *   }
  * 
  *   def apply(g: StringGiver)
  *     : Paper = 
  *   {
  *     new Paper (
  *       g.long().toLong,
  *       // No need for a conversion on string
  *       g.string()
  *     )
  *   }
  * }}}
  *
  */
trait TakeableMultiCompanion[OBJ]
{
  //def apply(g: MultiGiver) : OBJ = macro Macros.ctorMG[OBJ]
  def apply(g: Giver) : OBJ
  //def apply(g: StringGiver) : OBJ = macro Macros.ctorSG[OBJ]
  //def apply(g: StringGiver) : OBJ
}
