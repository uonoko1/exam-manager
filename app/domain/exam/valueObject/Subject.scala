package domain.exam.valueObject

enum Subject(val value: String) {
  case Math extends Subject("Math")
  case Science extends Subject("Science")
  case History extends Subject("History")
  case English extends Subject("English")
}

object Subject {
  def create(value: String): Either[String, Subject] = value match {
    case "Math"    => Right(Subject.Math)
    case "Science" => Right(Subject.Science)
    case "History" => Right(Subject.History)
    case "English" => Right(Subject.English)
    case _ =>
      Left(
        s"Invalid subject: $value. Valid subjects are: Math, Science, History, English"
      )
  }
}
