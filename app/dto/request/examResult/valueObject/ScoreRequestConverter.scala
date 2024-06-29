package dto.request.examResult.valueObject

import domain.examResult.valueObject.Score
import scala.util.{Try, Success, Failure}

object ScoreRequestConverter {
  def validateAndCreate(value: String): Either[String, Score] = {
    if (value.isEmpty) {
      Left("Score cannot be empty")
    } else {
      Try(value.toInt) match {
        case Success(intValue) => Score.create(intValue)
        case Failure(_)        => Left("Invalid score format")
      }
    }
  }
}
