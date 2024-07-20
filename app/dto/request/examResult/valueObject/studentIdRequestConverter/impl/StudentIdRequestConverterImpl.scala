package dto.request.examResult.valueObject.studentIdRequestConverter.impl

import javax.inject._
import domain.examResult.valueObject.StudentId
import dto.request.examResult.valueObject.studentIdRequestConverter.`trait`.StudentIdRequestConverter
import utils.UlidGenerator

@Singleton
class StudentIdRequestConverterImpl @Inject() (ulidGenerator: UlidGenerator)
    extends StudentIdRequestConverter {
  override def validateAndCreate(value: String): Either[String, StudentId] = {
    if (value.isEmpty) Left("StudentId cannot be empty")
    else if (!ulidGenerator.isValid(value)) Left("Invalid StudentId format")
    else Right(StudentId.create(value))
  }
}
