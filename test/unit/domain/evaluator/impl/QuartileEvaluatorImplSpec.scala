package domain.evaluator.impl

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import domain.examResult.entity.ExamResult
import domain.exam.entity.Exam
import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.utils.dateTime.{ CreatedAt, UpdatedAt }
import utils.CustomPatience
import java.time.ZonedDateTime

class QuartileEvaluatorImplSpec
    extends AnyWordSpec
    with Matchers
    with CustomPatience {

  "QuartileEvaluatorImpl" should {
    "correctly evaluate exam results based on quartiles" in {
      val evaluator = new QuartileEvaluatorImpl

      val exam = Exam(
        ExamId("exam1"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val results = Seq(
        ExamResult(
          ExamResultId("1"),
          ExamId("exam1"),
          Score(10),
          StudentId("student1"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        ),
        ExamResult(
          ExamResultId("2"),
          ExamId("exam1"),
          Score(20),
          StudentId("student2"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        ),
        ExamResult(
          ExamResultId("3"),
          ExamId("exam1"),
          Score(30),
          StudentId("student3"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        ),
        ExamResult(
          ExamResultId("4"),
          ExamId("exam1"),
          Score(40),
          StudentId("student4"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        )
      )

      val evaluations = evaluator.evaluate(exam, results)

      evaluations(results(0)) mustBe Evaluation.Failed
      evaluations(results(1)) mustBe Evaluation.Passed
      evaluations(results(2)) mustBe Evaluation.GoodJob
      evaluations(results(3)) mustBe Evaluation.Excellent
    }

    "handle an empty list of results" in {
      val evaluator = new QuartileEvaluatorImpl

      val exam = Exam(
        ExamId("exam1"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val results = Seq.empty[ExamResult]

      val evaluations = evaluator.evaluate(exam, results)

      evaluations mustBe empty
    }

    "correctly evaluate when all scores are the same" in {
      val evaluator = new QuartileEvaluatorImpl

      val exam = Exam(
        ExamId("exam1"),
        Subject.Math,
        DueDate(ZonedDateTime.now().minusDays(1)),
        EvaluationStatus.NotEvaluated,
        CreatedAt(ZonedDateTime.now().minusDays(10)),
        UpdatedAt(ZonedDateTime.now().minusDays(1))
      )

      val results = Seq(
        ExamResult(
          ExamResultId("1"),
          ExamId("exam1"),
          Score(50),
          StudentId("student1"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        ),
        ExamResult(
          ExamResultId("2"),
          ExamId("exam1"),
          Score(50),
          StudentId("student2"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        ),
        ExamResult(
          ExamResultId("3"),
          ExamId("exam1"),
          Score(50),
          StudentId("student3"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        ),
        ExamResult(
          ExamResultId("4"),
          ExamId("exam1"),
          Score(50),
          StudentId("student4"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        )
      )

      val evaluations = evaluator.evaluate(exam, results)

      evaluations.values.forall(_ == Evaluation.Excellent) mustBe true
    }
  }
}
