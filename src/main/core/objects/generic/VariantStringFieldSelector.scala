package freight
package core
package objects
package generic





object VariantStringFieldSelector
extends VariantFieldSelector
{

  def apply(fieldData: Seq[(String, TypeMark)], b: Taker, g: Giver) {

    fieldData.foreach{ case(fieldName, typeMark) =>
      typeMark match {

        case BooleanT => b.booleanStr(fieldName, g.booleanStr)
        case ShortT => b.shortStr(fieldName, g.shortStr)
        case IntT => b.intStr(fieldName, g.intStr)
        case LongT => b.longStr(fieldName, g.longStr)
        case DoubleT => b.doubleStr(fieldName, g.doubleStr)
        case FloatT => b.floatStr(fieldName, g.floatStr)
        case StringT => b.stringStr(fieldName, g.stringStr)
        case TextT => b.textStr(fieldName, g.textStr)
        case BinaryT => b.binaryStr(fieldName, g.binaryStr)
        case TimeT => b.timeStr(fieldName, g.timeStr)
        case TimestampT => b.timestampStr(fieldName, g.timestampStr)
        case LocaleT => b.localeStr(fieldName, g.localeStr)
      }
    }
  }

}//VariantStringFieldSelector
