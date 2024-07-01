package dto.infrastructure.exam.entity

import dto.infrastructure.exam.valueObject._
import dto.infrastructure.utils._
import domain.exam.entity

case class ExamDto(
    examIdDto: ExamIdDto,
    subjectDto: SubjectDto,
    dueDateDto: DueDateDto,
    evaluationStatusDto: EvaluationStatusDto,
    createdAtDto: CreatedAtDto,
    updatedAtDto: UpdatedAtDto
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

  def toDomain(dto: ExamDto): Either[String, Exam] = {
    for {
      examId <- Right(ExamId.create(dto.examIdDto))
      subject <- Subject.create(dto.subject)
      dueDate <- Right(DueDate.create(dto.dueDateDto))
      evaluationStatus <- EvaluationStatus.crate(dto.evaluationStatusDto)
      createdAt <- Right(CreatedAt.create(dto.createdAt))
      updatedAt <- Right(UpdatedAt.create(dto.updatedAt))
    } yield Exam(
      examId = examId,
      subject = subject,
      dueDate = dueDate,
      evaluationStatus = evaluationStatus,
      createdAt = createdAt,
      uodatedAt = updatedAt
    )
  }
}
