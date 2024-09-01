package dto.infrastructure.examResult.entity

import domain.examResult.entity.ExamResult
import domain.exam.valueObject._
import domain.examResult.valueObject._
import domain.utils.dateTime.{ CreatedAt, UpdatedAt }
import dto.infrastructure.examResult.valueObject._
import dto.infrastructure.exam.valueObject._
import dto.infrastructure.utils.dateTime.{ CreatedAtDto, UpdatedAtDto }
import java.time.ZonedDateTime

case class ExamResultDto(
    examResultId: String,
    examId: String,
    score: Int,
    studentId: String,
    evaluation: String,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
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

  def toDomain(dto: ExamResultDto): Either[String, ExamResult] =
    for {
      examResultId <- ExamResultId.create(dto.examResultId)
      examId <- ExamId.create(dto.examId)
      score <- Score.create(dto.score)
      studentId <- StudentId.create(dto.studentId)
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
