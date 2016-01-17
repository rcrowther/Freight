package freight
package core
package objects
package generic


/** Builds a map of meta-data for a collection.
*
* Uses the method `descriptiveGiver`. See [[CompanionMeta]].
*/
  private class FieldNameBuilder()
      extends DescriptiveTaker
  {
    private val b = Seq.newBuilder[String]

    def boolean(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def short(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def int(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def long(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def float(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def double(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def string(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    //TODO: Make just a label not *binary here*?
    // But not usable in model then...
    def binary(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def text(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def time(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def timestamp(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def locale(fieldName: String, description: String, typ: freight.TypeMark) {
      b += fieldName
    }

    def result(): Seq[String] = b.result

  }//FieldNameBuilder

