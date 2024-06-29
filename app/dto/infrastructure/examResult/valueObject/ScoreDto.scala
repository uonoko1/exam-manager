package dto.infrastructure.examResult.valueObject

import domain.examResult.valueObject.Score

case class ScoreDto(value: Int)

object ScoreDto {
  def fromDomain(score: Score): ScoreDto = ScoreDto(score.value)
  def toDomain(dto: ScoreDto): Score = Score(dto.value)
}
