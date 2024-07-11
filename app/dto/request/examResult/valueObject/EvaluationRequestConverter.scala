package dto.request.examResult.valueObject

import domain.examResult.valueObject.Evaluation

object EvaluationRequestConverter {
  def validateAndCreate(value: String): Either[String, Evaluation] = {
    if (value.isEmpty) Left("Evaluation cannot be empty")
    else Evaluation.create(value)
  }
}
