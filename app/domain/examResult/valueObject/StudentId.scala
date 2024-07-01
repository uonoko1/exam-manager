package domain.examResult.valueObject

case class StudentId(value: Int) extends AnyVal

object StudentId {
  def create(value: Int): StudentId = StudentId(value)
}
