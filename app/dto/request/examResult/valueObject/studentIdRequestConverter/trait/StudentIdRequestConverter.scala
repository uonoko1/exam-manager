package dto.request.examResult.valueObject.studentIdRequestConverter.`trait`

import domain.examResult.valueObject.StudentId

trait StudentIdRequestConverter {
  def validateAndCreate(value: String): Either[String, StudentId]
}
