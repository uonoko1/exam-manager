package dto.request.exam.valueObject

enum EvaluationStatusRequestDto(value: String) {
  case Evaluated extends EvaluationStatusRequestDto("Evaluated")
  case PartiallyEvaluated
      extends EvaluationStatusRequestDto("Partially Evaluated")
  case NotEvaluated extends EvaluationStatusRequestDto("Not Evaluated")
}

object EvaluationStatusRequestDto {

  def create(value: String): Either[String, EvaluationStatusRequestDto] =
    if (value.isEmpty) {
      Left("EvaluationStatusRequestDto cannot be empty")
    } else {
      value match {
        case "Evaluated" => Right(EvaluationStatusRequestDto.Evaluated)
        case "Partially Evaluated" =>
          Right(EvaluationStatusRequestDto.PartiallyEvaluated)
        case "Not Evaluated" => Right(EvaluationStatusRequestDto.NotEvaluated)
        case _ =>
          Left(
            s"Invalid EvaluationStatusRequestDto: $value. Valid EvaluationStatusRequestDto are: Evaluated, Partially Evaluated, Not Evaluated"
          )
      }
    }
}
