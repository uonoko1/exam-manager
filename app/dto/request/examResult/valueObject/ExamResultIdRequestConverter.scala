package dto.request.examResult.valueObject

import domain.examResult.valueObject.ExamResultId

object ExamResultIdRequestConverter {
  def validateAndCreate(value: String): Either[String, ExamResultId] = {
    if (value.isEmpty) Left("ExamId cannot be empty")
    else Right(ExamResultId(value))
  }
}
