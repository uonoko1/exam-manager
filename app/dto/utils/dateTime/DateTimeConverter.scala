package dto.utils.dateTime

import java.time.ZonedDateTime
import scala.util.Try

object DateTimeConverter {

  def stringToZonedDateTime(
      dateTimeString: String
  ): Either[String, ZonedDateTime] = {
    Try(ZonedDateTime.parse(dateTimeString)).toEither.left.map(e =>
      s"Invalid input. Error: ${e.getMessage}"
    )
  }
}
