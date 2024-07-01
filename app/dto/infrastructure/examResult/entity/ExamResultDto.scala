package dto.infrastructure.examResult.entity

import domain.examResult.entity.ExamResult
import domain.examResult.valueObject._
import domain.exam.valueObject._
import dto.infrastructure.examResult.valueObject._
import java.time.ZonedDateTime

case class ExamResultDto(
    examResultId: ExamResultIdDto,
    examId: ExamIdDto,
    score: ScoreDto,
    studentId: StudentIdDto,
    evaluation: EvaluationDto,
    createdAt: CreatedAtDto,
    updatedAt: UpdatedAtDto
)

object ExamResultDto {
  def fromDomain(examResult: ExamResult): ExamResultDto = ExamResultDto(
    examResultId = examResult.examResultId.value,
    examId = examResult.examId.value,
    score = examResult.score.value,
    studentId = examResult.studentId.value,
    evaluation = examResult.evaluation.value,
    createdAt = examResult.createdAt.value,
    updatedAt = examResult.updatedAt.value
  )

  def toDomain(dto: ExamResultDto): Either[String, ExamResult] = {
    for {
      examResultId <- Right(ExamResultId.create(dto.examResultId))
      examId <- Right(ExamId.create(dto.examId))
      score <- Score.create(dto.score)
      studentId <- Right(StudentId.create(dto.studentId))
      evaluation <- Evaluation.create(dto.evaluation)
      createdAt <- Right(CreatedAt.create(dto.createdAt))
      updatedAt <- Right(UpdatedAt.create(dto.updatedAt))
    } yield ExamResult(
      examResultId = examResultId,
      examId = examId,
      score = score,
      studentId = studentId,
      evaluation = evaluation,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }
}
