package dto.request.examResult.valueObject

import domain.examResult.valueObject.StudentId
import scala.util.{Try, Success, Failure}

object StudentIdRequestConverter {

  def validateAndCreate(value: String): Either[String, StudentId] = {
    if (value.isEmpty) Left("StudentId cannot be empty")
    else Right(StudentId(value))
  }
}
