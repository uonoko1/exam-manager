package dto.infrastructure.exam.valueObject

import domain.exam.valueObject._

case class EvaluationStatusDto(value: String)

object EvaluationStatusDto {
  def fromDomain(evaluationStatus: EvaluationStatus): EvaluationStatusDto =
    EvaluationStatusDto(EvaluationStatus.value)
  def toDomain(dto: EvaluationStatusDto): EvaluationStatus =
    EvaluationStatus.create(dto.value)
}
