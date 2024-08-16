package scheduler

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.concurrent.Eventually._
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.apache.pekko.actor.ActorSystem

import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.bind
import play.api.{Application, Configuration}
import play.api.db.slick.DatabaseConfigProvider

import domain.exam.entity.Exam
import domain.exam.valueObject._
import domain.examResult.entity.ExamResult
import domain.examResult.valueObject._
import domain.utils.dateTime.{CreatedAt, UpdatedAt}
import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import domain.evaluationPeriodProvider.impl.WeeklyEvaluationPeriodProviderImpl
import domain.evaluator.`trait`.Evaluator
import domain.evaluator.impl.QuartileEvaluatorImpl
import usecases.examResult.ExamResultUsecase
import usecases.examResult.repository.ExamResultRepository
import usecases.exam.repository.ExamRepository
import usecases.examResult.logic.examResultUpdater.`trait`.ExamResultUpdater
import usecases.examResult.logic.examResultUpdater.impl.ExamResultUpdaterImpl
import usecases.exam.logic.examUpdater.`trait`.ExamUpdater
import usecases.exam.logic.examUpdater.impl.ExamUpdaterImpl
import infrastructure.db.table.{ExamTable, ExamResultTable}
import infrastructure.db.repositories.ExamRepositoryImplOnDb
import infrastructure.db.repositories.ExamResultRepositoryImplOnDb
import infrastructure.db.DatabaseConfig
import utils.CustomPatience
import utils.{SystemClock, UlidGenerator}
import utils.TestDatabaseConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

import java.time.ZonedDateTime
import slick.jdbc.H2Profile.api._

class JobSchedulerIntegrationSpec
    extends PlaySpec
    with ScalaFutures
    with CustomPatience
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  val mockSystemClock: SystemClock = mock(classOf[SystemClock])
  val mockUlidGenerator: UlidGenerator = mock(classOf[UlidGenerator])

  val dbName: String = TestDatabaseConfig.generateUniqueDbName()
  val app: Application = new GuiceApplicationBuilder()
    .configure(TestDatabaseConfig.buildTestConfiguration(dbName))
    .overrides(
      bind[SystemClock].toInstance(mockSystemClock),
      bind[UlidGenerator].toInstance(mockUlidGenerator),
      bind[Evaluator].to[QuartileEvaluatorImpl],
      bind[ExamResultUpdater].to[ExamResultUpdaterImpl],
      bind[ExamUpdater].to[ExamUpdaterImpl],
      bind[EvaluationPeriodProvider].to[WeeklyEvaluationPeriodProviderImpl]
    )
    .build()

  val dbConfig = app.injector.instanceOf[DatabaseConfig]
  val examTable = TableQuery[ExamTable]
  val examResultTable = TableQuery[ExamResultTable]

  override def beforeAll(): Unit = {
    dbConfig.run(examTable.schema.create).futureValue
    dbConfig.run(examResultTable.schema.create).futureValue
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    dbConfig.run(examTable.schema.drop).futureValue
    dbConfig.run(examResultTable.schema.drop).futureValue
    super.afterAll()
  }

  override def beforeEach(): Unit = {
    dbConfig.run(examTable.delete).futureValue
    dbConfig.run(examResultTable.delete).futureValue
    super.beforeEach()
  }

  "JobScheduler" should {
    "execute the scheduled job immediately in test mode" in {
      val examRepository = app.injector.instanceOf[ExamRepositoryImplOnDb]
      val examResultRepository =
        app.injector.instanceOf[ExamResultRepositoryImplOnDb]
      val actorSystem = app.injector.instanceOf[ActorSystem]

      val now = ZonedDateTime.parse("2024-07-22T00:00:00+09:00[Asia/Tokyo]")
      val examAndResultTimestamp =
        ZonedDateTime.parse("2024-07-20T12:00:00+09:00[Asia/Tokyo]")

      when(mockSystemClock.now()).thenReturn(now)

      val exam = Exam(
        ExamId("exam-id"),
        Subject.Math,
        DueDate(now.minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(examAndResultTimestamp),
        UpdatedAt(examAndResultTimestamp)
      )

      val examResult = ExamResult(
        ExamResultId("01ARZ3NDEKTSV4RRFFQ69G5FAV"),
        ExamId("exam-id"),
        Score(85),
        StudentId("student-id"),
        Evaluation.NotEvaluated,
        CreatedAt(examAndResultTimestamp),
        UpdatedAt(examAndResultTimestamp)
      )

      examRepository.save(exam).futureValue mustBe Right(exam)
      examResultRepository.save(examResult).futureValue mustBe Right(examResult)

      val scheduler = new JobScheduler(
        actorSystem,
        app.injector.instanceOf[ExamResultUsecase],
        mockSystemClock,
        isTestMode = true
      )

      eventually(timeout(Span(5, Seconds)), interval(Span(500, Millis))) {
        val updatedExamResult =
          examResultRepository.findById(examResult.examResultId).futureValue
        updatedExamResult mustBe Right(
          Some(
            examResult.copy(
              evaluation = Evaluation.Excellent,
              updatedAt = UpdatedAt(now)
            )
          )
        )
      }
    }
  }
}
