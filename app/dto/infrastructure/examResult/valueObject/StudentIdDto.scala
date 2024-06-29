package dto.infrastructure.examResult.valueObject

import domain.examResult.valueObject.StudentId

case class StudentIdDto(value: Int)

object StudentIdDto {
  def fromDomain(studentId: StudentId): StudentIdDto = StudentIdDto(
    studentId.value
  )
  def toDomain(dto: StudentIdDto): StudentId = StudentId(dto.value)
}
