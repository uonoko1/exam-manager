package dto.request.examResult.valueObject

import utils.UlidGenerator
import javax.inject._

case class StudentIdRequestDto(val value: String)

class StudentIdRequestDtoFactory @Inject() (ulidGenerator: UlidGenerator) {
  def create(value: String): Either[String, StudentIdRequestDto] =
    if (value.isEmpty) Left("StudentId cannot be empty")
    else if (!ulidGenerator.isValid(value)) Left("Invalid StudentId format")
    else Right(StudentIdRequestDto(value))
}
