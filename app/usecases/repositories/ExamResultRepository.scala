package usecases.repositories

import domain.examResult.entity.ExamResult
import scala.concurrent.Future
import domain.examResult.valueObject.ExamId

trait ExamResultRepository {
  def save(examResult: ExamResult): Future[Unit]
  def findById(examId: ExamId): Future[Option[ExamResult]]
}
