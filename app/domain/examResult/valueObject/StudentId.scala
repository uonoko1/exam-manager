package domain.examResult.valueObject

case class StudentId(value: String) extends AnyVal

object StudentId {
  def create(value: String): Either[String, StudentId] =
    if (value.isEmpty) Left("StudentId cannot be empty")
    else Right(StudentId(value))
}
