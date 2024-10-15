package controllers

import play.api.test.{ FakeRequest, Injecting }
import play.api.test.Helpers._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JsValue
import play.api.libs.json.Json

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatest.matchers.must.Matchers._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Millis, Seconds, Span }
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.utils.dateTime.{ CreatedAt, UpdatedAt }
import domain.examResult.entity.ExamResult
import usecases.examResult.ExamResultUsecase
import dto.response.examResult.entity.ExamResultResponseDto
import dto.request.examResult.jsonParser.examResultFieldConverter.`trait`.ExamResultFieldConverter
import utils.CustomPatience

import scala.concurrent.Future
import java.time.ZonedDateTime
import dto.request.examResult.valueObject.ExamResultIdRequestDtoFactory
import dto.request.exam.valueObject.SubjectRequestDto
import dto.request.examResult.valueObject.ScoreRequestDto
import dto.request.exam.valueObject.ExamIdRequestDto
import dto.request.examResult.valueObject.StudentIdRequestDto
import dto.request.examResult.valueObject.ExamResultIdRequestDto

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
  val mockExamResultIdRequestDtoFactory: ExamResultIdRequestDtoFactory = mock(
    classOf[ExamResultIdRequestDtoFactory]
  )

  override def fakeApplication() = new GuiceApplicationBuilder()
    .overrides(
      bind[ExamResultUsecase].toInstance(mockUsecase),
      bind[ExamResultFieldConverter].toInstance(mockExamResultFieldConverter),
      bind[ExamResultIdRequestDtoFactory]
        .toInstance(mockExamResultIdRequestDtoFactory)
    )
    .build()

  "ExamResultController POST /exam-result" should {
    "return OK for valid parameters" in {
      val examResult = ExamResult(
        ExamResultId("test-id"),
        ExamId("exam-id"),
        Score(85),
        StudentId("student-id"),
        Evaluation.Passed,
        CreatedAt(ZonedDateTime.now()),
        UpdatedAt(ZonedDateTime.now())
      )

      when(
        mockUsecase.saveExamResult(
          any[ExamIdRequestDto],
          any[SubjectRequestDto],
          any[ScoreRequestDto],
          any[StudentIdRequestDto]
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
              ExamIdRequestDto("exam-id"),
              SubjectRequestDto.Math,
              ScoreRequestDto(85),
              StudentIdRequestDto("student-id")
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
        ExamResultId("test-id"),
        ExamId("exam-id"),
        Score(85),
        StudentId("student-id"),
        Evaluation.Passed,
        CreatedAt(ZonedDateTime.now()),
        UpdatedAt(ZonedDateTime.now())
      )

      when(mockUsecase.findById(any[ExamResultIdRequestDto]))
        .thenReturn(Future.successful(Right(Some(examResult))))

      when(mockExamResultIdRequestDtoFactory.create(any[String]))
        .thenReturn(Right(ExamResultIdRequestDto("examResult-id")))

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
      when(mockUsecase.findById(any[ExamResultIdRequestDto]))
        .thenReturn(Future.successful(Right(None)))

      when(mockExamResultIdRequestDtoFactory.create(any[String]))
        .thenReturn(Right(ExamResultIdRequestDto("examResult-id")))

      val request = FakeRequest(GET, "/exam-result/examResult-id")
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe NOT_FOUND
      }
    }

    "return BadRequest for invalid examId" in {
      val invalidExamId = "invalid-ulid"
      when(mockExamResultIdRequestDtoFactory.create(any[String]))
        .thenReturn(Left("Invalid exam ID"))

      val request = FakeRequest(GET, s"/exam-result/$invalidExamId")
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe BAD_REQUEST
      }
    }
  }
}
