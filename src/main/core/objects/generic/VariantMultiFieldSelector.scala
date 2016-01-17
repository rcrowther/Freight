package freight
package core
package objects
package generic





object VariantMultiFieldSelector
extends VariantFieldSelector
{

  def apply(fieldData: Seq[(String, TypeMark)], b: Taker, g: Giver) {

    fieldData.foreach{ case(fieldName, typeMark) =>
      typeMark match {

        case BooleanT => b.boolean(fieldName, g.boolean)
        case ShortT => b.short(fieldName, g.short)
        case IntT => b.int(fieldName, g.int)
        case LongT => b.long(fieldName, g.long)
        case DoubleT => b.double(fieldName, g.double)
        case FloatT => b.float(fieldName, g.float)
        case StringT => b.string(fieldName, g.string)
        case TextT => b.text(fieldName, g.text)
        case BinaryT => b.binary(fieldName, g.binary)
        case TimeT => b.time(fieldName, g.time)
        case TimestampT => b.timestamp(fieldName, g.timestamp)
        case LocaleT => b.locale(fieldName, g.locale)
      }
    }
  }

}//VariantMultiFieldSelector
