


/**
  * Provides easy shunting of objects into and out of interfaces.
  *
  * Objects are abstracted from their various representations. The
  * objects, whatever their representation, can be rolled into, and
  * queried out of, connections. This is slower than
  * hand-coding connection specifics, but more flexible. The code
  * works implicitly, converting objects representations as needed, so
  * should work by simple allocation most of the time. 
  *
  * Interfaces have a unified design, which is either a relief, or autocratic
  * and severely limiting, depening on the coder's outlook.
  *
  * Some of Freight compares to an object-wrapping DBDriver, of which
  * there are several in Java and Scala. See for example Lift's
  * MongoDB interface,
  * [[http://engineering.foursquare.com/2011/01/21/rogue-a-type-safe-scala-dsl-for-querying-mongodb/]],
  * or the speedy Salat,
  * [[https://github.com/novus/salat/]]. Freight is very limited in
  * actions compared to these codebases and, as it also handles
  * iface other than DB drivers (mail, the filesystem, etc.) much
  * more general.
  *
  * The idea of abstract object representations is similar in both
  * intention and usage to
  * [[https://github.com/riedelcastro/frontlets]]. Again, the
  * difference is that Freight has several object representations, not
  * simply Maps, and that these representations can be used within
  * several kinds of connections, not only databases.
  *
  * @define pkg freight
  */
package object freight {
  //import core._

  import scala.annotation._, elidable._

  //type FreightTypeException = java.lang.RuntimeException

 // type Id = core.Id
 // type Text = core.Text
 // type Binary = core.Binary
  //type Time = core.Time
 // type Timestamp = core.Timestamp



  /////////////////////////
  // External Interfaces //
  /////////////////////////

  //type Connection = core.iface.Connection
//type Collection  = core.iface.Collection
//type EPICollection  = core.iface.EPICollection
  //type FieldQueryCollection = core.iface.FieldQueryCollection
  //type BinaryCollection = core.iface.BinaryCollection
  //type GenAnyValCollection = core.iface.GenAnyValCollection
  //type GenBinaryCollection = core.iface.GenBinaryCollection

  type Connection = core.collection.Connection
  type FieldQueryable = core.collection.FieldQueryable
  type BinaryTakable = core.collection.BinaryTakable
  type Generator = core.collection.Generator
  type RefMap = core.collection.RefMap
  type TakableFieldQueryable = core.collection.TakableFieldQueryable
  type GiverTakable = core.collection.GiverTakable



  /////////////////////
  // Freight objects //
  /////////////////////

/** Joins a giver to a taker, selecting fields in order.
*
* Is defined for each object type. Note that the class of takers includes
* builders, and the class of givers includes parsers.
*
* Two major branches of this function are defined, `string` and `multi`. Both types can be found in
* an freight-enabled object. See [[freight.core.objects.CompanionMeta]] .
*/
  type FieldBridge = (Taker, Giver) => Unit

  // The lowest cast objects produced by interfaces.
  // Used for widely acceptable parameters.
  // Currently in GUI
  type GenTakeableOnce = core.objects.GenTakeableOnce
  type GenTakeable = core.objects.GenTakeable

  // Maybe become the main return object.
  // Used in db
  //type TypedMultiTakeable = freight.core.iface.TypedMultiTakeable

  // Imports for user class conversion
  // high cast objects produced by interfaces
// deprecated?
  //type TakeableMultiDefaulted = freight.core.objects.TakeableMultiDefaulted
  //type TakeableClassOps[Repr] = freight.core.objects.TakeableClassOps[Repr]
  type TakeableMultiCompanion[OBJ] = freight.core.objects.TakeableMultiCompanion[OBJ]

  // alternative complementary object
  type CompanionMeta = core.objects.generic.CompanionMeta

  // Complementary object provides CompanionMeta.empty
  val CompanionMeta = core.objects.generic.CompanionMeta

  // Stringified alternative to a user-defined typed classes
  //type StringObject =  freight.core.objects.immutable.StringObject
  //val StringObject =  freight.core.objects.immutable.StringObject



  /////////////////////////////////
  // Object builders and breakers //
  /////////////////////////////////


  //type MultiTaker = freight.core.iface.MultiTaker
  //type StringTaker = freight.core.iface.StringTaker

/** Represents an object input in abstract.
*
* Main type for generating new data.
*/
  type Taker = freight.core.iface.Taker

// Used wherever builders need construction
  type TakerForString = freight.core.iface.TakerForString
  type TakerForMulti = freight.core.iface.TakerForMulti

/** Represents an object output in abstract.
*
* Main type for wrapping input
*/
  type Giver = freight.core.iface.Giver
  val Giver = freight.core.iface.Giver

// Used wherever breakers need construction
  type GiverForString = freight.core.iface.GiverForString
  type GiverForMulti = freight.core.iface.GiverForMulti

  // Used in GUI for type recovery
  //val StringGiver = freight.core.iface.StringGiver

  // Used in GUI and schema builders building.
  type DescriptiveTaker = freight.core.iface.DescriptiveTaker

  // Unused as yet, but may be key...
  type DescriptiveStringTaker = freight.core.iface.DescriptiveStringTaker

  // Both a part of canspeak
  type GenStringBuilder[To] = freight.core.objects.mutable.GenStringBuilder[To]
  type GenStringBreaker = freight.core.objects.mutable.GenStringBreaker

  // For interface builders, Used in db
  type GenMultiBuilder[To] = freight.core.objects.mutable.GenMultiBuilder[To]
  //type GenStringBreaker = freight.core.objects.mutable.GenStringBreaker

  // was macro support
  //type MultiCanGive[OBJ] = core.objects.generic.MultiCanGive[OBJ]
  //type StringCanGive[OBJ] = core.objects.generic.StringCanGive[OBJ]





  /** A function which unloads a breaker into a multi taker.
    *
    * This method tells a breaker how to unload without generic
    * trickery (recovering such information through generics is
    * awkward).
    * 
    * Breakers are givers containing their own material, typically, a
    * parser with data.  However, a breaker does not know what types
    * to unload and when.
    *
    */
  //type StringFieldBridge = (StringTaker, Giver) => Unit
  type StringFieldBridge = (Taker, Giver) => Unit

  // A corresponding type to StringTransformer. So far, unused.
  //type MultiFieldBridge = (MultiTaker, Giver) => Unit
  type MultiFieldBridge = (Taker, Giver) => Unit

/** Take data from an object and give to a string taker. 
*/
// deprecated
  type StringTakerFieldSelect[A] = (Taker, A) => Unit

/** Take data from an object and give to a multi taker. 
*/
  type MultiTakerFieldSelect[A] = (Taker, A) => Unit
  type GenericObject = core.objects.generic.GenericObject

/** Generate an object from a giver. 
*/
  type Take[A] = (Giver) => A

  /** A group of transformer methods.
    */
  // Currently used round GUI.
  //type CanTransform = freight.core.iface.CanTransform

  /** A method giving object field metadata.
    */
  type DescriptiveGiver = (DescriptiveTaker) => Unit



  ///////////////////////////////////////////
  // Id constants and string conversions   //
  ///////////////////////////////////////////

  val NullIDStr: String = "0"
  val NullID: Long = 0
  val InitialID: Long = 1

  /** Convert a string id to long, throwing an error.
    */
  def idOrError(str: String) : Long = {
    try {
      val id = str.toLong
      if (id < 0) {
        error(s"id converted from string is < 0: $str")
      }
      id
    }
    catch {
      case e: Exception => {
        error(s"string supplied as id is invalid for conversion: $str")
        -1
      }
    }

  }

  /** Convert a string id to long, with optional return.
    */
  def idAsOption(str: String) : Option[Long] = {
    try {
      val id = str.toLong
      if (id < 0) {
        log(s"id converted from string is < 0 $str")
        None
      }
      else Some(id)
    }
    catch {
      case e: Exception => {
        log4(s"string supplied as id is invalid for conversion: $str")
        None
      }
    }
  }

  /** Convert a string id to long, with null return.
    */
  def idAsNull(str: String) : Long = {
    try {
      val id = str.toLong
      if (id < 0) {
        log(s"id converted from string is < 0 $str")
        NullID
      }
      else id
    }
    catch {
      case e: Exception => {
        log4(s"string supplied as id is invalid for conversion: $str")
        NullID
      }
    }
  }

  /** Convert a string id to long, with either return.
    *
    * Allows id strings which convert to a `Long` greater than 1.
    *
    * This method is intended for user input. The `Either` return can
    * be used for further message passing, e.g to notify a user of
    * status.
    *
    * @return if unsuccessful, Left carries an error message, else
    *  Right carries the id converted to a Long.
    */
  def idAsEither(idStr: String)
      : Either[String, Long] =
  {
    try {
      val id = idStr.toLong
      if (id < 1) {
        Left("id is < 1")
      }
      else Right(id)
    }
    catch {
      case e: Exception => {
        Left(s"Invalid string as id $idStr")
      }
    }

  }

  /** Convert a string id to long, with either return.
    *
    * Allows id strings which convert to a `Long` greater than 1.
    * Also allows `nullID`, with message if this is adapted.
    *
    * This method is intended for user input. The `Either` return can
    * be used for further message passing, e.g to notify a user of
    * status.
    *
    * @return if unsuccessful, Left carries an error message, else
    *  Right carries the id converted to a Long.
    */
  def idAndNullIdAsEither(idStr: String)
      : Either[String, Long] =
  {
    try {
      val id = idStr.toLong
      if (id > 0 || id == NullID) {
        Right(id)

      }
      else {
        if (id < NullID) {
          Left("id must not be -VE")
        }
        else Left("a null id probably intended, but now invalid")
      }
    }
    catch {
      case e: Exception => {
        Left(s"Invalid string as id $idStr")
      }
    }

  }

  // Exceptions and Annotations
  type ftype = freight.core.annotation.ftype

  /// Fix the anoying stacktrace thing?
  /*
   StringWriter sw = new StringWriter();
   e.printStackTrace(new PrintWriter(sw));
   String exceptionAsString = sw.toString();
   */



  //////////////////////////////
  // Exceptions/ asserts etc. //
  //////////////////////////////

  // quickExit from util/quickExit.h. Allows debugger hook and leak descriptions (JVM gear?)
  // used in fatalities
  def desperateExit() : Nothing = {
    // Should do cleanup/shutdown?
    throw new FreightException(s"Fatal error (shutdown attempted)")
    //System.exit(1)
  }

  // fassert fatal, abort with quickExit?, log
  // msgasserted interally documented.  throw exception, log stacktrace if possible
  // massert interally undocumented. throw exception, log stacktrace if possible
  // uassert user exception, such as capacity exceeded. log, throw exception
  // wassert warning, safe to continue, logs



  case class NoTraceException(msg: String) extends Exception(msg)
      with scala.util.control.NoStackTrace

  /** An exception that indicates an error during freight actions.
    */
  case class FreightException(msg: String) extends Exception(msg)


  /** An exception that indicates an error beyond code understanding.
    */
  case class HorizonException(msg: String) extends Exception(msg)


  /** Log commands at low level.
    *
    * elidable
    */
  @elidable(FINEST) def log(msg: String) = {
    println(msg)
  }

  @elidable(FINEST) def log(e: Exception) = {
    println(e.getMessage())
  }

  /** Log commands at higher level.
    *
    * elidable
    */
  @elidable(FINER) def log2(msg: String) = {
    println(msg)
  }

  /** Log commands at high level.
    *
    * elidable
    */
  @elidable(FINE) def log4(msg: String) = {
    println(msg)
  }



  /** Warnings.
    * 
    * Warnings are non-damaging, and allow continuation.
    *
    * Warnings ask for source file names as, unlike exceptions, they
    * have no inbuilt id
    */
  private def warningCommon(msg: String, srcFileName: String)  {
    log(s"Warning:$srcFileName: $msg")
  }


  /** Prints a logs during $pkg actions.
    */
  def warning(msg: String, srcFileName: String)  {
    warningCommon(msg, srcFileName)
  }

  /** Elidably logs a warning during $pkg actions.
    */
  @elidable(WARNING) def elWarning(msg: String, srcFileName: String)  {
    warningCommon(msg, srcFileName)
  }

  /** Conditionally logs a warning during $pkg actions.
    */
  def warningIf(
    trigger: Boolean,
    msg: String,
    srcFileName: String
  )
  {
    if (trigger) warningCommon(msg, srcFileName)
  }

  /** Elidably and conditionally logs a warning during $pkg actions.
    */
  @elidable(WARNING) def elWarningIf(
    trigger: Boolean,
    msg: String,
    srcFileName: String
  )
  {
    if (trigger) warningCommon(msg, srcFileName)
  }




  import java.io.{StringWriter, PrintWriter}

  private def stacktraceToString(e: Exception)
      : String =
  {
    val sw = new StringWriter
    e.printStackTrace(new PrintWriter(sw))
    sw.toString
  }



  /** Internal errors.
    * 
    * These errors are non-damaging, but terminal.
    */
  private def errorCommon(msg: String) : Nothing =  {
    log(s"Error: $msg")
    throw new FreightException(s"Error: $msg")
    //System.exit(1)
  }


  /** Throws an error.
    */
  def error(msg: String) : Nothing = {
    errorCommon(msg)
  }

  /** Elidably throws an error.
    */
  @elidable(SEVERE) def elError(msg: String) : Nothing =  {
    errorCommon(msg)
  }

  /** Throws an error from a caught exception.
    */
  def error(e: Exception) : Nothing =  {
    errorCommon(s"caught exception:\n${e.getMessage()}\n${stacktraceToString(e)}")
  }

  /** Throws an error from a caught exception.
    */
  def error(msg: String, e: Exception) : Nothing =  {
    errorCommon(s"$msg:\n${e.getMessage()}")
  }

  /** Conditionally throws an error.
    */
  def errorIf(trigger: Boolean, msg: String) {
    if (trigger) {
      errorCommon(msg)
    }
  }

  /** Elidably and conditionally throws an error.
    */
  @elidable(SEVERE) def elErrorIf(trigger: Boolean, msg: String) {
    if (trigger) {
      errorCommon(msg)
    }
  }



  /** Horizon errors.
    * 
    * These errors are from beyond the program and JVM (outside supply
    * of data). Non-damaging, but terminal.
    *
    * Horizon errors carry error Ids, as there is no structure to them
    * otherwise.
    */
  private def herrorCommon(msgId: Int, msg: String) : Nothing = {
    log(s"Horizon error: $msg")
    throw new HorizonException(s"Horizon Error: $msg")
  }

  /** Throws a horizon error.
    */
  def herror(msgId: Int, msg: String) : Nothing = {
    herrorCommon(msgId, msg)
  }

  /** Conditionally throws a horizon error.
    */
  def herrorIf(trigger: Boolean, msgId: Int, msg: String)  {
    if (trigger) {
      herrorCommon(msgId, msg)
    }
  }


  /** Severe errors.
    * 
    * These errors are potentially damaging, and terminal
    */
  private def severeCommon(msg: String) : Nothing = {
    log(s"Severe failure: $msg")
    log(s"\n\n***aborting after severe() failure\n\n")
    desperateExit()
  }

  /** Throws a severe error.
    */
  def severe(msg: String) : Nothing =  {
    severeCommon(msg)
  }

  /** Conditionally throws a severe error.
    */
  def severeIf(trigger: Boolean, msg: String) {
    if (trigger) {
      severeCommon(msg)
    }
  }

}//package
