package domain.examResult.valueObject

enum Evaluation(val value: String) {
    case Excellent extends Evaluation("Excellent")
    case GoodJob extends Evaluation("Good Job")
    case Passed extends Evaluation("Passed")
    case Failed extends Evaluation("Failed")
    case NotEvaluated extends Evaluation("Not Evaluated")
}

object Evaluation {
  def create(value: String): Either[String, Evaluation] = value match {
    case "Excellent"     => Right(Evaluation.Excellent)
    case "Good Job"      => Right(Evaluation.GoodJob)
    case "Passed"        => Right(Evaluation.Passed)
    case "Failed"        => Right(Evaluation.Failed)
    case "Not Evaluated" => Right(Evaluation.NotEvaluated)
    case _               => Left(s"Invalid evaluation: $value")
  }
}
