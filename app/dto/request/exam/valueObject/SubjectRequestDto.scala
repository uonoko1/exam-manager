package dto.request.exam.valueObject

enum SubjectRequestDto(val value: String) {
  case Math extends SubjectRequestDto("Math")
  case Science extends SubjectRequestDto("Science")
  case History extends SubjectRequestDto("History")
  case English extends SubjectRequestDto("English")
}

object SubjectRequestDto {

  def create(value: String): Either[String, SubjectRequestDto] =
    if (value.isEmpty) {
      Left("Subject cannot be empty")
    } else {
      value match {
        case "Math"    => Right(SubjectRequestDto.Math)
        case "Science" => Right(SubjectRequestDto.Science)
        case "History" => Right(SubjectRequestDto.History)
        case "English" => Right(SubjectRequestDto.English)
        case _         => Left("Invalid subject format")
      }
    }
}
