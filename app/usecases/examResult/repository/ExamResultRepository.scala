package usecases.examResult.repository

import domain.examResult.entity.ExamResult
import domain.examResult.valueObject._
import domain.exam.valueObject._
import scala.concurrent.Future

trait ExamResultRepository {
  def save(examResult: ExamResult): Future[Either[String, ExamResult]]
  def findById(
      examResultId: ExamResultId
  ): Future[Either[String, Option[ExamResult]]]
  def findByExamId(examId: ExamId): Future[Either[String, Seq[ExamResult]]]
  def update(examResult: ExamResult): Future[Either[String, ExamResult]]
}
