package dto.request.examResult.valueObject

import utils.UlidGenerator
import javax.inject._

class ExamResultIdRequestDto(val value: String)

class ExamResultIdRequestDtoFactory @Inject() (ulidGenerator: UlidGenerator) {
  def create(value: String): Either[String, ExamResultIdRequestDto] =
    if (value.isEmpty) Left("ExamId cannot be empty")
    else if (!ulidGenerator.isValid(value)) Left("Invalid ExamResultId format")
    else Right(ExamResultIdRequestDto(value))
}
