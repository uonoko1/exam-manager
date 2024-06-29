package domain.evaluator.`trait`

import domain.examResult.entity.ExamResult

trait Evaluator {
  def evaluate(results: Seq[ExamResult]): Map[ExamResult, String]
}
