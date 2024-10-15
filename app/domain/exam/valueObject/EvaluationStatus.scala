package domain.exam.valueObject

enum EvaluationStatus(val value: String) {
  case Evaluated extends EvaluationStatus("Evaluated")
  case PartiallyEvaluated extends EvaluationStatus("Partially Evaluated")
  case NotEvaluated extends EvaluationStatus("Not Evaluated")
}

object EvaluationStatus {
  def create(value: String): Either[String, EvaluationStatus] = value match {
    case "Evaluated"           => Right(EvaluationStatus.Evaluated)
    case "Partially Evaluated" => Right(EvaluationStatus.PartiallyEvaluated)
    case "Not Evaluated"       => Right(EvaluationStatus.NotEvaluated)
    case _ =>
      Left(
        s"Invalid EvaluationStatus: $value. Valid EvaluationStatus are: Evaluated, Partially Evaluated, Not Evaluated"
      )
  }
}
