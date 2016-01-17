package freight
package core
package objects
package generic


/** Builds a map of meta-data for a collection.
*
* Uses the method `descriptiveGiver`. See [[CompanionMeta]].
*/
  private class FieldNameTypeBuilder()
      extends DescriptiveTaker
  {
    private val b = Seq.newBuilder[(String, TypeMark)]

    def boolean(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def short(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def int(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def long(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def float(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def double(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def string(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    //TODO: Make just a label not *binary here*?
    // But not usable in model then...
    def binary(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def text(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def time(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def timestamp(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def locale(fieldName: String, description: String, typ: freight.TypeMark) {
      b += (fieldName -> typ)
    }

    def result(): Seq[(String, TypeMark)] = b.result

  }//FieldNameTypeBuilder

