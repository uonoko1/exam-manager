package dto.request.exam.valueObject.subjectRequestConverter.impl

import domain.exam.valueObject.Subject
import dto.request.exam.valueObject.subjectRequestConverter.`trait`.SubjectRequestConverter
import javax.inject._

@Singleton
class SubjectRequestConverterImpl @Inject() () extends SubjectRequestConverter {
  override def validateAndCreate(value: String): Either[String, Subject] = {
    if (value.isEmpty) Left("Subject cannot be empty")
    else Subject.create(value)
  }
}
