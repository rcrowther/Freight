package freight
package core
package objects
package generic


/** Builds a map of meta-data for a collection.
*
* Uses the method `descriptiveGiver`. See [[CompanionMeta]].
*/
  private class FieldMetaDataBuilder()
      extends DescriptiveTaker
  {
    private val b = Seq.newBuilder[(Int, String, String)]

    private var i = 0
    
    def boolean(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def short(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def int(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def long(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def float(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def double(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def string(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    //TODO: Make just a label not *binary here*?
    // But not usable in model then...
    def binary(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def text(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def time(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def timestamp(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def locale(fieldName: String, description: String, typ: freight.TypeMark) {
      b += Tuple3(i, fieldName, description)
      i += 1
    }

    def result(): Seq[(Int, String, String)] = b.result

  }//FieldMetaDataBuilder

