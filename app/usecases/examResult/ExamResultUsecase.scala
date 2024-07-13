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
    (for {
      (startDate, endDate) <- EitherT.pure[Future, String](
        evaluationPeriodProvider.getEvaluationPeriod
      )
      exams <- EitherT(examRepository.findByDueDate(startDate, endDate))
      examEvaluations <- EitherT
        .liftF(Future.sequence {
          exams.map { exam =>
            (for {
              results <- EitherT(examResultRepository.findByExamId(exam.examId))
              evaluations = evaluator.evaluate(exam, results)
            } yield (exam, results, evaluations)).value
          }
        })
        .map(_.collect { case Right(value) => value })
      _ <- EitherT
        .liftF(Future.sequence {
          examEvaluations.map { case (exam, results, evaluations) =>
            (for {
              updatedResults <- EitherT(
                examResultUpdater.updateEvaluations(results, evaluations)
              )
              _ <- EitherT.liftF(
                examUpdater.updateEvaluations(exam, updatedResults, results)
              )
            } yield ()).value
          }
        })
        .map(_.collect { case Right(value) => value })
    } yield ()).value.map {
      case Right(_)    => ()
      case Left(error) => throw new Exception(error)
    }
  }
}
