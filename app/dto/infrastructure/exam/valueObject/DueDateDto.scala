package dto.infrastructure.exam.valueObject

import java.time.ZonedDateTime
import domain.exam.valueObject._

case class DueDateDto(value: ZonedDateTime)

object DueDateDto {
  def fromDomain(dueDate: DueDate): DueDateDto = DueDateDto(dueDate.value)
  def toDomain(dto: DueDateDto): DueDate = DueDate.create(dto.value)
}
