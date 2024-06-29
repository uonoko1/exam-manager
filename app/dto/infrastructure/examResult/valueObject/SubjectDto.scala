package dto.infrastructure.examResult.valueObject

import domain.examResult.valueObject.Subject

case class SubjectDto(value: String)

object SubjectDto {
  def fromDomain(subejct: Subject): SubjectDto = SubjectDto(subejct.value)
  def toDomain(dto: SubjectDto): Either[String, Subject] =
    Subject.create(dto.value)
}
