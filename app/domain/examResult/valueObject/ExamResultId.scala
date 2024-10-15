package domain.examResult.valueObject

case class ExamResultId(value: String) extends AnyVal

object ExamResultId {
  def create(value: String): Either[String, ExamResultId] =
    if (value.isEmpty) Left("ExamResultId cannot be empty")
    else Right(ExamResultId(value))
}
