package dto.request.examResult.jsonParser.studentIdRequestConverter.`trait`

import domain.examResult.valueObject.StudentId

trait StudentIdRequestConverter {
  def validateAndCreate(value: String): Either[String, StudentId]
}
