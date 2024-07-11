package domain.utils.dateTime

import java.time.ZonedDateTime

case class CreatedAt(value: ZonedDateTime) extends AnyVal

object CreatedAt {
  def create(value: ZonedDateTime): CreatedAt = CreatedAt(value)
}
