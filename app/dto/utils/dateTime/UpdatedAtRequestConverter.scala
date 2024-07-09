package dto.utils.dateTime

import domain.utils.dateTime.UpdatedAt
import dto.utils.dateTime.DateTimeConverter

object UpdatedAtRequestConverter {

  def validateAndCreate(value: String): Either[String, UpdatedAt] = {
    if (value.isEmpty) {
      Left("UpdatedAt cannot be empty")
    } else {
      DateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(UpdatedAt.create(zonedDateTime))
        case Left(error)          => Left(s"Invalid UpdatedAt format: $error")
      }
    }
  }
}
