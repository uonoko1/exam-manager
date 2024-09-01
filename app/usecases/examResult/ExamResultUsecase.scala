package usecases.examResult

import domain.examResult.entity.ExamResult
import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.evaluator.`trait`.Evaluator
import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import domain.utils.dateTime.{ CreatedAt, UpdatedAt }
import usecases.examResult.repository.ExamResultRepository
import usecases.examResult.logic.examResultUpdater.`trait`.ExamResultUpdater
import usecases.exam.logic.examUpdater.`trait`.ExamUpdater
import usecases.exam.repository.ExamRepository
import dto.request.examResult.valueObject._
import dto.request.exam.valueObject._
import utils.UlidGenerator
import utils.SystemClock
import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import java.time.{ DayOfWeek, LocalDate, ZonedDateTime }
import java.time.temporal.{ ChronoUnit, TemporalAdjusters }
import cats.data.EitherT
import cats.implicits._
import scala.concurrent.Future
import dto.request.examResult.ToDomainConverter.ExamResultToDomainConverter

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
      examIdRequestDto: ExamIdRequestDto,
      subjectRequestDto: SubjectRequestDto,
      scoreRequestDto: ScoreRequestDto,
      studentIdRequestDto: StudentIdRequestDto
  ): Future[Either[String, ExamResult]] =
    (for {
      (examId, subject, score, studentId) <- EitherT.fromEither[Future](
        ExamResultToDomainConverter.convert[(ExamId, Subject, Score, StudentId)](
          Some(examIdRequestDto),
          Some(subjectRequestDto),
          Some(scoreRequestDto),
          Some(studentIdRequestDto)
        )
      )

      examResultIdStr <- EitherT.rightT[Future, String](
        ulidGenerator.generate()
      )

      examResultId <- EitherT.fromEither[Future](
        ExamResultId.create(examResultIdStr)
      )

      examResult = ExamResult(
        examResultId,
        examId,
        score,
        studentId,
        Evaluation.NotEvaluated,
        CreatedAt(systemClock.now()),
        UpdatedAt(systemClock.now())
      )
      savedExamResult <- EitherT(examResultRepository.save(examResult))
    } yield savedExamResult).value

  def findById(
      examResultIdRequestDto: ExamResultIdRequestDto
  ): Future[Either[String, Option[ExamResult]]] =
    (for {
      Tuple1(examResultId) <- EitherT.fromEither[Future](
        ExamResultToDomainConverter.convert[Tuple1[ExamResultId]](
          Some(examResultIdRequestDto)
        )
      )

      result <- EitherT(
        examResultRepository.findById(examResultId)
      )
    } yield result).value

  def evaluateResults(): Future[Either[String, Unit]] =
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
