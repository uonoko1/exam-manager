package dto.infrastructure.exam.valueObject

import domain.exam.valueObject.ExamId

case class ExamIdDto(value: String)

object ExamIdDto {
  def fromDomain(examId: ExamId): ExamIdDto = ExamIdDto(examId.value)
  def toDomain(dto: ExamIdDto): ExamId = ExamId(dto.value)
}
