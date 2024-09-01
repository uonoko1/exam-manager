package dto.request.examResult.valueObject

class EvaluationRequestDto(value: String)

object EvaluationRequestDto {
  def create(value: String): EvaluationRequestDto =
    new EvaluationRequestDto(value)
}
