package dto.request.exam.valueObject

import utils.UlidGenerator
import javax.inject._

case class ExamIdRequestDto(val value: String)

class ExamIdRequestDtoFactory @Inject() (ulidGenerator: UlidGenerator) {
  def create(value: String): Either[String, ExamIdRequestDto] =
    if (value.isEmpty) Left("ExamId cannot be empty")
    else if (!ulidGenerator.isValid(value)) Left("Invalid ExamId format")
    else Right(ExamIdRequestDto(value))
}
