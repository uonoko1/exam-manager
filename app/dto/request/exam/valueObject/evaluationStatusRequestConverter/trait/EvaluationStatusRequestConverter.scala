package dto.request.exam.valueObject.evaluationStatusRequestConverter.`trait`

import domain.exam.valueObject.EvaluationStatus

trait EvaluationStatusRequestConverter {
  def validateAndCreate(value: String): Either[String, EvaluationStatus]
}
