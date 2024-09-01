package dto.request.exam.valueObject.evaluationStatusRequestConverter.impl

import domain.exam.valueObject.EvaluationStatus
import dto.request.exam.valueObject.evaluationStatusRequestConverter.`trait`.EvaluationStatusRequestConverter
import javax.inject._

@Singleton
class EvaluationStatusRequestConverterImpl @Inject() ()
    extends EvaluationStatusRequestConverter {

  override def validateAndCreate(
      value: String
  ): Either[String, EvaluationStatus] = {
    if (value.isEmpty) Left("EvaluationStatus cannot be empty")
    else EvaluationStatus.create(value)
  }
}
