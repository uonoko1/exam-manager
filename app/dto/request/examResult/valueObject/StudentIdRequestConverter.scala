package dto.request.examResult.valueObject

import domain.examResult.valueObject.StudentId
import scala.util.{Try, Success, Failure}

object StudentIdRequestConverter {
  def validateAndCreate(value: String): Either[String, StudentId] = {
    if (value.isEmpty) {
      Left("StudentId cannot be empty")
    } else {
      Try(value.toInt) match {
        case Success(valueInt) => Right(StudentId(valueInt))
        case Failure(_)        => Left("Invalid studentId format")
      }
    }
  }
}
