package dto.infrastructure.examResult.entity

import domain.examResult.entity.ExamResult
import domain.examResult.valueObject._
import dto.infrastructure.examResult.valueObject._

case class ExamResultDto(
    examId: String,
    subject: String,
    score: Int,
    studentId: Int,
    evaluation: String
)

object ExamResultDto {
  def fromDomain(examResult: ExamResult): ExamResultDto = ExamResultDto(
    examId = examResult.examId.value,
    subject = examResult.subject.value,
    score = examResult.score.value,
    studentId = examResult.studentId.value,
    evaluation = examResult.evaluation.value
  )

  def toDomain(dto: ExamResultDto): Either[String, ExamResult] = {
    for {
      examId <- Right(ExamId(dto.examId))
      subject <- Subject.create(dto.subject)
      score <- Right(Score(dto.score))
      studentId <- Right(StudentId(dto.studentId))
      evaluation <- Evaluation.create(dto.evaluation)
    } yield ExamResult(
      examId = examId,
      subject = subject,
      score = score,
      studentId = studentId,
      evaluation = evaluation
    )
  }
}
