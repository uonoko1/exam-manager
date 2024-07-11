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
      evaluations: Map[ExamResult, String]
  ): Future[Either[String, Seq[ExamResult]]] = {
    val updatedResults = results.map { result =>
      evaluations(result) match {
        case evaluationString =>
          Evaluation.create(evaluationString) match {
            case Right(evaluation) =>
              result.copy(
                evaluation = evaluation,
                updatedAt = UpdatedAt(ZonedDateTime.now())
              )
            case Left(error) => throw new Exception(error)
          }
      }
    }
    Future
      .sequence(updatedResults.map(examResultRepository.update))
      .map(_ => Right(updatedResults))
      .recover { case ex => Left(ex.getMessage) }
  }
}
