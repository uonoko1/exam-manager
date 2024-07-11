package usecases.exam.logic.examUpdater.impl

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import domain.exam.entity.Exam
import domain.examResult.entity.ExamResult
import domain.exam.valueObject._
import domain.utils.dateTime.UpdatedAt
import usecases.exam.repository.ExamRepository
import usecases.exam.logic.examUpdater.`trait`.ExamUpdater
import java.time.ZonedDateTime

class ExamUpdaterImpl @Inject() (examRepository: ExamRepository)(implicit
    ec: ExecutionContext
) extends ExamUpdater {

  override def updateEvaluations(
      exam: Exam,
      updatedExamResults: Seq[ExamResult],
      examResults: Seq[ExamResult]
  ): Future[Either[String, Exam]] = {
    val newStatus = if (updatedExamResults.size == examResults.size) {
      EvaluationStatus.Evaluated
    } else if (updatedExamResults.nonEmpty) {
      EvaluationStatus.PartiallyEvaluated
    } else {
      EvaluationStatus.NotEvaluated
    }

    val updatedExam = exam.copy(
      evaluationStatus = newStatus,
      updatedAt = UpdatedAt(ZonedDateTime.now())
    )
    examRepository.update(updatedExam)
  }
}
