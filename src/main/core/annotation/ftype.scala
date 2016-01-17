package freight.core.annotation


/** Specifies how a class construction value is handled within freight.
*
* an implicit AnyVal type and field description.
 *
 * @param typ an implicit type as a String. If necessary, the value will be converted to this type via an AnyVal. Through interfaces, the value will be written as this type (so will appear in XML output, for example, as having this implicit type)
* @param dsc a description of the field. This will be used in public-facing interfaces, for example, the HTML interface will write HTML with this value to explain to users the purpose of the field in forms.
 */
final class ftype(typ: String = "", dsc: String = "") extends scala.annotation.StaticAnnotation {}
