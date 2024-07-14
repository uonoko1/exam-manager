package domain.evaluationPeriodProvider.impl

import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek

class WeeklyEvaluationPeriodProvider extends EvaluationPeriodProvider {
  override def getEvaluationPeriod: (ZonedDateTime, ZonedDateTime) = {
    val now = ZonedDateTime.now()
    val startOfWeek = now
      .minusWeeks(1)
      .toLocalDate
      .`with`(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
      .atStartOfDay(now.getZone)
    val endOfWeek = now
      .minusWeeks(1)
      .toLocalDate
      .`with`(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
      .atTime(23, 59, 59, 999999999)
      .atZone(now.getZone)
    (startOfWeek, endOfWeek)
  }
}
