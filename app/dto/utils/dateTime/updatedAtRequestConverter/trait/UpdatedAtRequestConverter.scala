package dto.utils.dateTime.updatedAtRequestConverter.`trait`

import domain.utils.dateTime.UpdatedAt

trait UpdatedAtRequestConverter {
  def validateAndCreate(value: String): Either[String, UpdatedAt]
}
