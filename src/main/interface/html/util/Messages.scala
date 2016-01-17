package freight.interface.html.util

import collection.mutable.ArrayBuffer


/** Represents messages on status levels.
  * 
  * This class is a  builder. The return (a map) is not very
  * interesting unless the coder would like to retheme.
  */
//N.B. It's not a buffer because insert is not possible.
class MessageBuilder
    extends scala.collection.mutable.Builder[String, Map[String, Seq[String]]]
{
  private var statuses : ArrayBuffer[String] = ArrayBuffer.empty
  private var warnings : ArrayBuffer[String] = ArrayBuffer.empty
  private var errors : ArrayBuffer[String] = ArrayBuffer.empty

  /** Appends a message.
    * 
    * Defaults to 'status' level
    */
  def +=(msg : String): this.type =  {
    statuses += msg
    this
  }


  def status(msg : String) =  statuses += msg
  def warning(msg : String) = warnings += msg  
  def error(msg : String) = errors += msg
  
  def clear()
  {
    statuses = ArrayBuffer.empty
    warnings = ArrayBuffer.empty
    errors = ArrayBuffer.empty
  }

  /** Tests if errors have been collected.
    */
  def hasErrors
      : Boolean =
  {
    !warnings.isEmpty ||
    !errors.isEmpty
  }
  
  private def levelAddHtml(
    b: StringBuilder,
    levelName: String,
    levelData: ArrayBuffer[String]
  )
  {
    if(!levelData.isEmpty) {
      b ++= " <ul class=\""
      b ++= levelName
      b ++= "\">\n"
      levelData.foreach { msg =>
        b ++= "  <li>"
        b ++= msg
        b ++= "</li>\n"
      }
      b ++= " </ul>\n"
    }
  }
  
  /** Add the data to a string builder
    */
  // Drupal: theme_status_messages()
  def addHtml(b: StringBuilder)
      : StringBuilder =
  {
    b ++= "<div class=\"messages\">\n"
    levelAddHtml(b: StringBuilder, "status", statuses)
    levelAddHtml(b: StringBuilder, "warning", warnings)
    levelAddHtml(b: StringBuilder, "error", errors)
    b ++= "</div>\n";
    b
  }
  
  /** Produces a map of message data.
    */
  def result()
      : Map[String, Seq[String]] =
  {
    Map(
      "status" -> statuses.result.toSeq,
      "warning" -> warnings.result.toSeq,
      "error" -> errors.result.toSeq
    )
  }
  
  override def toString
      : String =
  {
    val b = new StringBuilder("Messages(")
    statuses.addString(b)
    warnings.addString(b)
    errors.addString(b)
    b += ')'
    b.toString
  }
}//MessageBuilder

object MessageBuilder {
  def apply()
      : MessageBuilder =
  {
    new MessageBuilder
  }
}//MessageBuilderO
