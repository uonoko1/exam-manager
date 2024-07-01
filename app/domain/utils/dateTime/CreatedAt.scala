package domain.utils.dateTime

case class CreatedAt(value: ZonedDateTime) extends AnyVal

object CreatedAt {
  def create(value: ZonedDateTime): CreatedAt = CreatedAt(value)
}
