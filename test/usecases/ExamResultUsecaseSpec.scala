package usecases

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import domain.examResult.entity.ExamResult
import domain.exam.valueObject.ExamId
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
import java.time.ZonedDateTime
import usecases.examResult.ExamResultUsecase
import domain.exam.entity.Exam

class ExamResultUsecaseSpec
    extends AnyWordSpec
    with ScalaFutures
    with Matchers {
  val mockExamResultRepository = mock(classOf[ExamResultRepository])
  val mockExamRepository = mock(classOf[ExamRepository])
  val mockEvaluator = mock(classOf[Evaluator])
  val mockEvaluationPeriodProvider = mock(classOf[EvaluationPeriodProvider])
  val mockExamResultUpdater = mock(classOf[ExamResultUpdater])
  val mockExamUpdater = mock(classOf[ExamUpdater])
  val mockUlidGenerator = mock(classOf[UlidGenerator])

  val usecase = new ExamResultUsecase(
    mockExamResultRepository,
    mockExamRepository,
    mockEvaluator,
    mockEvaluationPeriodProvider,
    mockExamResultUpdater,
    mockExamUpdater,
    mockUlidGenerator
  )

  "ExamResultUsecase#saveExamResult" should {
    "save the ExamResult successfully" in {
      val examId = ExamId("exam-id")
      val subject = Subject.Math
      val score = Score(85)
      val studentId = StudentId("student-id")
      val examResultId = "01ARZ3NDEKTSV4RRFFQ69G5FAV"
      val examResult = ExamResult(
        ExamResultId(examResultId),
        examId,
        score,
        studentId,
        Evaluation.NotEvaluated,
        CreatedAt(ZonedDateTime.now()),
        UpdatedAt(ZonedDateTime.now())
      )

      when(mockUlidGenerator.generate()).thenReturn(examResultId)
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

      when(mockUlidGenerator.generate()).thenReturn(examResultId)
      when(mockExamResultRepository.save(any[ExamResult]))
        .thenReturn(Future.successful(Left("Database error")))

      val result = usecase.saveExamResult(examId, subject, score, studentId)

      whenReady(result) { res =>
        res mustBe Left("Database error")
      }
    }
  }

  "ExamResultUsecase#findById" should {
    "find the exam result by ID successfully" in {
      val examId = ExamId("exam-id")
      val examResult = Some(
        ExamResult(
          ExamResultId("01ARZ3NDEKTSV4RRFFQ69G5FAV"),
          examId,
          Score(85),
          StudentId("student-id"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        )
      )

      when(mockExamResultRepository.findById(examId))
        .thenReturn(Future.successful(Right(examResult)))

      val result = usecase.findById(examId)

      whenReady(result) { res =>
        res mustBe Right(examResult)
        verify(mockExamResultRepository).findById(examId)
      }
    }

    "handle repository findById failure" in {
      val examId = ExamId("exam-id")

      when(mockExamResultRepository.findById(examId))
        .thenReturn(Future.successful(Left("Database error")))

      val result = usecase.findById(examId)

      whenReady(result) { res =>
        res mustBe Left("Database error")
      }
    }
  }

  "ExamResultUsecase#evaluateResults" should {
    "evaluate exam results successfully" in {
      val startDate = ZonedDateTime.now().minusDays(7)
      val endDate = ZonedDateTime.now()
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
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        )
      )
      val evaluations = Map(examResults.head -> Evaluation.Excellent)

      when(mockEvaluationPeriodProvider.getEvaluationPeriod)
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
          any[Map[ExamResult, Evaluation]]
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

      val result = usecase.evaluateResults

      whenReady(result) { res =>
        res mustBe Right(())
      }
    }

    "handle evaluation failure" in {
      val startDate = ZonedDateTime.now().minusDays(7)
      val endDate = ZonedDateTime.now()

      when(mockEvaluationPeriodProvider.getEvaluationPeriod)
        .thenReturn((startDate, endDate))
      when(mockExamRepository.findByDueDate(startDate, endDate))
        .thenReturn(Future.successful(Left("Database error")))

      val result = usecase.evaluateResults

      whenReady(result) { res =>
        res mustBe Left("Database error")
      }
    }
  }
}
