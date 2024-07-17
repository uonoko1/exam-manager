package dto.request.examResult.valueObject

import javax.inject._
import domain.examResult.valueObject.StudentId
import utils.UlidGenerator

@Singleton
class StudentIdRequestConverter @Inject() (ulidGenerator: UlidGenerator) {
  def validateAndCreate(value: String): Either[String, StudentId] = {
    if (value.isEmpty) Left("StudentId cannot be empty")
    else if (!ulidGenerator.isValid(value)) Left("Invalid StudentId format")
    else Right(StudentId.create(value))
  }
}
