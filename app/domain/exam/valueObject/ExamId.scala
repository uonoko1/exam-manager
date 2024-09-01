package domain.exam.valueObject

case class ExamId(value: String) extends AnyVal

object ExamId {
  def create(value: String): Either[String, ExamId] =
    if (value.isEmpty) Left("ExamId cannot be empty")
    else Right(ExamId(value))

}
