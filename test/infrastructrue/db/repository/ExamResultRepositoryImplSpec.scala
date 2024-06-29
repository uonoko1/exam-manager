package infrastructure.db.repository

import org.scalatestplus.play._
import org.scalatest.concurrent.ScalaFuture
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, inject}
import infrastructure.db.DatabaseConfig
import infrastructure.db.tables.{ExamResult, ExamResultTable}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.test.Injecting

class ExamResultRepositoryImplSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with BeforeAndAfterEach
    with GuiceOneAppPerSuite
    with Injecting {
  override def fakeApplication(): Application = {
    new GuiceApplicationBuilder()
      .configure("slick.dbs.default.profile" -> "slick.jdbc.H2Profile$")
      .configure("slick.dbs.default.db.driver" -> "org.h2.Driver")
      .configure(
        "slick.dbs.default.db.url" -> "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
      )
      .configure("slick.dbs.default.db.user" -> "sa")
      .configure("slick.dbs.default.db.password" -> "")
      .build()
  }

  val dbConfigProvider = inject.instanceOf[DatabaseConfigProvider]
  val dbConfig = new DatabaseConfig(dbConfigProvider)
  val repository = new ExamResultRepositoryImpl(dbConfig)

  override def beforeEach(): Unit = {
    import dbConfig.profile.api._
    dbConfig.db.run(ExamResultTable.examResults.schema.create).futureValue
  }

  override def afterEach(): Unit = {
    import dbConfig.profile.api._
    dbConfig.db.run(ExamResultTable.examResults.schema.drop).futureValue
  }

  "ExamResultRepositoryImpl#save" should {
    "save an exam result" in {
      val examResult = ExamResult("11111111111111111111", "Math", 85, 1)
      val saveFuture = repository.save(examResult)
      whenReady(saveFuture) { _ =>
        import dbConfig.profile.api._
        val query =
          ExamResultTable.examResults.filter(_.examId === examResult.examId)
        val result = dbConfig.db.run(query.result.headOption).futureValue
        result mustBe Some(examResult)
      }
    }
  }

  "fail to save an exam result if there is a database error" in {
    val examResult = ExamResult("11111111111111111111", "Math", 85, 1)
    val brokenDbConfig = new DatabaseConfig(dbConfigProvider) {
      override val db = Database.forURL("jdbc:h2:mem:broken;DB_CLOSE_DELAY=-1")
    }
    val brokenRepository = new ExamResultRepositoryImplSpec(brokenDbConfig)
    val saveFuture = brokenRepository.save(examResult)

    whenReady(saveFuture.failed) { ex =>
      ex mustBe an[Exception]
      ex.getMessage must include("Database error")
    }
  }

  "ExamResultRepositoryImpl#findById" should {
    "find an exam result by ID" in {
      val examResult = ExamResult("11111111111111111111", "Math", 85, 1)
      dbConfig.db.run(ExamResultTable.examResults += examResult).futureValue
      val findFuture = repository.findById(examResult.examId)
      whenReady(findFuture) { result =>
        result mustBe Some(examResult)
      }
    }
  }

  "fail to find an exam result if it does not exists" in {
    val findFuture = repository.findById("nonexistent_id")
    whenReady(findFuture) { result =>
      result mustBe None
    }
  }
}
