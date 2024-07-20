package dto.request.exam.valueObject.subjectRequestConverter.`trait`

import domain.exam.valueObject.Subject

trait SubjectRequestConverter {
  def validateAndCreate(value: String): Either[String, Subject]
}
