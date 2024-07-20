package domain.evaluator.impl

import domain.evaluator.`trait`.Evaluator
import domain.examResult.entity.ExamResult
import domain.exam.entity.Exam
import domain.examResult.valueObject._

class QuartileEvaluator extends Evaluator {

  def evaluate(
      exam: Exam,
      results: Seq[ExamResult]
  ): Map[ExamResult, Evaluation] = {
    val sortedResults = results.sortBy(_.score.value)
    val q1Index = sortedResults.length / 4
    val q2Index = sortedResults.length / 2
    val q3Index = sortedResults.length * 3 / 4

    val q1Score = sortedResults(q1Index).score.value
    val q2Score = sortedResults(q2Index).score.value
    val q3Score = sortedResults(q3Index).score.value

    results.map { result =>
      val evaluation = result.score.value match {
        case _ if result.score.value >= q3Score => Evaluation.Excellent
        case _ if result.score.value >= q2Score => Evaluation.GoodJob
        case _ if result.score.value >= q1Score => Evaluation.Passed
        case _                                  => Evaluation.Failed
      }
      result -> evaluation
    }.toMap
  }
}
