package controllers

import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import usecases.examResult.ExamResultUsecase
import scala.concurrent.Future
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import domain.examResult.valueObject._
import domain.exam.valueObject._
import play.api.libs.json.Json
import dto.response.examResult.entity.ExamResultResponseDto
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import domain.utils.dateTime.CreatedAt
import domain.utils.dateTime.UpdatedAt
import java.time.ZonedDateTime
import domain.examResult.entity.ExamResult

class ExamResultControllerSpec
    extends PlaySpec
    with GuiceOneAppPerSuite
    with Injecting {
  val mockUsecase: ExamResultUsecase = mock(classOf[ExamResultUsecase])

  override def fakeApplication() = new GuiceApplicationBuilder()
    .overrides(bind[ExamResultUsecase].toInstance(mockUsecase))
    .build()

  "ExamResultController POST /exam-result" should {
    "return OK for valid parameters" in {
      val examResult = ExamResult(
        ExamResultId.create("test-id"),
        ExamId.create("exam-id"),
        Score(85),
        StudentId.create("student-id"),
        Evaluation.Passed,
        CreatedAt(ZonedDateTime.now()),
        UpdatedAt(ZonedDateTime.now())
      )

      when(
        mockUsecase.saveExamResult(
          any[ExamId],
          any[Subject],
          any[Score],
          any[StudentId]
        )
      )
        .thenReturn(Future.successful(Right(examResult)))

      val jsonBody = Json.obj(
        "examId" -> "exam-id",
        "subject" -> "Math",
        "score" -> 85,
        "studentId" -> "student-id"
      )

      val request = FakeRequest(POST, "/exam-result")
        .withJsonBody(jsonBody)
      val result = route(app, request).get

      status(result) mustBe OK
      contentAsJson(result) mustBe Json.toJson(
        ExamResultResponseDto.fromDomain(examResult)
      )
    }

    "return BadRequest for invalid parameters" in {
      val jsonBody = Json.obj(
        "examId" -> "exam-id",
        "subject" -> "Math",
        "score" -> "invalid",
        "studentId" -> "student-id"
      )

      val request = FakeRequest(POST, "/exam-result")
        .withJsonBody(jsonBody)
      val result = route(app, request).get

      status(result) mustBe BAD_REQUEST
    }
  }

  "ExamResultController GET /exam-result/:examId" should {
    "return OK for existing exam result" in {
      val examResult = ExamResult(
        ExamResultId.create("test-id"),
        ExamId.create("exam-id"),
        Score(85),
        StudentId.create("student-id"),
        Evaluation.Passed,
        CreatedAt(ZonedDateTime.now()),
        UpdatedAt(ZonedDateTime.now())
      )

      when(mockUsecase.findById(any[ExamId]))
        .thenReturn(Future.successful(Right(Some(examResult))))

      val request = FakeRequest(GET, "/exam-result/exam-id")
      val result = route(app, request).get

      status(result) mustBe OK
      contentAsJson(result) mustBe Json.toJson(
        ExamResultResponseDto.fromDomain(examResult)
      )
    }

    "return NotFound for non-existing exam result" in {
      when(mockUsecase.findById(any[ExamId]))
        .thenReturn(Future.successful(Right(None)))

      val request = FakeRequest(GET, "/exam-result/exam-id")
      val result = route(app, request).get

      status(result) mustBe NOT_FOUND
    }

    "return BadRequest for invalid exam ID" in {
      val request = FakeRequest(GET, "/exam-result/invalid-exam-id")
      val result = route(app, request).get

      status(result) mustBe BAD_REQUEST
    }
  }
}
