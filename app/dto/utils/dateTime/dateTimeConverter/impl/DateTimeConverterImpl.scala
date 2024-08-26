package dto.utils.dateTime.dateTimeConverter.impl

import dto.utils.dateTime.dateTimeConverter.`trait`.DateTimeConverter
import scala.util.Try
import java.time.{ZonedDateTime, OffsetDateTime, Instant}
import javax.inject._

@Singleton
class DateTimeConverterImpl extends DateTimeConverter {
  override def stringToZonedDateTime(
      dateTimeString: String
  ): Either[String, ZonedDateTime] = {
    if (
      Try(OffsetDateTime.parse(dateTimeString)).isSuccess || Try(
        Instant.parse(dateTimeString)
      ).isSuccess
    ) {
      Left(
        "Invalid input. Error: OffsetDateTime or Instant format is not allowed."
      )
    } else {
      Try(ZonedDateTime.parse(dateTimeString)).toEither.left.map(e =>
        s"Invalid input. Error: ${e.getMessage}"
      )
    }
  }
}
