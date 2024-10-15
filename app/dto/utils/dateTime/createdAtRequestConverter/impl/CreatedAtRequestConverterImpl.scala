package dto.utils.dateTime.createdAtRequestConverter.impl

import domain.utils.dateTime.CreatedAt
import dto.utils.dateTime.createdAtRequestConverter.`trait`.CreatedAtRequestConverter
import javax.inject._
import dto.utils.dateTime.dateTimeConverter.`trait`.DateTimeConverter

@Singleton
class CreatedAtRequestConverterImpl @Inject() (
    dateTimeConverter: DateTimeConverter
) extends CreatedAtRequestConverter {
  override def validateAndCreate(value: String): Either[String, CreatedAt] =
    if (value.isEmpty) {
      Left("CreatedAt cannot be empty")
    } else {
      dateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(CreatedAt.create(zonedDateTime))
        case Left(error)          => Left(s"Invalid CreatedAt format: $error")
      }
    }
}
