package domain.examResult.valueObject

case class ExamResultId(value: String) extends AnyVal

object ExamResultId {
  def create(value: String): ExamResultId = ExamResultId(value)
}
