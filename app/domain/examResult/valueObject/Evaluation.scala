package domain.examResult.valueObject

sealed trait Evaluation {
  val value: String
}

case object Excellent extends Evaluation {
  val value: String = "Excellent"
}

case object GoodJob extends Evaluation {
  val value: String = "Good Job"
}

case object Passed extends Evaluation {
  val value: String = "Passed"
}

case object Failed extends Evaluation {
  val value: String = "Failed"
}

case object NotEvaluated extends Evaluation {
  val value: String = "Not Evaluated"
}

object Evaluation {
  def create(value: String): Either[String, Evaluation] = value match {
    case "Excellent"     => Right(Excellent)
    case "Good Job"      => Right(GoodJob)
    case "Passed"        => Right(Passed)
    case "Failed"        => Right(Failed)
    case "Not Evaluated" => Right(NotEvaluated)
    case _               => Left(s"Invalid evaluation: $value")
  }
}
