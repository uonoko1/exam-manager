package dto.infrastructure.exam.valueObject

import domain.exam.valueObject.Subject

case class SubjectDto(value: String)

object SubjectDto {
  def fromDomain(subejct: Subject): SubjectDto = SubjectDto(subejct.value)
  def toDomain(dto: SubjectDto): Either[String, Subject] =
    Subject.create(dto.value)
}
