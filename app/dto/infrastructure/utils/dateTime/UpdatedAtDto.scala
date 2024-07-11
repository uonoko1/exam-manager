package dto.infrastructure.utils.dateTime

import domain.utils.dateTime._
import java.time.ZonedDateTime

case class UpdatedAtDto(value: ZonedDateTime)

object UpdatedAtDto {
  def fromDomain(updatedAt: UpdatedAt): UpdatedAtDto = UpdatedAtDto(
    updatedAt.value
  )
  def toDomain(dto: UpdatedAtDto): UpdatedAt = UpdatedAt.create(dto.value)
}
