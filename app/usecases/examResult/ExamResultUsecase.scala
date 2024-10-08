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
import utils.SystemClock
import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import java.time.{ZonedDateTime, DayOfWeek, LocalDate}
import java.time.temporal.{ChronoUnit, TemporalAdjusters}
import cats.data.EitherT
import cats.implicits._
import scala.concurrent.Future

@Singleton
class ExamResultUsecase @Inject() (
    examResultRepository: ExamResultRepository,
    examRepository: ExamRepository,
    evaluator: Evaluator,
    evaluationPeriodProvider: EvaluationPeriodProvider,
    examResultUpdater: ExamResultUpdater,
    examUpdater: ExamUpdater,
    ulidGenerator: UlidGenerator,
    systemClock: SystemClock
)(implicit ec: ExecutionContext) {

  def saveExamResult(
      examId: ExamId,
      subject: Subject,
      score: Score,
      studentId: StudentId
  ): Future[Either[String, ExamResult]] = {
    (for {
      examResultId <- EitherT.rightT[Future, String](ulidGenerator.generate())
      examResult = ExamResult(
        ExamResultId.create(examResultId),
        examId,
        score,
        studentId,
        Evaluation.NotEvaluated,
        CreatedAt(systemClock.now()),
        UpdatedAt(systemClock.now())
      )
      savedExamResult <- EitherT(examResultRepository.save(examResult))
    } yield savedExamResult).value
  }

  def findById(
      examResultId: ExamResultId
  ): Future[Either[String, Option[ExamResult]]] = {
    examResultRepository.findById(examResultId)
  }

  def evaluateResults(): Future[Either[String, Unit]] = {
    (for {
      (startDate, endDate) <- EitherT.pure[Future, String](
        evaluationPeriodProvider.getEvaluationPeriod(systemClock.now())
      )
      exams <- EitherT(examRepository.findByDueDate(startDate, endDate))
      examEvaluations <- exams.traverse { exam =>
        for {
          results <- EitherT(examResultRepository.findByExamId(exam.examId))
          evaluations = evaluator.evaluate(exam, results)
        } yield (exam, results, evaluations)
      }
      _ <- examEvaluations.traverse_ { case (exam, results, evaluations) =>
        for {
          updatedResults <- EitherT(
            examResultUpdater.updateEvaluations(
              results,
              evaluations,
              systemClock.now()
            )
          )
          _ <- EitherT(
            examUpdater.updateEvaluations(exam, updatedResults, results)
          )
        } yield ()
      }
    } yield ()).value
  }
}
