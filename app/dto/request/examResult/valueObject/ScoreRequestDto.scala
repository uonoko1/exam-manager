package dto.request.examResult.valueObject

import scala.util.{Try, Success, Failure}

class ScoreRequestDto(value: Int)

object ScoreRequestDto {
  def create(value: String): Either[String, ScoreRequestDto] =
    Try(value.toInt) match {
      case Success(intValue) => Right(new ScoreRequestDto(intValue))
      case Failure(_)        => Left("Invalid score format")
    }
}
