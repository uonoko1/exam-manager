package domain.examResult.entity

import domain.examResult.valueObject._
import domain.utils.dateTime._

case class ExamResult(
    examResultId: ExamResultId,
    examId: ExamId,
    score: Score,
    studentId: StudentId,
    evaluation: Evaluation = NotEvaluated,
    createdAt: CreatedAt,
    updatedAt: UpdatedAt
) {
  def evaluateScore(score: Int): String = {
    score match {
      case s if s >= 90 => "Excellent!"
      case s if s >= 75 => "Good Job!"
      case s if s >= 50 => "Passed"
      case _            => "Failed"
    }
  }
}
