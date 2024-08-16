package controllers

import play.api.test.{FakeRequest, Injecting}
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.db.slick.DatabaseConfigProvider
import play.api.{Configuration, Application}

import org.scalatestplus.play.PlaySpec
import org.scalatest.matchers.must.Matchers._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.concurrent.ScalaFutures._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfterEach
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

import domain.examResult.entity.ExamResult
import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.evaluator.impl.QuartileEvaluatorImpl
import domain.evaluator.`trait`.Evaluator
import domain.evaluationPeriodProvider.impl.WeeklyEvaluationPeriodProviderImpl
import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import domain.utils.dateTime.{CreatedAt, UpdatedAt}
import usecases.examResult.ExamResultUsecase
import usecases.examResult.repository.ExamResultRepository
import usecases.exam.repository.ExamRepository
import usecases.examResult.logic.examResultUpdater.impl.ExamResultUpdaterImpl
import usecases.examResult.logic.examResultUpdater.`trait`.ExamResultUpdater
import usecases.exam.logic.examUpdater.impl.ExamUpdaterImpl
import usecases.exam.logic.examUpdater.`trait`.ExamUpdater
import infrastructure.libs.UlidGeneratorImpl
import infrastructure.db.repositories.{
  ExamResultRepositoryImplOnDb,
  ExamRepositoryImplOnDb
}
import infrastructure.db.DatabaseConfig
import infrastructure.db.table.ExamResultTable
import dto.request.examResult.jsonParser.examResultFieldConverter.`trait`.ExamResultFieldConverter
import dto.request.examResult.valueObject.examResultIdRequestConverter.`trait`.ExamResultIdRequestConverter
import dto.response.examResult.entity.ExamResultResponseDto
import utils.SystemClock
import utils.UlidGenerator
import utils.CustomPatience
import utils.TestDatabaseConfig

import java.time.ZonedDateTime
import scala.concurrent.{Future, ExecutionContext}
import slick.jdbc.H2Profile.api._

class ExamResultControllerIntegrationSpec
    extends PlaySpec
    with Injecting
    with ScalaFutures
    with CustomPatience
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val mockSystemClock: SystemClock = mock(classOf[SystemClock])
  val mockUlidGenerator: UlidGenerator = mock(classOf[UlidGenerator])

  val dbName: String = TestDatabaseConfig.generateUniqueDbName()
  val app: Application = new GuiceApplicationBuilder()
    .configure(TestDatabaseConfig.buildTestConfiguration(dbName))
    .overrides(
      bind[SystemClock].toInstance(mockSystemClock),
      bind[UlidGenerator].toInstance(mockUlidGenerator),
      bind[ExamResultRepository].to[ExamResultRepositoryImplOnDb],
      bind[ExamRepository].to[ExamRepositoryImplOnDb],
      bind[Evaluator].to[QuartileEvaluatorImpl],
      bind[ExamResultUpdater].to[ExamResultUpdaterImpl],
      bind[ExamUpdater].to[ExamUpdaterImpl],
      bind[EvaluationPeriodProvider].to[WeeklyEvaluationPeriodProviderImpl]
    )
    .build()

  val dbConfig = app.injector.instanceOf[DatabaseConfig]
  val examResultTable = TableQuery[ExamResultTable]

  override def beforeAll(): Unit = {
    dbConfig.run(examResultTable.schema.create).futureValue
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    dbConfig.run(examResultTable.schema.drop).futureValue
    super.afterAll()
  }

  override def beforeEach(): Unit = {
    reset(mockUlidGenerator)
    dbConfig.run(examResultTable.delete).futureValue
    super.beforeEach()
  }

  "ExamResultController#saveExamResult" should {
    "return OK when the exam result is saved successfully" in {
      val examId = ExamId("01ARZ3NDEKTSV4RRFFQ69G5FAV")
      val subject = Subject.Math
      val score = Score(85)
      val studentId = StudentId("01ARZ3NDEKTSV4RRFFQ69G5FAV")
      val examResultId = "01ARZ3NDEKTSV4RRFFQ69G5FAV"
      val fixedNow = ZonedDateTime.parse("2024-07-21T12:00+09:00[Asia/Tokyo]")

      val examResult = ExamResultResponseDto(
        examResultId,
        "01ARZ3NDEKTSV4RRFFQ69G5FAV",
        85,
        "01ARZ3NDEKTSV4RRFFQ69G5FAV",
        "Not Evaluated",
        "2024-07-21T12:00+09:00[Asia/Tokyo]",
        "2024-07-21T12:00+09:00[Asia/Tokyo]"
      )

      when(mockSystemClock.now()).thenReturn(fixedNow)
      when(mockUlidGenerator.generate()).thenReturn(examResultId)
      when(mockUlidGenerator.isValid(any[String])).thenReturn(true)

      val request = FakeRequest(POST, "/exam-result")
        .withJsonBody(
          Json.obj(
            "examId" -> examId.value,
            "subject" -> subject.value,
            "score" -> score.value.toString,
            "studentId" -> studentId.value
          )
        )
        .withCSRFToken

      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe OK
        contentAsJson(result) mustBe Json.toJson(examResult)
      }
    }

    "return BadRequest when the request is invalid" in {
      val request =
        FakeRequest(POST, "/exam-result").withJsonBody(Json.obj()).withCSRFToken
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe BAD_REQUEST
        contentAsString(result) must include("Invalid parameters")
      }
    }
  }

  "ExamResultController#getExamResult" should {
    "return OK when the exam result is found" in {
      val examId = ExamId("01ARZ3NDEKTSV4RRFFQ69G5FAV")
      val subject = Subject.Math
      val score = Score(85)
      val studentId = StudentId("01ARZ3NDEKTSV4RRFFQ69G5FAV")
      val examResultId = ExamResultId("01ARZ3NDEKTSV4RRFFQ69G5FAV")
      val fixedNow = ZonedDateTime.parse("2024-07-21T12:00+09:00[Asia/Tokyo]")

      when(mockSystemClock.now()).thenReturn(fixedNow)
      when(mockUlidGenerator.generate()).thenReturn(examResultId.value)
      when(mockUlidGenerator.isValid(any[String])).thenReturn(true)

      val postRequest = FakeRequest(POST, "/exam-result")
        .withJsonBody(
          Json.obj(
            "examId" -> examId.value,
            "subject" -> subject.value,
            "score" -> score.value.toString,
            "studentId" -> studentId.value
          )
        )
        .withCSRFToken

      val postResult = route(app, postRequest).get
      whenReady(postResult) { res =>
        status(postResult) mustBe OK
      }

      val examResult = ExamResultResponseDto(
        "01ARZ3NDEKTSV4RRFFQ69G5FAV",
        "01ARZ3NDEKTSV4RRFFQ69G5FAV",
        85,
        "01ARZ3NDEKTSV4RRFFQ69G5FAV",
        "Not Evaluated",
        "2024-07-21T12:00+09:00[Asia/Tokyo]",
        "2024-07-21T12:00+09:00[Asia/Tokyo]"
      )

      val getRequest =
        FakeRequest(GET, s"/exam-result/${examResultId.value}").withCSRFToken
      val getResult = route(app, getRequest).get

      whenReady(getResult) { res =>
        status(getResult) mustBe OK
        contentAsJson(getResult) mustBe Json.toJson(examResult)
      }
    }

    "return NotFound when the exam result is not found" in {
      val examResultId = ExamResultId("01ARZ3NDEKTSV4RRFFQ69G5FAV")
      val notFoundId = examResultId.value

      when(mockUlidGenerator.isValid(any[String])).thenReturn(true)

      val request =
        FakeRequest(GET, s"/exam-result/${examResultId.value}").withCSRFToken
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe NOT_FOUND
        contentAsString(result) must include(
          s"Exam result with id $notFoundId not found"
        )
      }
    }

    "return BadRequest when the exam result ID is invalid" in {
      when(mockUlidGenerator.isValid(any[String])).thenReturn(false)
      val request = FakeRequest(GET, s"/exam-result/invalid-id").withCSRFToken
      val result = route(app, request).get

      whenReady(result) { res =>
        status(result) mustBe BAD_REQUEST
        contentAsString(result) must include("Invalid exam ID")
      }
    }
  }
}
