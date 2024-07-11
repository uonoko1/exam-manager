package domain.exam.valueObject

case class ExamId(value: String) extends AnyVal

object ExamId {
  def create(value: String): ExamId = ExamId(value)
}
