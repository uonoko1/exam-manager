package dto.request.exam.valueObject

import domain.exam.valueObject.DueDate
import dto.utils.dateTime.DateTimeConverter

object DueDateRequestConverter {

  def validateAndCreate(value: String): Either[String, DueDate] = {
    if (value.isEmpty) {
      Left("DueDate cannot be empty")
    } else {
      DateTimeConverter.stringToZonedDateTime(value) match {
        case Right(zonedDateTime) => Right(DueDate.create(zonedDateTime))
        case Left(error)          => Left(s"Invalid DueDate format: $error")
      }
    }
  }
}
