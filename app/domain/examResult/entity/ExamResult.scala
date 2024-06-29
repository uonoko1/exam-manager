package domain.examResult.entity

import domain.examResult.valueObject._

case class ExamResult(
    examId: ExamId,
    subject: Subject,
    score: Score,
    studentId: StudentId,
    evaluation: Evaluation = NotEvaluated
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
