package freight



sealed trait TypeMark {}


case object BooleanT extends TypeMark {
  override def toString = "Boolean"
}

case object ShortT extends TypeMark {
  override def toString = "Short"
}

case object IntT extends TypeMark {
  override def toString = "Int"
}

case object LongT extends TypeMark {
  override def toString = "Long"
}

case object FloatT extends TypeMark {
  override def toString = "Float"
}

case object DoubleT extends TypeMark {
  override def toString = "Double"
}

case object StringT extends TypeMark {
  override def toString = "String"
}

case object TextT extends TypeMark {
  override def toString = "Text"
}


/** Types an Array[Byte] as a 'Binary' value.
 *
 */
case object BinaryT extends TypeMark {
  override def toString = "Binary"
}

case object TimeT extends TypeMark {
  override def toString = "Time"
}

case object TimestampT extends TypeMark {
  override def toString = "Timestamp"
}

case object LocaleT extends TypeMark {
  override def toString = "Timestamp"
}
