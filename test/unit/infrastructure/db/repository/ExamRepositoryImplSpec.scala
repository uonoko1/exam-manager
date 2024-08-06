package infrastructure.db.repository

import play.api.{Application, Configuration}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.db.slick.DatabaseConfigProvider
import play.api.Environment

import org.scalatestplus.play.PlaySpec
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfterEach
import org.scalatest.time.{Millis, Seconds, Span}
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.apache.pekko.actor.Scheduler

import domain.exam.entity.Exam
import domain.exam.valueObject._
import domain.utils.dateTime.{CreatedAt, UpdatedAt}
import infrastructure.db.DatabaseConfig
import infrastructure.db.repositories.ExamRepositoryImpl
import infrastructure.db.table.ExamTable
import dto.infrastructure.exam.entity.ExamDto
import utils.CustomPatience
import utils.TestDatabaseConfig

import scala.concurrent.{ExecutionContext, Future}
import java.time.ZonedDateTime
import java.sql.SQLTransientConnectionException
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcProfile

class ExamRepositoryImplSpec
    extends PlaySpec
    with Matchers
    with ScalaFutures
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with CustomPatience {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val dbName: String = TestDatabaseConfig.generateUniqueDbName()
  val app: Application = new GuiceApplicationBuilder()
    .configure(TestDatabaseConfig.buildTestConfiguration(dbName))
    .build()

  val dbConfig = app.injector.instanceOf[DatabaseConfig]
  val mockScheduler = mock(classOf[Scheduler])
  val repository = new ExamRepositoryImpl(dbConfig, mockScheduler)

  val examTable = TableQuery[ExamTable]

  override def beforeAll(): Unit = {
    dbConfig.run(examTable.schema.create).futureValue
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    dbConfig.run(examTable.schema.drop).futureValue
    super.afterAll()
  }

  override def beforeEach(): Unit = {
    dbConfig.run(examTable.delete).futureValue
    super.beforeEach()
  }

  "ExamRepositoryImpl#save" should {
    "return a saved Exam when given valid Exam as input" in {
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val result = repository.save(exam)

      whenReady(result) { res =>
        res mustBe Right(exam)
      }
    }

    "handle database connection error on save" in {
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
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

  "ExamRepositoryImpl#findById" should {
    "return an Exam when given a valid ExamId" in {
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      dbConfig.run(examTable += ExamDto.fromDomain(exam)).futureValue

      val result = repository.findById(exam.examId)

      whenReady(result) { res =>
        res mustBe Right(Some(exam))
      }
    }

    "return None when the ExamId does not exist" in {
      val examId = ExamId("non-existent-id")

      val result = repository.findById(examId)

      whenReady(result) { res =>
        res mustBe Right(None)
      }
    }

    "handle database connection error on findById" in {
      val examId = ExamId("exam-id")

      val failedFuture = Future.failed(
        new SQLTransientConnectionException("Database connection error")
      )

      whenReady(failedFuture.failed) { ex =>
        ex mustBe a[SQLTransientConnectionException]
      }
    }

    "handle other database errors on findById" in {
      val examId = ExamId("exam-id")

      val failedFuture = Future.failed(new Exception("Some other error"))

      whenReady(failedFuture.failed) { ex =>
        ex mustBe an[Exception]
      }
    }
  }

  "ExamRepositoryImpl#findByDueDate" should {
    "return exams within the date range as input" in {
      val startDate = ZonedDateTime.now().minusDays(10)
      val endDate = ZonedDateTime.now()

      val expectedExam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val examDtos = Seq(
        ExamDto(
          expectedExam.examId.value,
          expectedExam.subject.value,
          expectedExam.dueDate.value,
          expectedExam.evaluationStatus.value,
          expectedExam.createdAt.value,
          expectedExam.updatedAt.value
        )
      )

      dbConfig.run(examTable ++= examDtos).futureValue

      val result = repository.findByDueDate(startDate, endDate)

      whenReady(result) { res =>
        res mustBe Right(Seq(expectedExam))
      }
    }

    "return an error if no exams found within the date range" in {
      val startDate = ZonedDateTime.now().minusDays(10)
      val endDate = ZonedDateTime.now()

      val result = repository.findByDueDate(startDate, endDate)

      whenReady(result) { res =>
        res mustBe Left("No exams found for the given period")
      }
    }

    "handle database connection error" in {
      val startDate = ZonedDateTime.now().minusDays(10)
      val endDate = ZonedDateTime.now()

      val failedFuture = Future.failed(
        new SQLTransientConnectionException("Database connection error")
      )
      whenReady(failedFuture.failed) { ex =>
        ex mustBe a[SQLTransientConnectionException]
      }
    }

    "handle other database errors" in {
      val startDate = ZonedDateTime.now().minusDays(10)
      val endDate = ZonedDateTime.now()

      val failedFuture = Future.failed(new Exception("Some other error"))
      whenReady(failedFuture.failed) { ex =>
        ex mustBe an[Exception]
      }
    }
  }

  "ExamRepositoryImpl#update" should {
    "return a updated Exam when given valid Exam as input" in {
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val queryResult = Future.successful(1)

      val result = repository.update(exam)

      whenReady(result) { res =>
        res mustBe Right(exam)
      }
    }

    "handle database connection error on update" in {
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
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
      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
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
