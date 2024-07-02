package dto.infrastructure.utils.dateTime

import domain.utils.dateTime._
import java.time.ZonedDateTime

case class CreatedAtDto(value: ZonedDateTime)

object CreatedAtDto {
  def fromDomain(createdAt: CreateAt): CreatedAtDt = CreatedAtDto(
    createdAt.value
  )
  def toDomain(dto: CreatedAtDto): CreatedAt = CreatedAt.create(dto.value)
}
