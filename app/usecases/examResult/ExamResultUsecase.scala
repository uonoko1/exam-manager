package usecases.examResult

import domain.examResult.entity.ExamResult
import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.evaluator.`trait`.Evaluator
import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import domain.utils.dateTime.{CreatedAt, UpdatedAt}
import usecases.examResult.repository.ExamResultRepository
import usecases.examResult.logic.examResultUpdater.`trait`.ExamResultUpdater
import usecases.exam.logic.examUpdater.`trait`.ExamUpdater
import usecases.exam.repository.ExamRepository
import utils.UlidGenerator
import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import java.time.{ZonedDateTime, DayOfWeek, LocalDate}
import java.time.temporal.{ChronoUnit, TemporalAdjusters}

@Singleton
class ExamResultUsecase @Inject() (
    examResultRepository: ExamResultRepository,
    examRepository: ExamRepository,
    evaluator: Evaluator,
    evaluationPeriodProvider: EvaluationPeriodProvider,
    examResultUpdater: ExamResultUpdater,
    examUpdater: ExamUpdater,
    ulidGenerator: UlidGenerator
)(implicit ec: ExecutionContext) {

  def saveExamResult(
      examId: ExamId,
      subject: Subject,
      score: Score,
      studentId: StudentId
  ): Future[Either[String, ExamResult]] = {
    val examResultId = ulidGenerator.generate()
    val examResult = ExamResult(
      ExamResultId.create(examResultId),
      examId,
      score,
      studentId,
      Evaluation.NotEvaluated,
      CreatedAt(ZonedDateTime.now()),
      UpdatedAt(ZonedDateTime.now())
    )

    examResultRepository.save(examResult)
  }

  def findById(
      examId: ExamId
  ): Future[Either[String, Option[ExamResult]]] = {
    examResultRepository.findById(examId)
  }

  def evaluateResults: Future[Unit] = {
    val (startDate, endDate) = evaluationPeriodProvider.getEvaluationPeriod

    examRepository.findByDueDate(startDate, endDate).flatMap {
      case Right(exams) =>
        val examEvaluationsFutures = exams.map { exam =>
          examResultRepository.findByExamId(exam.examId).map {
            case Right(results) =>
              val evaluations = evaluator.evaluate(exam, results)
              (exam, results, evaluations)
            case Left(error) => throw new Exception(error)
          }
        }

        Future.sequence(examEvaluationsFutures).flatMap { examEvaluations =>
          val updateFutures =
            examEvaluations.map { case (exam, results, evaluations) =>
              examResultUpdater
                .updateEvaluations(results, evaluations)
                .flatMap {
                  case Right(updatedResults) =>
                    examUpdater.updateEvaluations(exam, updatedResults, results)
                  case Left(error) => throw new Exception(error)
                }
            }
          Future.sequence(updateFutures).map(_ => ())
        }
      case Left(error) => Future.failed(new Exception(error))
    }
  }
}
