package freight

import scala.language.{implicitConversions, existentials}

package object core {


  // Exceptions and Annotations
  /** An exception that indicates an error during Scala reflection
   */
  case class FreightReflectionException(msg: String) extends Exception(msg)


}
