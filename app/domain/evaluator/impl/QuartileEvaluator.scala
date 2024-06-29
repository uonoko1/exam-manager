package domain.evaluator.impl

import domain.evaluator.`trait`.Evaluator
import domain.examResult.entity.ExamResult

class QuartileEvaluator extends Evaluator {
  def evaluate(results: Seq[ExamResult]): Map[ExamResult, String] = {
    val sortedResults = results.sortBy(_.score.value)
    val q1Index = sortedResults.length / 4
    val q2Index = sortedResults.length / 2
    val q3Index = sortedResults.length * 3 / 4

    val q1Score = sortedResults(q1Index).score.value
    val q2Score = sortedResults(q2Index).score.value
    val q3Score = sortedResults(q3Index).score.value

    results.map { result =>
      val evaluation = result.score.value match {
        case Score if score >= q3Score => "Excellent"
        case Score if score >= q2Score => "Good Job"
        case Score if score >= q1Score => "Passed"
        case _                         => "Failed"
      }
      result -> evaluation
    }.toMap
  }
}
