package dto.infrastructure.utils.dateTime

import domain.utils.dateTime._
import java.time.ZonedDateTime

case class UpdatedAtAtDto(value: ZonedDateTime)

object UpdatedAtDto {
  def fromDomain(updatedAt: UpdatedAt): UpdatedAtDt = UpdatedAtDto(
    updatedAt.value
  )
  def toDomain(dto: UpdatedAtDto): UpdatedAt = UpdatedAt.create(dto.value)
}
