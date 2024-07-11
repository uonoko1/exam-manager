package domain.exam.entity

import domain.exam.valueObject._
import domain.utils.dateTime._

case class Exam(
    examId: ExamId,
    subject: Subject,
    dueDate: DueDate,
    evaluationStatus: EvaluationStatus,
    createdAt: CreatedAt,
    updatedAt: UpdatedAt
)
