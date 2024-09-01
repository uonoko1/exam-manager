package dto.utils.dateTime.updatedAtRequestConverter.impl

import domain.utils.dateTime.UpdatedAt
import dto.utils.dateTime.updatedAtRequestConverter.`trait`.UpdatedAtRequestConverter
import dto.utils.dateTime.dateTimeConverter.`trait`.DateTimeConverter
import javax.inject._

@Singleton
class UpdatedAtRequestConverterImpl @Inject() (
    dateTimeConverter: DateTimeConverter
) extends UpdatedAtRequestConverter {
  override def validateAndCreate(value: String): Either[String, UpdatedAt] =
    if (value.isEmpty) {
      Left("UpdatedAt cannot be empty")
    } else {
      dateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(UpdatedAt.create(zonedDateTime))
        case Left(error)          => Left(s"Invalid UpdatedAt format: $error")
      }
    }
}
