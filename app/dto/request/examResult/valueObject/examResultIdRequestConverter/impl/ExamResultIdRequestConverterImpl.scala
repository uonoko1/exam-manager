package dto.request.examResult.valueObject.examResultIdRequestConverter.impl

import domain.examResult.valueObject.ExamResultId
import dto.request.examResult.valueObject.examResultIdRequestConverter.`trait`.ExamResultIdRequestConverter
import utils.UlidGenerator
import javax.inject._

@Singleton
class ExamResultIdRequestConverterImpl @Inject() (ulidGenerator: UlidGenerator)
    extends ExamResultIdRequestConverter {
  override def validateAndCreate(
      value: String
  ): Either[String, ExamResultId] = {
    if (value.isEmpty) Left("ExamId cannot be empty")
    else if (!ulidGenerator.isValid(value)) Left("Invalid ExamResultId format")
    else Right(ExamResultId(value))
  }
}
