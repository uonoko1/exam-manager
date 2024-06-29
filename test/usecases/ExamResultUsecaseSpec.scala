package usecases

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import usecases.repository.ExamResultRepository
import utils.UlidGenerator
import domain.ExamResult
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ExamResultUsecaseSpec extends AnyWordSpec with ScalaFutures {
  val mockRepository = mock[ExamResultRepository]
  val mockUlidGenerator = mock[UlidGenerator]

  val usecase = new ExamResultUsecase(mockRepository, mockUlidGenerator)

  "ExamResultUsecase#saveExamResult" should {
    "save the exam result and return evaluation" in {
      val examId = "01F8MECHZX3TBDSZ7XRADM79XE"
      val subject = "Math"
      val score = 85
      val studentId = 1

      when(mockUlidGenerator.generate()).thenReturn(examId)
      when(mockRepository.save(any[ExamResult]))
        .thenReturn(Future.successful(()))

      val result = usecase.saveExamResult(subject, score, studentId)

      whenReady(result) { res =>
        res mustBe "Good Job!"
        verify(mockRepository).save(
          ExamResult(examId, subject, score, studentId)
        )
      }
    }

    "handle repository save failure" in {
      val examId = "01F8MECHZX3TBDSZ7XRADM79XE"
      val subject = "Math"
      val score = 85
      val studentId = 1

      when(mockUlidGenerator.generate()).thenReturn(examId)
      when(mockRepository.save(any[ExamResult]))
        .thenReturn(Future.failure(new Exception("Database error")))

      val result = usecase.saveExamResult(subject, score, studentId)

      whenReady(result.failed) { ex =>
        ex mustBe an[Exeption]
        ex.getMessage must include("Database error")
      }
    }

    "return Failed for a score below 50" in {
      val examId = "01F8MECHZX3TBDSZ7XRADM79XE"
      val subject = "Math"
      val score = 45
      val studentId = 1

      when(mockUlidGenerator.generate()).thenReturn(examId)
      when(mockRepository.save(any[ExamResult]))
        .thenReturn(Future.successful(()))

      val result = usecase.saveExamResult(subject, score, studentId)

      whenReady(result) { res =>
        res mustBe "Failed"
        verify(mockRepository).save(
          ExamResult(examId, subject, score, studentId)
        )
      }
    }
  }
}
