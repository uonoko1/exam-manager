package dto.infrastructure.examResult.valueObject

import domain.examResult.valueObject.ExamResultId

case class ExamResultIdDto(value: String)

object ExamResultIdDto {
  def fromDomain(examResultId: ExamResultId): ExamResultIdDto = ExamResultIdDto(
    examResultId.value
  )
  def toDomain(dto: ExamResultIdDto): Either[String, ExamResultId] =
    ExamResultId.create(dto.value)
}
