package domain.examResult.valueObject

case class StudentId(value: String) extends AnyVal

object StudentId {
  def create(value: String): StudentId = StudentId(value)
}
