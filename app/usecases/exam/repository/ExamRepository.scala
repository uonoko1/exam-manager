package usecases.exam.repository

import domain.exam.entity.Exam
import scala.concurrent.Future
import java.time.ZonedDateTime
import domain.exam.valueObject._

trait ExamRepository {
  def save(exam: Exam): Future[Either[String, Exam]]
  def findById(examId: ExamId): Future[Either[String, Option[Exam]]]
  def findByDueDate(
      startDate: ZonedDateTime,
      endDate: ZonedDateTime
  ): Future[Either[String, Seq[Exam]]]
  def update(exam: Exam): Future[Either[String, Exam]]
}
