package dto.request.examResult.valueObject

import domain.examResult.valueObject.ExamId

object ExamIdRequestConverter {
  def validateAndCreate(value: String): Either[String, ExamId] = {
    if (value.isEmpty) Left("ExamId cannot be empty")
    else Right(ExamId(value))
  }
}
