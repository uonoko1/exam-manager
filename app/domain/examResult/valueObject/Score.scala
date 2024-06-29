package domain.examResult.valueObject

case class Score(value: Int) extends AnyVal

object Score {
  def create(value: Int): Either[String, Score] = {
    if (value >= 0 && value <= 100) Right(Score(value))
    else Left("Score must be between 0 and 100")
  }
}
