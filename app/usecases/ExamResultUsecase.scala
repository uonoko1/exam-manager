package usecases

import domain.examResult.entity.ExamResult
import usecases.repositories.ExamResultRepository
import utils.UlidGenerator
import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import domain.examResult.valueObject._

@Singleton
class ExamResultUsecase @Inject() (
    examResultRepository: ExamResultRepository,
    ulidGenerator: UlidGenerator
)(implicit ec: ExecutionContext) {
  def saveExamResult(
      subject: Subject,
      score: Score,
      studentId: StudentId
  ): Future[String] = {
    val examId = ulidGenerator.generate()
    val examResult = ExamResult(ExamId(examId), subject, score, studentId)

    examResultRepository
      .save(examResult)
      .map(_ => examResult.evaluateScore(score.value))
  }

  def findExamResultById(examId: ExamId): Future[Option[ExamResult]] = {
    examResultRepository.findById(examId)
  }
}
