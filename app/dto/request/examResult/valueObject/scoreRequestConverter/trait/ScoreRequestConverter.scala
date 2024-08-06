package dto.request.examResult.valueObject.scoreRequestConverter.`trait`

import domain.examResult.valueObject.Score

trait ScoreRequestConverter {
  def validateAndCreate(value: String): Either[String, Score]
}
