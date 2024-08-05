package domain.evaluationPeriodProvider.`trait`

import java.time.ZonedDateTime

trait EvaluationPeriodProvider {
  def getEvaluationPeriod(now: ZonedDateTime): (ZonedDateTime, ZonedDateTime)
}
