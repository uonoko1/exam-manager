package dto.response.examResult.entity

import domain.examResult.entity.ExamResult
import play.api.libs.json.{Json, Writes}

case class ExamResultResponseDto(
    examResultId: String,
    examId: String,
    score: Int,
    studentId: String,
    evaluation: String,
    createdAt: String,
    updatedAt: String
)

object ExamResultResponseDto {
  implicit val writes: Writes[ExamResultResponseDto] =
    Json.writes[ExamResultResponseDto]

  def fromDomain(examResult: ExamResult): ExamResultResponseDto =
    ExamResultResponseDto(
      examResultId = examResult.examResultId.value,
      examId = examResult.examId.value,
      score = examResult.score.value,
      studentId = examResult.studentId.value,
      evaluation = examResult.evaluation.toString,
      createdAt = examResult.createdAt.value.toString,
      updatedAt = examResult.updatedAt.value.toString
    )
}
