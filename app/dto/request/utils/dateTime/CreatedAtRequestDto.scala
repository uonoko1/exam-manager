package dto.request.utils.dateTime

import dto.utils.dateTime.dateTimeConverter.`trait`.DateTimeConverter
import javax.inject.Inject
import java.time.ZonedDateTime

case class CreatedAtRequestDto(val value: ZonedDateTime)

class CreatedAtRequestFactory @Inject() (
    dateTimeConverter: DateTimeConverter
) {

  def create(value: String): Either[String, CreatedAtRequestDto] =
    if (value.isEmpty) {
      Left("CreatedAt cannot be empty")
    } else {
      dateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(CreatedAtRequestDto(zonedDateTime))
        case Left(error)          => Left("Invalid CreatedAt format")
      }
    }
}
