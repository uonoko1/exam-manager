package usecases.examResult.logic.examResultUpdater.impl

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import domain.examResult.entity.ExamResult
import domain.examResult.valueObject._
import usecases.examResult.repository.ExamResultRepository
import usecases.examResult.logic.examResultUpdater.`trait`.ExamResultUpdater
import java.time.ZonedDateTime
import domain.utils.dateTime.UpdatedAt

class ExamResultUpdaterImpl @Inject() (
    examResultRepository: ExamResultRepository
)(implicit ec: ExecutionContext)
    extends ExamResultUpdater {

  override def updateEvaluations(
      results: Seq[ExamResult],
      evaluations: Map[ExamResult, Evaluation],
      now: ZonedDateTime
  ): Future[Either[String, Seq[ExamResult]]] = {
    val updatedResults = results.map { result =>
      evaluations.get(result) match {
        case Some(evaluation) =>
          result.copy(
            evaluation = evaluation,
            updatedAt = UpdatedAt(now)
          )
        case None => throw new Exception("Evaluation not found for result")
      }
    }
    Future
      .sequence(updatedResults.map(examResultRepository.update))
      .map { resultSeq =>
        val errors = resultSeq.collect { case Left(error) => error }
        if (errors.nonEmpty) Left(errors.mkString(", "))
        else Right(updatedResults)
      }
      .recover { case ex => Left(ex.getMessage) }
  }
}
