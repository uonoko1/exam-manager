package dto.request.exam.valueObject.dueDateRequestConverter.impl

import domain.exam.valueObject.DueDate
import dto.utils.dateTime.dateTimeConverter.`trait`.DateTimeConverter
import dto.request.exam.valueObject.dueDateRequestConverter.`trait`.DueDateRequestConverter
import javax.inject._

@Singleton
class DueDateRequestConverterImpl @Inject() (
    dateTimeConverter: DateTimeConverter
) extends DueDateRequestConverter {

  override def validateAndCreate(value: String): Either[String, DueDate] = {
    if (value.isEmpty) {
      Left("DueDate cannot be empty")
    } else {
      dateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(DueDate.create(zonedDateTime))
        case Left(error)          => Left(s"Invalid DueDate format: $error")
      }
    }
  }
}
