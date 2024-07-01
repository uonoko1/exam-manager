package domain.exam.valueObject

sealed trait EvaluationStatus {
  val value: String
}

case object NotEvaluated extends EvaluationStatus {
  val value: String = "Not Evaluated"
}

case object PartiallyEvaluated extends EvaluationStatus {
  val value: String = "Partially Evaluated"
}

case object Evaluated extends EvaluationStatus {
  val value: String = "Evaluated"
}

object EvaluationStatus {
  def create(value: String): Either[String, EvaluationStatus] = value match {
    case "Evaluated"           => Right(Evaluated)
    case "Partially Evaluated" => Right(PartiallyEvaluated)
    case "Not Evaluated"       => Right(NotEvaluated)
    case _ =>
      Left(
        s"Invalid EvaluationStatus: $value. Valid EvaluationStatus are: Evaluated, Partially Evaluated, Not Evaluated"
      )
  }
}
