package usecases.examResult.logic.examResultUpdater.impl

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfterEach
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.mockito.ArgumentCaptor

import domain.examResult.entity.ExamResult
import domain.exam.valueObject._
import domain.examResult.valueObject._
import domain.utils.dateTime.UpdatedAt
import domain.utils.dateTime.CreatedAt
import usecases.examResult.repository.ExamResultRepository
import utils.CustomPatience

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.time.ZonedDateTime

class ExamResultUpdaterImplSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with BeforeAndAfterEach
    with CustomPatience {
  val mockExamResultRepository = mock(classOf[ExamResultRepository])
  val updater = new ExamResultUpdaterImpl(mockExamResultRepository)

  override def beforeEach(): Unit =
    reset(mockExamResultRepository)

  "ExamResultUpdaterImpl#updateEvaluations" should {
    "update evaluations successfully when given valid input" in {
      val fixedTime =
        ZonedDateTime.parse("2024-07-26T01:41:09.005056400+09:00[Asia/Tokyo]")

      val results = Seq(
        ExamResult(
          ExamResultId("exam-result-id-1"),
          ExamId("exam-id"),
          Score(85),
          StudentId("student-id-1"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now().minusDays(10)),
          UpdatedAt(ZonedDateTime.now().minusDays(1))
        ),
        ExamResult(
          ExamResultId("exam-result-id-2"),
          ExamId("exam-id"),
          Score(90),
          StudentId("student-id-2"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now().minusDays(10)),
          UpdatedAt(ZonedDateTime.now().minusDays(1))
        )
      )
      val evaluations = Map(
        results(0) -> Evaluation.Excellent,
        results(1) -> Evaluation.GoodJob
      )

      val updatedResults = Seq(
        results(0).copy(
          evaluation = Evaluation.Excellent,
          updatedAt = UpdatedAt(fixedTime)
        ),
        results(1).copy(
          evaluation = Evaluation.GoodJob,
          updatedAt = UpdatedAt(fixedTime)
        )
      )

      when(mockExamResultRepository.update(any[ExamResult]))
        .thenAnswer { invocation =>
          val argument = invocation.getArgument(0).asInstanceOf[ExamResult]
          Future.successful(Right(argument))
        }

      val result = updater.updateEvaluations(results, evaluations, fixedTime)

      whenReady(result) { res =>
        res mustBe Right(updatedResults)
        val captor = ArgumentCaptor.forClass(classOf[ExamResult])
        verify(mockExamResultRepository, times(results.size))
          .update(captor.capture())

        val capturedResults = captor.getAllValues
        capturedResults must contain theSameElementsAs updatedResults
      }
    }

    "handle repository update failure" in {
      val fixedTime =
        ZonedDateTime.parse("2024-07-26T01:41:09.005056400+09:00[Asia/Tokyo]")

      val results = Seq(
        ExamResult(
          ExamResultId("exam-result-id-1"),
          ExamId("exam-id"),
          Score(85),
          StudentId("student-id-1"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now().minusDays(10)),
          UpdatedAt(ZonedDateTime.now().minusDays(1))
        )
      )
      val evaluations = Map(
        results(0) -> Evaluation.Excellent
      )

      when(mockExamResultRepository.update(any[ExamResult]))
        .thenReturn(Future.successful(Left("Database error")))

      val result = updater.updateEvaluations(results, evaluations, fixedTime)

      whenReady(result) { res =>
        res mustBe Left("Database error")
        verify(mockExamResultRepository, times(results.size))
          .update(any[ExamResult])
      }
    }

    "throw an exception when evaluation not found for result" in {
      val fixedTime =
        ZonedDateTime.parse("2024-07-26T01:41:09.005056400+09:00[Asia/Tokyo]")

      val results = Seq(
        ExamResult(
          ExamResultId("exam-result-id-1"),
          ExamId("exam-id"),
          Score(85),
          StudentId("student-id-1"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now().minusDays(10)),
          UpdatedAt(ZonedDateTime.now().minusDays(1))
        )
      )
      val evaluations = Map.empty[ExamResult, Evaluation]

      assertThrows[Exception] {
        updater.updateEvaluations(results, evaluations, fixedTime).futureValue
      }
    }
  }
}
