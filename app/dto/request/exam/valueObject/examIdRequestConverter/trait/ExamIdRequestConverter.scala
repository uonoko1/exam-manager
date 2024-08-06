package dto.request.exam.valueObject.examIdRequestConverter.`trait`

import domain.exam.valueObject.ExamId

trait ExamIdRequestConverter {
  def validateAndCreate(value: String): Either[String, ExamId]
}
