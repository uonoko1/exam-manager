package dto.request.examResult.valueObject.evaluationRequestConverter.`trait`

import domain.examResult.valueObject.Evaluation

trait EvaluationRequestConverter {
  def validateAndCreate(value: String): Either[String, Evaluation]
}
