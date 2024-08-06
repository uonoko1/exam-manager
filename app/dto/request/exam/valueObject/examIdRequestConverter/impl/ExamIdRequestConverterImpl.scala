package dto.request.exam.valueObject.examIdRequestConverter.impl

import javax.inject._
import domain.exam.valueObject.ExamId
import dto.request.exam.valueObject.examIdRequestConverter.`trait`.ExamIdRequestConverter
import utils.UlidGenerator

@Singleton
class ExamIdRequestConverterImpl @Inject() (ulidGenerator: UlidGenerator)
    extends ExamIdRequestConverter {
  override def validateAndCreate(value: String): Either[String, ExamId] = {
    if (value.isEmpty) Left("ExamId cannot be empty")
    else if (!ulidGenerator.isValid(value)) Left("Invalid ExamId format")
    else Right(ExamId.create(value))
  }
}
