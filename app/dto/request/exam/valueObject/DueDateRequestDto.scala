package dto.request.exam.valueObject

import dto.utils.dateTime.dateTimeConverter.`trait`.DateTimeConverter
import javax.inject._
import java.time.ZonedDateTime

case class DueDateRequestDto(val value: ZonedDateTime)

class DueDateRequestFactory @Inject() (
    dateTimeConverter: DateTimeConverter
) {

  def create(value: String): Either[String, DueDateRequestDto] =
    if (value.isEmpty) {
      Left("DueDate cannot be empty")
    } else {
      dateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(DueDateRequestDto(zonedDateTime))
        case Left(error)          => Left("Invalid CreatedAt format")
      }
    }
}
