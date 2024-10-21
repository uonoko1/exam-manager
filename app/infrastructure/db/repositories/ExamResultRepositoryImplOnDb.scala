package infrastructure.db.repositories

import javax.inject.{ Inject, Singleton }
import infrastructure.db.DatabaseConfig
import infrastructure.db.table.ExamResultTable
import scala.concurrent.{ ExecutionContext, Future }
import usecases.examResult.repository.ExamResultRepository
import domain.examResult.entity.ExamResult
import domain.exam.valueObject._
import dto.infrastructure.examResult.entity.ExamResultDto
import utils.Retry
import scala.concurrent.duration._
import org.apache.pekko.actor.Scheduler
import java.sql.SQLTransientConnectionException
import java.time.ZonedDateTime
import domain.examResult.valueObject.ExamResultId

@Singleton
class ExamResultRepositoryImplOnDb @Inject() (
    dbConfig: DatabaseConfig,
    scheduler: Scheduler
)(implicit
    ec: ExecutionContext
) extends ExamResultRepository {
  import dbConfig._
  import profile.api._

  override def save(
      examResult: ExamResult
  ): Future[Either[String, ExamResult]] = {
    val dto = ExamResultDto.fromDomain(examResult)
    val query = ExamResultTable.examResults += dto
    Retry
      .withRetry(run(query), 3, 1.second)(ec, scheduler)
      .map(_ => Right(examResult))
      .recover {
        case ex: SQLTransientConnectionException =>
          Left(s"Database connection error: ${ex.getMessage}")
        case ex: Throwable => Left(ex.getMessage)
      }
  }

  override def findById(
      examResultId: ExamResultId
  ): Future[Either[String, Option[ExamResult]]] = {
    val query = ExamResultTable.examResults
      .filter(_.examResultId === examResultId.value)
      .result
      .headOption
    Retry
      .withRetry(run(query), 3, 1.second)(ec, scheduler)
      .map {
        case Some(dto) =>
          ExamResultDto.toDomain(dto) match {
            case Right(examResult) => Right(Some(examResult))
            case Left(error)       => Left(error)
          }
        case None => Right(None)
      }
      .recover {
        case ex: SQLTransientConnectionException =>
          Left(s"Database connection error: ${ex.getMessage}")
        case ex: Throwable => Left(ex.getMessage)
      }
  }

  override def findByExamId(
      examId: ExamId
  ): Future[Either[String, Seq[ExamResult]]] = {
    val query = ExamResultTable.examResults
      .filter(_.examId === examId.value)
      .result
    Retry
      .withRetry(run(query), 3, 1.second)(ec, scheduler)
      .map { dtos =>
        val examResults = dtos.map(ExamResultDto.toDomain)
        val errors = examResults.collect { case Left(error) => error }
        if (errors.nonEmpty) {
          Left(errors.mkString(", "))
        } else {
          Right(examResults.collect { case Right(examResult) => examResult })
        }
      }
      .recover {
        case ex: SQLTransientConnectionException =>
          Left(s"Database connection error: ${ex.getMessage}")
        case ex: Throwable => Left(ex.getMessage)
      }
  }

  override def update(
      examResult: ExamResult
  ): Future[Either[String, ExamResult]] = {
    val dto = ExamResultDto.fromDomain(examResult)
    val query = ExamResultTable.examResults
      .filter(_.examResultId === dto.examResultId)
      .update(dto)
    Retry
      .withRetry(run(query), 3, 1.second)(ec, scheduler)
      .map(_ => Right(examResult))
      .recover {
        case ex: SQLTransientConnectionException =>
          Left(s"Database connection error: ${ex.getMessage}")
        case ex: Throwable => Left(ex.getMessage)
      }
  }
}
