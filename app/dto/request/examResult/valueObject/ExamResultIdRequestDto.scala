package dto.request.examResult.valueObject

class ExamResultIdRequestDto(value: String)

object ExamResultIdRequestDto {
  def create(value: String): ExamResultIdRequestDto =
    new ExamResultIdRequestDto(value)
}
