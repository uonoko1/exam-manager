package domain.evaluationPeriodProvider.impl

import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek

class WeeklyEvaluationPeriodProvider extends EvaluationPeriodProvider {
  override def getEvaluationPeriod: (ZonedDateTime, ZonedDateTime) = {
    val now = ZonedDateTime.now()
    val startOfWeek = now.toLocalDate
      .`with`(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
      .atStartOfDay(now.getZone)
    val endOfWeek = now
    (startOfWeek, endOfWeek)
  }
}
