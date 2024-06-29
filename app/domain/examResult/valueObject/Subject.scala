package domain.examResult.valueObject

sealed trait Subject {
  val value: String
}

case object Math extends Subject {
  val value: String = "Math"
}

case object Science extends Subject {
  val value: String = "Science"
}

case object History extends Subject {
  val value: String = "History"
}

case object English extends Subject {
  val value: String = "English"
}

object Subject {
  def create(value: String): Either[String, Subject] = value match {
    case "Math"    => Right(Math)
    case "Science" => Right(Science)
    case "History" => Right(History)
    case "English" => Right(English)
    case _ =>
      Left(
        s"Invalid subject: $value. Valid subjects are: Math, Science, History, English"
      )
  }
}
