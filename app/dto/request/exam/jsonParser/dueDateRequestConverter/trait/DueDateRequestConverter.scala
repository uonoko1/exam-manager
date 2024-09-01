package dto.request.exam.valueObject.dueDateRequestConverter.`trait`

import domain.exam.valueObject.DueDate

trait DueDateRequestConverter {
  def validateAndCreate(value: String): Either[String, DueDate]
}
