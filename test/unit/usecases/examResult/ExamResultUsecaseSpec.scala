package usecases.examResult

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

import domain.examResult.entity.ExamResult
import domain.exam.valueObject.ExamId
import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.evaluator.`trait`.Evaluator
import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import domain.utils.dateTime.{CreatedAt, UpdatedAt}
import domain.exam.entity.Exam
import usecases.examResult.repository.ExamResultRepository
import usecases.examResult.logic.examResultUpdater.`trait`.ExamResultUpdater
import usecases.exam.logic.examUpdater.`trait`.ExamUpdater
import usecases.exam.repository.ExamRepository
import utils.{UlidGenerator, SystemClock}
import utils.CustomPatience

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.time.ZonedDateTime

class ExamResultUsecaseSpec
    extends AnyWordSpec
    with ScalaFutures
    with Matchers
    with CustomPatience {
  val mockExamResultRepository = mock(classOf[ExamResultRepository])
  val mockExamRepository = mock(classOf[ExamRepository])
  val mockEvaluator = mock(classOf[Evaluator])
  val mockEvaluationPeriodProvider = mock(classOf[EvaluationPeriodProvider])
  val mockExamResultUpdater = mock(classOf[ExamResultUpdater])
  val mockExamUpdater = mock(classOf[ExamUpdater])
  val mockUlidGenerator = mock(classOf[UlidGenerator])
  val mockSystemClock = mock(classOf[SystemClock])

  val usecase = new ExamResultUsecase(
    mockExamResultRepository,
    mockExamRepository,
    mockEvaluator,
    mockEvaluationPeriodProvider,
    mockExamResultUpdater,
    mockExamUpdater,
    mockUlidGenerator,
    mockSystemClock
  )

  "ExamResultUsecase#saveExamResult" should {
    "return the saved ExamResult when given valid input" in {
      val examId = ExamId("exam-id")
      val subject = Subject.Math
      val score = Score(85)
      val studentId = StudentId("student-id")
      val examResultId = "01ARZ3NDEKTSV4RRFFQ69G5FAV"
      val fixedTime =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")
      val examResult = ExamResult(
        ExamResultId(examResultId),
        examId,
        score,
        studentId,
        Evaluation.NotEvaluated,
        CreatedAt(fixedTime),
        UpdatedAt(fixedTime)
      )

      when(mockUlidGenerator.generate()).thenReturn(examResultId)
      when(mockSystemClock.now()).thenReturn(fixedTime)
      when(mockExamResultRepository.save(any[ExamResult]))
        .thenReturn(Future.successful(Right(examResult)))

      val result = usecase.saveExamResult(examId, subject, score, studentId)

      whenReady(result) { res =>
        res mustBe Right(examResult)
        verify(mockExamResultRepository).save(any[ExamResult])
      }
    }

    "handle repository save failure" in {
      val examId = ExamId("exam-id")
      val subject = Subject.Math
      val score = Score(85)
      val studentId = StudentId("student-id")
      val examResultId = "01ARZ3NDEKTSV4RRFFQ69G5FAV"
      val fixedTime =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")

      when(mockUlidGenerator.generate()).thenReturn(examResultId)
      when(mockSystemClock.now()).thenReturn(fixedTime)
      when(mockExamResultRepository.save(any[ExamResult]))
        .thenReturn(Future.successful(Left("Database error")))

      val result = usecase.saveExamResult(examId, subject, score, studentId)

      whenReady(result) { res =>
        res mustBe Left("Database error")
      }
    }
  }

  "ExamResultUsecase#findById" should {
    "return the ExamResult when given existing ExamId" in {
      val examResultId = ExamResultId("examResult-id")
      val examResult = Some(
        ExamResult(
          ExamResultId("01ARZ3NDEKTSV4RRFFQ69G5FAV"),
          ExamId("exam-id"),
          Score(85),
          StudentId("student-id"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        )
      )

      when(mockExamResultRepository.findById(examResultId))
        .thenReturn(Future.successful(Right(examResult)))

      val result = usecase.findById(examResultId)

      whenReady(result) { res =>
        res mustBe Right(examResult)
        verify(mockExamResultRepository).findById(examResultId)
      }
    }

    "handle repository findById failure" in {
      val examResultId = ExamResultId("exam-id")

      when(mockExamResultRepository.findById(examResultId))
        .thenReturn(Future.successful(Left("Database error")))

      val result = usecase.findById(examResultId)

      whenReady(result) { res =>
        res mustBe Left("Database error")
      }
    }
  }

  "ExamResultUsecase#evaluateResults" should {
    "evaluate examResults successfully" in {
      val startDate = ZonedDateTime.now().minusDays(7)
      val endDate = ZonedDateTime.now()
      val fixedTime =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")
      val exams = Seq(
        Exam(
          ExamId("exam-id"),
          Subject.Math,
          DueDate(ZonedDateTime.now().minusDays(1)),
          EvaluationStatus.NotEvaluated,
          CreatedAt(ZonedDateTime.now().minusDays(10)),
          UpdatedAt(ZonedDateTime.now().minusDays(1))
        )
      )
      val examResults = Seq(
        ExamResult(
          ExamResultId("01ARZ3NDEKTSV4RRFFQ69G5FAV"),
          ExamId("exam-id"),
          Score(85),
          StudentId("student-id"),
          Evaluation.NotEvaluated,
          CreatedAt(fixedTime),
          UpdatedAt(fixedTime)
        )
      )
      val evaluations = Map(examResults.head -> Evaluation.Excellent)

      when(mockEvaluationPeriodProvider.getEvaluationPeriod(any[ZonedDateTime]))
        .thenReturn((startDate, endDate))
      when(mockExamRepository.findByDueDate(startDate, endDate))
        .thenReturn(Future.successful(Right(exams)))
      when(mockExamResultRepository.findByExamId(any[ExamId]))
        .thenReturn(Future.successful(Right(examResults)))
      when(mockEvaluator.evaluate(any[Exam], any[Seq[ExamResult]]))
        .thenReturn(evaluations)
      when(
        mockExamResultUpdater.updateEvaluations(
          any[Seq[ExamResult]],
          any[Map[ExamResult, Evaluation]],
          any[ZonedDateTime]
        )
      )
        .thenReturn(Future.successful(Right(examResults)))
      when(
        mockExamUpdater.updateEvaluations(
          any[Exam],
          any[Seq[ExamResult]],
          any[Seq[ExamResult]]
        )
      )
        .thenReturn(Future.successful(Right(exams.head)))

      val result = usecase.evaluateResults()

      whenReady(result) { res =>
        res mustBe Right(())
      }
    }

    "handle evaluation failure" in {
      val startDate = ZonedDateTime.now().minusDays(7)
      val endDate = ZonedDateTime.now()

      when(mockEvaluationPeriodProvider.getEvaluationPeriod(any[ZonedDateTime]))
        .thenReturn((startDate, endDate))
      when(mockExamRepository.findByDueDate(startDate, endDate))
        .thenReturn(Future.successful(Left("Database error")))

      val result = usecase.evaluateResults()

      whenReady(result) { res =>
        res mustBe Left("Database error")
      }
    }
  }
}
