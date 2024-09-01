package dto.request.examResult.valueObject

import scala.util.{ Failure, Success, Try }

case class ScoreRequestDto(val value: Int)

object ScoreRequestDto {
  def create(value: String): Either[String, ScoreRequestDto] =
    Try(value.toInt) match {
      case Success(intValue) => Right(ScoreRequestDto(intValue))
      case Failure(_)        => Left("Invalid score format")
    }
}
