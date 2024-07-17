package controllers

import play.api.test.{FakeRequest, Injecting}
import play.api.test.Helpers._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JsValue
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatest.matchers.must.Matchers._
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

import usecases.examResult.ExamResultUsecase
import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.utils.dateTime.{CreatedAt, UpdatedAt}
import domain.examResult.entity.ExamResult
import dto.response.examResult.entity.ExamResultResponseDto

import play.api.libs.json.Json
import scala.concurrent.Future
import java.time.ZonedDateTime
import dto.request.examResult.jsonParser.ExamResultFieldConverter
import dto.request.exam.valueObject.ExamIdRequestConverter

class ExamResultControllerSpec
    extends PlaySpec
    with GuiceOneAppPerSuite
    with Injecting
    with ScalaFutures {

  implicit val defaultPatience: PatienceConfig = PatienceConfig(
    timeout = Span(300, Seconds),
    interval = Span(500, Millis)
  )

  val mockUsecase: ExamResultUsecase = mock(classOf[ExamResultUsecase])
  val mockExamResultFieldConverter: ExamResultFieldConverter = mock(
    classOf[ExamResultFieldConverter]
  )
  val mockExamIdRequestConverter: ExamIdRequestConverter = mock(
    classOf[ExamIdRequestConverter]
  )

  override def fakeApplication() = new GuiceApplicationBuilder()
    .overrides(
      bind[ExamResultUsecase].toInstance(mockUsecase),
      bind[ExamResultFieldConverter].toInstance(mockExamResultFieldConverter),
      bind[ExamIdRequestConverter].toInstance(mockExamIdRequestConverter)
    )
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
        "score" -> "85",
        "studentId" -> "student-id"
      )

      when(mockExamResultFieldConverter.convertAndValidate(any[JsValue]))
        .thenReturn(
          Right(
            (
              ExamId("exam-id"),
              Subject.Math,
              Score(85),
              StudentId("student-id")
            )
          )
        )

      val request = FakeRequest(POST, "/exam-result")
        .withJsonBody(jsonBody)
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe OK
        contentAsJson(result) mustBe Json.toJson(
          ExamResultResponseDto.fromDomain(examResult)
        )
      }
    }

    "return BadRequest for invalid parameters" in {
      val jsonBody = Json.obj(
        "examId" -> "exam-id",
        "subject" -> "Math",
        "score" -> "invalid",
        "studentId" -> "student-id"
      )

      when(mockExamResultFieldConverter.convertAndValidate(any[JsValue]))
        .thenReturn(Left("Invalid parameters"))

      val request = FakeRequest(POST, "/exam-result")
        .withJsonBody(jsonBody)
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe BAD_REQUEST
      }
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

      when(mockExamIdRequestConverter.validateAndCreate(any[String]))
        .thenReturn(Right(ExamId("exam-id")))

      val request = FakeRequest(GET, "/exam-result/exam-id")
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe OK
        contentAsJson(result) mustBe Json.toJson(
          ExamResultResponseDto.fromDomain(examResult)
        )
      }
    }

    "return NotFound for non-existing exam result" in {
      when(mockUsecase.findById(any[ExamId]))
        .thenReturn(Future.successful(Right(None)))

      when(mockExamIdRequestConverter.validateAndCreate(any[String]))
        .thenReturn(Right(ExamId("exam-id")))

      val request = FakeRequest(GET, "/exam-result/exam-id")
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe NOT_FOUND
      }
    }

    "return BadRequest for invalid exam ID" in {
      val invalidExamId = "invalid-ulid"
      when(mockExamIdRequestConverter.validateAndCreate(any[String]))
        .thenReturn(Left("Invalid exam ID"))

      val request = FakeRequest(GET, s"/exam-result/$invalidExamId")
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe BAD_REQUEST
      }
    }
  }
}
