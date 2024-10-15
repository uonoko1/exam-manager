package dto.request.utils.dateTime

import dto.utils.dateTime.dateTimeConverter.`trait`.DateTimeConverter
import javax.inject.Inject
import java.time.ZonedDateTime

case class UpdatedAtRequestDto(val value: ZonedDateTime)

class UpdatedAtRequestFactory @Inject() (
    dateTimeConverter: DateTimeConverter
) {

  def create(value: String): Either[String, UpdatedAtRequestDto] =
    if (value.isEmpty) {
      Left("UpdatedAt cannot be empty")
    } else {
      dateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(UpdatedAtRequestDto(zonedDateTime))
        case Left(error)          => Left("Invalid UpdatedAt format")
      }
    }
}
