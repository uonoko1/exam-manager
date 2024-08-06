package dto.request.examResult.valueObject.examResultIdRequestConverter.`trait`

import domain.examResult.valueObject.ExamResultId

trait ExamResultIdRequestConverter {
  def validateAndCreate(value: String): Either[String, ExamResultId]
}
