package dto.request.examResult.jsonParser.examResultIdRequestConverter.`trait`

import domain.examResult.valueObject.ExamResultId

trait ExamResultIdRequestConverter {
  def validateAndCreate(value: String): Either[String, ExamResultId]
}
