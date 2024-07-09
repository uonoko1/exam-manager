package dto.request.exam.valueObject

import domain.exam.valueObject.EvaluationStatus

object EvaluationStatusRequestConverter {

  def validateAndCreate(value: String): Either[String, EvaluationStatus] = {
    if (value.isEmpty) Left("EvaluationStatus cannot be empty")
    else EvaluationStatus.create(value)
  }
}
