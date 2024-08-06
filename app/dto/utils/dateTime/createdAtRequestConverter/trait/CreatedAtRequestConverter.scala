package dto.utils.dateTime.createdAtRequestConverter.`trait`

import domain.utils.dateTime.CreatedAt

trait CreatedAtRequestConverter {
  def validateAndCreate(value: String): Either[String, CreatedAt]
}
