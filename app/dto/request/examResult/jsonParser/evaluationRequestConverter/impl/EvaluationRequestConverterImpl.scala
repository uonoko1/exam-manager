package dto.request.examResult.jsonParser.evaluationRequestConverter.impl

import domain.examResult.valueObject.Evaluation
import dto.request.examResult.jsonParser.evaluationRequestConverter.`trait`.EvaluationRequestConverter
import javax.inject._

@Singleton
class EvaluationRequestConverterImpl @Inject() ()
    extends EvaluationRequestConverter {
  override def validateAndCreate(value: String): Either[String, Evaluation] = {
    if (value.isEmpty) Left("Evaluation cannot be empty")
    else Evaluation.create(value)
  }
}
