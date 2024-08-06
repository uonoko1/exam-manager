package dto.utils.dateTime.dateTimeConverter.`trait`

import java.time.ZonedDateTime

trait DateTimeConverter {
  def stringToZonedDateTime(
      dateTimeString: String
  ): Either[String, ZonedDateTime]
}
