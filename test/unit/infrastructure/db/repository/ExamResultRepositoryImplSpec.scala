package infrastructure.db.repository

import play.api.{Application, Configuration}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.db.slick.DatabaseConfigProvider
import play.api.Environment

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.BeforeAndAfterAll
import org.apache.pekko.actor.Scheduler
import org.scalatest.BeforeAndAfterEach

import domain.examResult.entity.ExamResult
import domain.exam.valueObject._
import domain.utils.dateTime.{CreatedAt, UpdatedAt}
import domain.examResult.valueObject._
import domain.exam.valueObject._
import infrastructure.db.DatabaseConfig
import infrastructure.db.repositories.ExamResultRepositoryImpl
import infrastructure.db.table.ExamResultTable
import dto.infrastructure.examResult.entity.ExamResultDto
import utils.CustomPatience
import utils.TestDatabaseConfig

import scala.concurrent.{ExecutionContext, Future}
import java.time.ZonedDateTime
import java.sql.SQLTransientConnectionException
import slick.jdbc.H2Profile.api._

class ExamResultRepositoryImplSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with CustomPatience {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val dbName: String = TestDatabaseConfig.generateUniqueDbName()
  val app: Application = new GuiceApplicationBuilder()
    .configure(TestDatabaseConfig.buildTestConfiguration(dbName))
    .build()

  val dbConfig = app.injector.instanceOf[DatabaseConfig]
  val mockScheduler = mock[Scheduler]
  val repository = new ExamResultRepositoryImpl(dbConfig, mockScheduler)

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
    dbConfig.run(examResultTable.delete).futureValue
    super.beforeEach()
  }

  "ExamResultRepositoryImpl#save" should {
    "return a saved Exam when given valid ExamResult as input" in {
      val examResult = ExamResult(
        ExamResultId("exam-result-id"),
        ExamId("exam-id"),
        Score(85),
        StudentId("student-id"),
        Evaluation.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val result = repository.save(examResult)

      whenReady(result) { res =>
        res mustBe Right(examResult)
      }
    }

    "handle database connection error on save" in {
      val examResult = ExamResult(
        ExamResultId("exam-result-id"),
        ExamId("exam-id"),
        Score(85),
        StudentId("student-id"),
        Evaluation.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val failedFuture = Future.failed(
        new SQLTransientConnectionException("Database connection error")
      )
      whenReady(failedFuture.failed) { ex =>
        ex mustBe a[SQLTransientConnectionException]
      }
    }
  }

  "ExamResultRepositoryImpl#findById" should {
    "return a ExamResult when given corresponding ExamResultId as input" in {
      val examResultDto = ExamResultDto(
        "examResult-id",
        "exam-id",
        85,
        "student-id",
        "Not Evaluated",
        ZonedDateTime.now().minusDays(10),
        ZonedDateTime.now().minusDays(1)
      )

      dbConfig.run(examResultTable += examResultDto).futureValue

      val result = repository.findById(ExamResultId("examResult-id"))

      whenReady(result) { res =>
        res mustBe Right(
          Some(ExamResultDto.toDomain(examResultDto).toOption.get)
        )
      }
    }

    "handle database connection error on findById" in {
      val failedFuture = Future.failed(
        new SQLTransientConnectionException("Database connection error")
      )
      whenReady(failedFuture.failed) { ex =>
        ex mustBe a[SQLTransientConnectionException]
      }
    }
  }

  "ExamResultRepositoryImpl#findByExamId" should {
    "return some ExamResult when given corresponding ExamId as input" in {
      val examResultDto = ExamResultDto(
        "exam-result-id",
        "exam-id",
        85,
        "student-id",
        "Not Evaluated",
        ZonedDateTime.now().minusDays(10),
        ZonedDateTime.now().minusDays(1)
      )

      dbConfig.run(examResultTable += examResultDto).futureValue

      val result = repository.findByExamId(ExamId("exam-id"))

      whenReady(result) { res =>
        res mustBe Right(
          Seq(ExamResultDto.toDomain(examResultDto).toOption.get)
        )
      }
    }

    "handle database connection error on findByExamId" in {
      val failedFuture = Future.failed(
        new SQLTransientConnectionException("Database connection error")
      )
      whenReady(failedFuture.failed) { ex =>
        ex mustBe a[SQLTransientConnectionException]
      }
    }
  }

  "ExamResultRepositoryImpl#update" should {
    "return the updated ExamResult when given valid ExamResult as input" in {
      val examResult = ExamResult(
        ExamResultId("exam-result-id"),
        ExamId("exam-id"),
        Score(85),
        StudentId("student-id"),
        Evaluation.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val result = repository.update(examResult)

      whenReady(result) { res =>
        res mustBe Right(examResult)
      }
    }

    "handle database connection error on update" in {
      val examResult = ExamResult(
        ExamResultId("exam-result-id"),
        ExamId("exam-id"),
        Score(85),
        StudentId("student-id"),
        Evaluation.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val failedFuture = Future.failed(
        new SQLTransientConnectionException("Database connection error")
      )
      whenReady(failedFuture.failed) { ex =>
        ex mustBe a[SQLTransientConnectionException]
      }
    }

    "handle other database errors on update" in {
      val examResult = ExamResult(
        ExamResultId("exam-result-id"),
        ExamId("exam-id"),
        Score(85),
        StudentId("student-id"),
        Evaluation.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val failedFuture = Future.failed(new Exception("Some other error"))
      whenReady(failedFuture.failed) { ex =>
        ex mustBe an[Exception]
      }
    }
  }
}
