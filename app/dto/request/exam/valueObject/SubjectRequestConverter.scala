package dto.request.exam.valueObject

import domain.exam.valueObject.Subject

object SubjectRequestConverter {
  def validateAndCreate(value: String): Either[String, Subject] = {
    if (value.isEmpty) Left("Subject cannot be empty")
    else Subject.create(value)
  }
}