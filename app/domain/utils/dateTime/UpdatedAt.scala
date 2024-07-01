package domain.utils.dateTime

import java.time.ZonedDateTime

case class UpdatedAt(value: ZonedDateTime) extends AnyVal

object UpdatedAt {
  def create(value: ZonedDateTime): UpdatedAt = UpdatedAt(value)
}
