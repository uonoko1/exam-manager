package domain.evaluator.`trait`

import domain.examResult.entity.ExamResult
import domain.exam.entity.Exam

trait Evaluator {
  def evaluate(exam: Exam, results: Seq[ExamResult]): Map[ExamResult, String]
}
