package dto.utils.dateTime.dateTimeConverter.impl

import dto.utils.dateTime.dateTimeConverter.`trait`.DateTimeConverter
import java.time.ZonedDateTime
import scala.util.Try
import javax.inject._

@Singleton
class DateTimeConverterImpl @Inject() () extends DateTimeConverter {
  override def stringToZonedDateTime(
      dateTimeString: String
  ): Either[String, ZonedDateTime] = {
    Try(ZonedDateTime.parse(dateTimeString)).toEither.left.map(e =>
      s"Invalid input. Error: ${e.getMessage}"
    )
  }
}
