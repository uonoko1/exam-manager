package usecases.examResult.logic.examResultUpdater.`trait`

import domain.examResult.entity.ExamResult
import domain.examResult.valueObject.Evaluation
import scala.concurrent.Future

trait ExamResultUpdater {
  def updateEvaluations(
      results: Seq[ExamResult],
      evaluations: Map[ExamResult, Evaluation]
  ): Future[Either[String, Seq[ExamResult]]]
}
