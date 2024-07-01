package domain.exam.valueObject

import java.time.ZonedDateTime

case class DueDate(value: ZonedDateTime) extends AnyVal

object DueDate {
  def create(value: ZonedDateTime): DueDate = DueDate(value)
}
