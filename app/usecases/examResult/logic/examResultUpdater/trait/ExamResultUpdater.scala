package usecases.examResult.logic.examResultUpdater.`trait`

import domain.examResult.entity.ExamResult
import domain.examResult.valueObject.Evaluation
import scala.concurrent.Future
import java.time.ZonedDateTime

trait ExamResultUpdater {
  def updateEvaluations(
      results: Seq[ExamResult],
      evaluations: Map[ExamResult, Evaluation],
      now: ZonedDateTime
  ): Future[Either[String, Seq[ExamResult]]]
}
