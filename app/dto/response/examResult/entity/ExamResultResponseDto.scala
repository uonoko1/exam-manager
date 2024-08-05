package dto.response.examResult.entity

import domain.examResult.entity.ExamResult
import play.api.libs.json.{Json, Writes}
import play.api.libs.json.JsValue

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
    new Writes[ExamResultResponseDto] {
      def writes(dto: ExamResultResponseDto): JsValue = Json.obj(
        "examResultId" -> dto.examResultId,
        "examId" -> dto.examId,
        "score" -> dto.score,
        "studentId" -> dto.studentId,
        "evaluation" -> dto.evaluation,
        "createdAt" -> dto.createdAt,
        "updatedAt" -> dto.updatedAt
      )
    }

  def fromDomain(examResult: ExamResult): ExamResultResponseDto =
    ExamResultResponseDto(
      examResultId = examResult.examResultId.value,
      examId = examResult.examId.value,
      score = examResult.score.value,
      studentId = examResult.studentId.value,
      evaluation = examResult.evaluation.value,
      createdAt = examResult.createdAt.value.toString,
      updatedAt = examResult.updatedAt.value.toString
    )
}
