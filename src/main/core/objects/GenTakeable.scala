package freight
package core
package objects

//import freight.core.iface.{MultiTaker, StringTaker}
//import generic.{GenericCompanion}
//import generic.CanGive


// Like TraversableOnce[+A] and TraversableLike[+A, +Repr](it has a companion)
// FObjectLike
// NB: Repr is either a FObject or a Meta, both can be realized without another Generic parameter. However, in generic objects, we may need to know the thing?  

/** A trait for classes which wish to implement givers.
 *
 */
// Like GenTraversable, but realised like TraversableLike
trait GenTakeable extends Any
with GenTakeableOnce
{


  //def repr: OBJ = this.asInstanceOf[OBJ]
  //protected[this] type Self = OBJ

 // final def isGivableAgain: Boolean = true

  // Creates a new builder for this collection type.
  //protected[this] def newMultiBuilder: GenMultiBuilder[Repr]


// Like '++' in GenTraversableLike. At a stretch.
// sadly, I think this needs the implicit parameters i.e.
  //def give(t: MultiTaker): Unit 
  //def give(t: StringTaker): Unit

  // From GenTraversable defined in GenericTraversableTemplate
  // There implemented, but returning a CanGive[Nothing] is not helpful?
  //def companion: GenericCompanion
}


