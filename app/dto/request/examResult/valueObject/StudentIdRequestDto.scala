package dto.request.examResult.valueObject

class StudentIdRequestDto(value: String)

object StudentIdRequestDto {
  def create(value: String): StudentIdRequestDto =
    new StudentIdRequestDto(value)
}
