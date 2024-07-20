package dto.request.examResult.valueObject.scoreRequestConverter.impl

import domain.examResult.valueObject.Score
import dto.request.examResult.valueObject.scoreRequestConverter.`trait`.ScoreRequestConverter
import javax.inject._
import scala.util.Try

@Singleton
class ScoreRequestConverterImpl @Inject() () extends ScoreRequestConverter {
  override def validateAndCreate(value: String): Either[String, Score] = {
    if (value.isEmpty) {
      Left("Score cannot be empty")
    } else {
      Try(value.toInt) match {
        case scala.util.Success(intValue) => Score.create(intValue)
        case scala.util.Failure(_)        => Left("Invalid score format")
      }
    }
  }
}
