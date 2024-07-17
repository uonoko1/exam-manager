package dto.request.exam.valueObject

import javax.inject._
import domain.exam.valueObject.ExamId
import utils.UlidGenerator

@Singleton
class ExamIdRequestConverter @Inject() (ulidGenerator: UlidGenerator) {
  def validateAndCreate(value: String): Either[String, ExamId] = {
    if (value.isEmpty) Left("ExamId cannot be empty")
    else if (!ulidGenerator.isValid(value)) Left("Invalid ExamId format")
    else Right(ExamId.create(value))
  }
}
