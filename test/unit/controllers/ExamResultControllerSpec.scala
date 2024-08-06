package controllers

import play.api.test.{FakeRequest, Injecting}
import play.api.test.Helpers._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JsValue
import play.api.libs.json.Json

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatest.matchers.must.Matchers._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.utils.dateTime.{CreatedAt, UpdatedAt}
import domain.examResult.entity.ExamResult
import usecases.examResult.ExamResultUsecase
import dto.response.examResult.entity.ExamResultResponseDto
import dto.request.examResult.jsonParser.examResultFieldConverter.`trait`.ExamResultFieldConverter
import dto.request.examResult.valueObject.examResultIdRequestConverter.`trait`.ExamResultIdRequestConverter
import utils.CustomPatience

import scala.concurrent.Future
import java.time.ZonedDateTime

class ExamResultControllerSpec
    extends PlaySpec
    with GuiceOneAppPerSuite
    with Injecting
    with ScalaFutures
    with CustomPatience {

  val mockUsecase: ExamResultUsecase = mock(classOf[ExamResultUsecase])
  val mockExamResultFieldConverter: ExamResultFieldConverter = mock(
    classOf[ExamResultFieldConverter]
  )
  val mockExamResultIdRequestConverter: ExamResultIdRequestConverter = mock(
    classOf[ExamResultIdRequestConverter]
  )

  override def fakeApplication() = new GuiceApplicationBuilder()
    .overrides(
      bind[ExamResultUsecase].toInstance(mockUsecase),
      bind[ExamResultFieldConverter].toInstance(mockExamResultFieldConverter),
      bind[ExamResultIdRequestConverter]
        .toInstance(mockExamResultIdRequestConverter)
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

  "ExamResultController GET /exam-result/:examResultId" should {
    "return an ExamResult corresponding to the examResultId" in {
      val examResult = ExamResult(
        ExamResultId.create("test-id"),
        ExamId.create("exam-id"),
        Score(85),
        StudentId.create("student-id"),
        Evaluation.Passed,
        CreatedAt(ZonedDateTime.now()),
        UpdatedAt(ZonedDateTime.now())
      )

      when(mockUsecase.findById(any[ExamResultId]))
        .thenReturn(Future.successful(Right(Some(examResult))))

      when(mockExamResultIdRequestConverter.validateAndCreate(any[String]))
        .thenReturn(Right(ExamResultId("examResult-id")))

      val request = FakeRequest(GET, "/exam-result/examResult-id")
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe OK
        contentAsJson(result) mustBe Json.toJson(
          ExamResultResponseDto.fromDomain(examResult)
        )
      }
    }

    "return NotFound for non-existing examResult" in {
      when(mockUsecase.findById(any[ExamResultId]))
        .thenReturn(Future.successful(Right(None)))

      when(mockExamResultIdRequestConverter.validateAndCreate(any[String]))
        .thenReturn(Right(ExamResultId("examResult-id")))

      val request = FakeRequest(GET, "/exam-result/examResult-id")
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe NOT_FOUND
      }
    }

    "return BadRequest for invalid examId" in {
      val invalidExamId = "invalid-ulid"
      when(mockExamResultIdRequestConverter.validateAndCreate(any[String]))
        .thenReturn(Left("Invalid exam ID"))

      val request = FakeRequest(GET, s"/exam-result/$invalidExamId")
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe BAD_REQUEST
      }
    }
  }
}
