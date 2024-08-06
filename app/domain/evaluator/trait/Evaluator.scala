package domain.evaluator.`trait`

import domain.examResult.entity.ExamResult
import domain.exam.entity.Exam
import domain.examResult.valueObject.Evaluation

trait Evaluator {
  def evaluate(
      exam: Exam,
      results: Seq[ExamResult]
  ): Map[ExamResult, Evaluation]
}
