package dto.infrastructure.examResult.valueObject

import domain.examResult.valueObject.StudentId

case class StudentIdDto(value: String)

object StudentIdDto {
  def fromDomain(studentId: StudentId): StudentIdDto = StudentIdDto(
    studentId.value
  )
  def toDomain(dto: StudentIdDto): StudentId = StudentId.create(dto.value)
}
