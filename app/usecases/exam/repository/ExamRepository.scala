package usecases.exam.repository

import domain.exam.entity.Exam
import scala.concurrent.Future
import java.time.ZonedDateTime

trait ExamRepository {
  def findByDueDate(
      startDate: ZonedDateTime,
      endDate: ZonedDateTime
  ): Future[Either[String, Seq[Exam]]]
  def update(exam: Exam): Future[Either[String, Exam]]
}
