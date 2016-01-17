package freight.interface.html.servlet

object Config {

  /** Salts and Private keys are used to encrypt data sent out from
    * Freight forms.
    * 
    * These configuration values should be set by hand. Freight will
    not compile otherwise.
    * 
    * Both values are hexadecimal Strings. They can be any length. If
    * the application is to be secure both keys should, using bytes as
    * the base data, be at least 16 bytes long. About 55 bytes length
    * is recommended. For further info, see the help file.
    */
  // Drupal salt is either Base64ed random,
  // or encoded dB id references.
  // TODO: associate salts with users?
  // TODO: is salt protected in this open config?
  // TODO: Salt needs saving, not fixing here?
  val salt: String = "2ca62291c310c7b586b31d3ed5313fe62ac033445789928500a8d4ba3a5535516522c5834a434ed426d59536ebae700b622177b465c6f3"

  val privateKey: String = "cad5ae32d2658e2e873293476e7df8b87fe1466ccfd69c64d0c5ca839763076849f678b93f16af7f6925e22c3ef17937a7acc7d44499b3"

}//Config
