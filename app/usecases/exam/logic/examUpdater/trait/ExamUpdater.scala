package usecases.exam.logic.examUpdater.`trait`

import domain.exam.entity.Exam
import domain.examResult.entity.ExamResult
import scala.concurrent.Future

trait ExamUpdater {
  def updateEvaluations(
      exam: Exam,
      updatedExamResults: Seq[ExamResult],
      examResults: Seq[ExamResult]
  ): Future[Either[String, Exam]]
}
