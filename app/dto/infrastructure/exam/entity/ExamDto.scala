package dto.infrastructure.exam.entity

import domain.exam.entity.Exam
import domain.exam.valueObject._
import domain.utils.dateTime.{ CreatedAt, UpdatedAt }
import dto.infrastructure.exam.valueObject._
import dto.infrastructure.utils.dateTime.{ CreatedAtDto, UpdatedAtDto }
import java.time.ZonedDateTime

case class ExamDto(
    examIdDto: String,
    subjectDto: String,
    dueDateDto: ZonedDateTime,
    evaluationStatusDto: String,
    createdAtDto: ZonedDateTime,
    updatedAtDto: ZonedDateTime
)

object ExamDto {
  def fromDomain(exam: Exam): ExamDto = ExamDto(
    examIdDto = exam.examId.value,
    subjectDto = exam.subject.value,
    dueDateDto = exam.dueDate.value,
    evaluationStatusDto = exam.evaluationStatus.value,
    createdAtDto = exam.createdAt.value,
    updatedAtDto = exam.updatedAt.value
  )

  def toDomain(dto: ExamDto): Either[String, Exam] =
    for {
      examId <- ExamId.create(dto.examIdDto)
      subject <- Subject.create(dto.subjectDto)
      dueDate <- Right(DueDate.create(dto.dueDateDto))
      evaluationStatus <- EvaluationStatus.create(dto.evaluationStatusDto)
      createdAt <- Right(CreatedAt.create(dto.createdAtDto))
      updatedAt <- Right(UpdatedAt.create(dto.updatedAtDto))
    } yield Exam(
      examId = examId,
      subject = subject,
      dueDate = dueDate,
      evaluationStatus = evaluationStatus,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
}
