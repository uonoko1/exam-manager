package usecases.exam.logic.examUpdater.impl

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.scalatest.BeforeAndAfterEach

import domain.exam.entity.Exam
import domain.examResult.entity.ExamResult
import domain.exam.valueObject._
import domain.examResult.valueObject._
import domain.utils.dateTime.UpdatedAt
import domain.utils.dateTime.{CreatedAt, UpdatedAt}
import usecases.exam.repository.ExamRepository
import utils.CustomPatience
import utils.SystemClock

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.time.ZonedDateTime

class ExamUpdaterImplSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with BeforeAndAfterEach
    with CustomPatience {
  val mockExamRepository = mock(classOf[ExamRepository])
  val mockSystemClock = mock(classOf[SystemClock])
  val examUpdater = new ExamUpdaterImpl(mockExamRepository, mockSystemClock)
  when(mockSystemClock.now())
    .thenReturn(ZonedDateTime.parse("2024-07-20T12:00:00+09:00[Asia/Tokyo]"))

  override def beforeEach(): Unit = {
    reset(mockExamRepository)
  }

  "ExamUpdaterImpl#updateEvaluations" should {
    "update the exam status to Evaluated when all exam results are evaluated" in {
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(mockSystemClock.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(mockSystemClock.now().minusDays(10)),
        UpdatedAt(mockSystemClock.now().minusDays(1))
      )
      val examResults = Seq(
        ExamResult(
          ExamResultId("exam-result-id-1"),
          exam.examId,
          Score(85),
          StudentId("student-id-1"),
          Evaluation.Excellent,
          CreatedAt(mockSystemClock.now().minusDays(10)),
          UpdatedAt(mockSystemClock.now().minusDays(1))
        ),
        ExamResult(
          ExamResultId("exam-result-id-2"),
          exam.examId,
          Score(90),
          StudentId("student-id-2"),
          Evaluation.GoodJob,
          CreatedAt(mockSystemClock.now().minusDays(10)),
          UpdatedAt(mockSystemClock.now().minusDays(1))
        )
      )

      val updatedExam = exam.copy(
        evaluationStatus = EvaluationStatus.Evaluated,
        updatedAt = UpdatedAt(mockSystemClock.now())
      )

      when(mockExamRepository.update(any[Exam]))
        .thenReturn(Future.successful(Right(updatedExam)))

      val result = examUpdater.updateEvaluations(exam, examResults, examResults)

      whenReady(result) { res =>
        res mustBe Right(updatedExam)
        verify(mockExamRepository).update(any[Exam])
      }
    }

    "update the exam status to PartiallyEvaluated when some exam results are evaluated" in {
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(mockSystemClock.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(mockSystemClock.now().minusDays(10)),
        UpdatedAt(mockSystemClock.now().minusDays(1))
      )
      val updatedExamResults = Seq(
        ExamResult(
          ExamResultId("exam-result-id-1"),
          exam.examId,
          Score(85),
          StudentId("student-id-1"),
          Evaluation.Excellent,
          CreatedAt(mockSystemClock.now().minusDays(10)),
          UpdatedAt(mockSystemClock.now().minusDays(1))
        )
      )
      val examResults = Seq(
        updatedExamResults.head,
        ExamResult(
          ExamResultId("exam-result-id-2"),
          exam.examId,
          Score(90),
          StudentId("student-id-2"),
          Evaluation.NotEvaluated,
          CreatedAt(mockSystemClock.now().minusDays(10)),
          UpdatedAt(mockSystemClock.now().minusDays(1))
        )
      )

      val updatedExam = exam.copy(
        evaluationStatus = EvaluationStatus.PartiallyEvaluated,
        updatedAt = UpdatedAt(mockSystemClock.now())
      )

      when(mockExamRepository.update(any[Exam]))
        .thenReturn(Future.successful(Right(updatedExam)))

      val result =
        examUpdater.updateEvaluations(exam, updatedExamResults, examResults)

      whenReady(result) { res =>
        res mustBe Right(updatedExam)
        verify(mockExamRepository).update(any[Exam])
      }
    }

    "update the exam status to NotEvaluated when no exam results are evaluated" in {
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(mockSystemClock.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(mockSystemClock.now().minusDays(10)),
        UpdatedAt(mockSystemClock.now().minusDays(1))
      )
      val examResults = Seq(
        ExamResult(
          ExamResultId("exam-result-id-1"),
          exam.examId,
          Score(85),
          StudentId("student-id-1"),
          Evaluation.NotEvaluated,
          CreatedAt(mockSystemClock.now().minusDays(10)),
          UpdatedAt(mockSystemClock.now().minusDays(1))
        ),
        ExamResult(
          ExamResultId("exam-result-id-2"),
          exam.examId,
          Score(90),
          StudentId("student-id-2"),
          Evaluation.NotEvaluated,
          CreatedAt(mockSystemClock.now().minusDays(10)),
          UpdatedAt(mockSystemClock.now().minusDays(1))
        )
      )

      val updatedExam = exam.copy(
        evaluationStatus = EvaluationStatus.NotEvaluated,
        updatedAt = UpdatedAt(mockSystemClock.now())
      )

      when(mockExamRepository.update(any[Exam]))
        .thenReturn(Future.successful(Right(updatedExam)))

      val result = examUpdater.updateEvaluations(exam, Seq.empty, examResults)

      whenReady(result) { res =>
        res mustBe Right(updatedExam)
        verify(mockExamRepository).update(any[Exam])
      }
    }

    "handle repository update failure" in {
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(mockSystemClock.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(mockSystemClock.now().minusDays(10)),
        UpdatedAt(mockSystemClock.now().minusDays(1))
      )
      val updatedExamResults = Seq(
        ExamResult(
          ExamResultId("exam-result-id-1"),
          exam.examId,
          Score(85),
          StudentId("student-id-1"),
          Evaluation.Excellent,
          CreatedAt(mockSystemClock.now().minusDays(10)),
          UpdatedAt(mockSystemClock.now().minusDays(1))
        )
      )
      val examResults = Seq(
        updatedExamResults.head,
        ExamResult(
          ExamResultId("exam-result-id-2"),
          exam.examId,
          Score(90),
          StudentId("student-id-2"),
          Evaluation.NotEvaluated,
          CreatedAt(mockSystemClock.now().minusDays(10)),
          UpdatedAt(mockSystemClock.now().minusDays(1))
        )
      )

      when(mockExamRepository.update(any[Exam]))
        .thenReturn(Future.successful(Left("Database error")))

      val result =
        examUpdater.updateEvaluations(exam, updatedExamResults, examResults)

      whenReady(result) { res =>
        res mustBe Left("Database error")
        verify(mockExamRepository).update(any[Exam])
      }
    }
  }
}
