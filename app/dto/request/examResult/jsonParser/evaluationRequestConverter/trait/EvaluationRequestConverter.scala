package dto.request.examResult.jsonParser.evaluationRequestConverter.`trait`

import domain.examResult.valueObject.Evaluation

trait EvaluationRequestConverter {
  def validateAndCreate(value: String): Either[String, Evaluation]
}
