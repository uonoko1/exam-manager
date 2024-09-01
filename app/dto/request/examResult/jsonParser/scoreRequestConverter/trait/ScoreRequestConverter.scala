package dto.request.examResult.jsonParser.scoreRequestConverter.`trait`

import domain.examResult.valueObject.Score

trait ScoreRequestConverter {
  def validateAndCreate(value: String): Either[String, Score]
}
