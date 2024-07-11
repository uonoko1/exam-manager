package usecases.examResult.logic.examResultUpdater.`trait`

import domain.examResult.entity.ExamResult
import scala.concurrent.Future

trait ExamResultUpdater {
  def updateEvaluations(
      results: Seq[ExamResult],
      evaluations: Map[ExamResult, String]
  ): Future[Either[String, Seq[ExamResult]]]
}
