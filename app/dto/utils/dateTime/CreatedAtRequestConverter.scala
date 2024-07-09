package dto.utils.dateTime

import domain.utils.dateTime.CreatedAt
import dto.utils.dateTime.DateTimeConverter

object CreatedAtRequestConverter {

  def validateAndCreate(value: String): Either[String, CreatedAt] = {
    if (value.isEmpty) {
      Left("CreatedAt cannot be empty")
    } else {
      DateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(CreatedAt.create(zonedDateTime))
        case Left(error)          => Left(s"Invalid CreatedAt format: $error")
      }
    }
  }
}
