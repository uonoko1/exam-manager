package dto.request.examResult.valueObject

enum EvaluationRequestDto(val value: String) {
  case Excellent extends EvaluationRequestDto("Excellent")
  case GoodJob extends EvaluationRequestDto("Good Job")
  case Passed extends EvaluationRequestDto("Passed")
  case Failed extends EvaluationRequestDto("Failed")
  case NotEvaluated extends EvaluationRequestDto("Not Evaluated")
}

object EvaluationRequestDto {
  def create(value: String): Either[String, EvaluationRequestDto] =
    if (value.isEmpty) {
      Left("EvaluationRequestDto cannot be empty")
    } else {
      value match {
        case "Excellent"     => Right(EvaluationRequestDto.Excellent)
        case "Good Job"      => Right(EvaluationRequestDto.GoodJob)
        case "Passed"        => Right(EvaluationRequestDto.Passed)
        case "Failed"        => Right(EvaluationRequestDto.Failed)
        case "Not Evaluated" => Right(EvaluationRequestDto.NotEvaluated)
        case _               => Left(s"Invalid evaluation format")
      }
    }
}
