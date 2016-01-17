package freight
package core
package objects
package generic



// How to further equip this for building is the big issue.
// There must be types coming in?
// If a builder is used, it must be individual anyhow - not a great improvement.
// So could bite that bullet, and pass the object, thus a constructor, in.
// But if the glue is a Tuple, then that is limited to 22 parameters?
// Without tuples, you need individual builders
// GetInt idiom is common to both MongoDDB and SQL connectors.

/** A template class for companion objects of object classes.
  *  
  * Such object classes usually inherit from trait `GenericObjectTemplate`.
  * This is so they can use companion objects which derive from this class.
  *  
  * This is the base of object companions.
  *  
  *  @tparam  OBJ  The type constructor representing the object class.
  *  @define obj  fobject
  *  @define Obj  `OBJ`
  */
//GenericCompanion is heavily typed,
// but we go for everything which is fixed typed
// from GenericCompanion, absorbs GenTraversableFactory, ignores GenericTraversableTemplate

//TOCONSIDER: The generic is not strictly necessary, but may be needed later...
abstract class GenericCompanion[OBJ]
{

 // def multiTakerFieldSelector : MultiTakerFieldSelect[OBJ]
  //def multiTakerFieldSelector : (OBJ, MultiTaker) => Unit

  //def stringTakerFieldSelector : StringTakerFieldSelect[OBJ]
  //def stringTakerFieldSelector : (OBJ, StringTaker) => Unit

  /** The overall type of class which this complements.
    *  
    *  The meta object is unknown.
    */
  //protected[this] type Obj = OBJ





  /*
   private[this] val ReusableBreakerInstance: GenericCanBreak[Nothing] =
   new GenericCanBreak[Nothing]
   
   def ReusableCBF: GenericCanBreak[Nothing] = ReusableBreakerInstance
   */

 

  // NB: Due to macro calls, the below must be realised, hence the error thowing.

  /** Creates a new $obj from a giver.
    */
  //def apply(g: MultiGiver) : OBJ = throw new Exception("base class call on method without macro implementation")

  /** Creates a new $obj from a string giver.
    */
  //def apply(g: StringGiver) : OBJ = throw new Exception("base class call on method without macro implementation")

}
