package dto.infrastructure.exam.valueObject

import domain.exam.valueObject._

case class EvaluationStatusDto(value: String)

object EvaluationStatusDto {
  def fromDomain(evaluationStatus: EvaluationStatus): EvaluationStatusDto =
    EvaluationStatusDto(evaluationStatus.value)
  def toDomain(dto: EvaluationStatusDto): Either[String, EvaluationStatus] =
    EvaluationStatus.create(dto.value)
}
