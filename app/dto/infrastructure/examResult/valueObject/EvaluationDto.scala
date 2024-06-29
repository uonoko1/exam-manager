package dto.infrastructure.examResult.valueObject

import domain.examResult.valueObject.Evaluation

case class EvaluationDto(value: String)

object EvaluationDto {
  def fromDomain(evaluation: Evaluation): EvaluationDto = EvaluationDto(
    evaluation.value
  )
  def toDomain(dto: EvaluationDto): Either[String, Evaluation] =
    Evaluation.create(dto.value)
}
