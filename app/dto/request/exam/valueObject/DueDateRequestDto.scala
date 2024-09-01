package dto.request.exam.valueObject

import dto.utils.dateTime.dateTimeConverter.impl.DateTimeConverterImpl
import javax.inject._
import java.time.ZonedDateTime

case class DueDateRequestDto(value: ZonedDateTime)

class DueDateRequestFactory @Inject() (
    dateTimeConverter: DateTimeConverterImpl
) {

  def create(value: String): Either[String, DueDateRequestDto] =
    if (value.isEmpty) {
      Left("DueDate cannot be empty")
    } else {
      dateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(DueDateRequestDto(zonedDateTime))
        case Left(error)          => Left(s"Invalid CreatedAt format: $error")
      }
    }
}
