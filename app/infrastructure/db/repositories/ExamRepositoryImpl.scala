package infrastructure.db.repositories

import javax.inject.{Inject, Singleton}
import domain.exam.entity.Exam
import dto.infrastructure.exam.entity.ExamDto
import infrastructure.db.DatabaseConfig
import infrastructure.db.table.ExamTable
import usecases.exam.repository.ExamRepository
import utils.Retry
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._
import org.apache.pekko.actor.Scheduler
import java.sql.SQLTransientConnectionException
import java.time.ZonedDateTime

@Singleton
class ExamRepositoryImpl @Inject() (
    dbConfig: DatabaseConfig,
    scheduler: Scheduler
)(implicit
    ec: ExecutionContext
) extends ExamRepository {
  import dbConfig._
  import profile.api._

  override def findByDueDate(
      startDate: ZonedDateTime,
      endDate: ZonedDateTime
  ): Future[Either[String, Seq[Exam]]] = {
    val query = ExamTable.exams
      .filter(exam => exam.dueDate >= startDate && exam.dueDate <= endDate)
      .result

    Retry
      .withRetry(run(query), 3, 1.second)(ec, scheduler)
      .map { dtos =>
        val exams = dtos.map(ExamDto.toDomain)
        val errors = exams.collect { case Left(error) => error }
        if (errors.nonEmpty) {
          Left(errors.mkString(", "))
        } else {
          Right(exams.collect { case Right(exam) => exam })
        }
      }
      .recover {
        case ex: SQLTransientConnectionException =>
          Left("Database connection error")
        case ex: Throwable => Left(ex.getMessage)
      }
  }

  override def update(exam: Exam): Future[Either[String, Exam]] = {
    val dto = ExamDto.fromDomain(exam)
    val query =
      ExamTable.exams.filter(_.examId === dto.examIdDto.value).update(dto)
    Retry
      .withRetry(run(query), 3, 1.second)(ec, scheduler)
      .map(_ => Right(exam))
      .recover {
        case ex: SQLTransientConnectionException =>
          Left("Database connection error")
        case ex: Throwable => Left(ex.getMessage)
      }
  }
}
