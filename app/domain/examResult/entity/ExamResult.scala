package domain.examResult.entity

import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.utils.dateTime._

case class ExamResult(
    examResultId: ExamResultId,
    examId: ExamId,
    score: Score,
    studentId: StudentId,
    evaluation: Evaluation = Evaluation.NotEvaluated,
    createdAt: CreatedAt,
    updatedAt: UpdatedAt
)
