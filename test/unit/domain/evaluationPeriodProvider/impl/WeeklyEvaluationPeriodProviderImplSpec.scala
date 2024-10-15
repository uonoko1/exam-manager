package domain.evaluationPeriodProvider.impl

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import java.time.{ ZoneId, ZonedDateTime }
import utils.CustomPatience

class WeeklyEvaluationPeriodProviderImplSpec
    extends AnyWordSpec
    with Matchers
    with CustomPatience {

  "WeeklyEvaluationPeriodProvider" should {

    "correctly calculate the evaluation period for Tokyo time zone at the end of the week" in {
      val provider = new WeeklyEvaluationPeriodProviderImpl
      val now = ZonedDateTime.parse("2024-07-21T10:15:30+09:00[Asia/Tokyo]")
      val (startOfWeek, endOfWeek) = provider.getEvaluationPeriod(now)

      val expectedStartOfWeek =
        ZonedDateTime.parse("2024-07-08T00:00:00+09:00[Asia/Tokyo]")
      val expectedEndOfWeek =
        ZonedDateTime.parse("2024-07-14T23:59:59.999999999+09:00[Asia/Tokyo]")

      startOfWeek mustBe expectedStartOfWeek
      endOfWeek mustBe expectedEndOfWeek
    }

    "correctly calculate the evaluation period for Tokyo time zone at the start of the week" in {
      val provider = new WeeklyEvaluationPeriodProviderImpl
      val now = ZonedDateTime.parse("2024-07-15T00:10:00+09:00[Asia/Tokyo]")
      val (startOfWeek, endOfWeek) = provider.getEvaluationPeriod(now)

      val expectedStartOfWeek =
        ZonedDateTime.parse("2024-07-08T00:00:00+09:00[Asia/Tokyo]")
      val expectedEndOfWeek =
        ZonedDateTime.parse("2024-07-14T23:59:59.999999999+09:00[Asia/Tokyo]")

      startOfWeek mustBe expectedStartOfWeek
      endOfWeek mustBe expectedEndOfWeek
    }

    "correctly calculate the evaluation period for London time zone at the end of the week" in {
      val provider = new WeeklyEvaluationPeriodProviderImpl
      val now = ZonedDateTime.parse("2024-07-21T01:15:30+01:00[Europe/London]")
      val (startOfWeek, endOfWeek) = provider.getEvaluationPeriod(now)

      val expectedStartOfWeek =
        ZonedDateTime.parse("2024-07-08T00:00:00+01:00[Europe/London]")
      val expectedEndOfWeek = ZonedDateTime.parse(
        "2024-07-14T23:59:59.999999999+01:00[Europe/London]"
      )

      startOfWeek mustBe expectedStartOfWeek
      endOfWeek mustBe expectedEndOfWeek
    }

    "correctly calculate the evaluation period for London time zone at the start of the week" in {
      val provider = new WeeklyEvaluationPeriodProviderImpl
      val now = ZonedDateTime.parse("2024-07-15T00:10:00+01:00[Europe/London]")
      val (startOfWeek, endOfWeek) = provider.getEvaluationPeriod(now)

      val expectedStartOfWeek =
        ZonedDateTime.parse("2024-07-08T00:00:00+01:00[Europe/London]")
      val expectedEndOfWeek = ZonedDateTime.parse(
        "2024-07-14T23:59:59.999999999+01:00[Europe/London]"
      )

      startOfWeek mustBe expectedStartOfWeek
      endOfWeek mustBe expectedEndOfWeek
    }

    "correctly calculate the evaluation period for New York time zone at the end of the week" in {
      val provider = new WeeklyEvaluationPeriodProviderImpl
      val now =
        ZonedDateTime.parse("2024-07-20T20:15:30-04:00[America/New_York]")
      val (startOfWeek, endOfWeek) = provider.getEvaluationPeriod(now)

      val expectedStartOfWeek =
        ZonedDateTime.parse("2024-07-08T00:00:00-04:00[America/New_York]")
      val expectedEndOfWeek = ZonedDateTime.parse(
        "2024-07-14T23:59:59.999999999-04:00[America/New_York]"
      )

      startOfWeek mustBe expectedStartOfWeek
      endOfWeek mustBe expectedEndOfWeek
    }

    "correctly calculate the evaluation period for New York time zone at the start of the week" in {
      val provider = new WeeklyEvaluationPeriodProviderImpl
      val now =
        ZonedDateTime.parse("2024-07-15T00:10:00-04:00[America/New_York]")
      val (startOfWeek, endOfWeek) = provider.getEvaluationPeriod(now)

      val expectedStartOfWeek =
        ZonedDateTime.parse("2024-07-08T00:00:00-04:00[America/New_York]")
      val expectedEndOfWeek = ZonedDateTime.parse(
        "2024-07-14T23:59:59.999999999-04:00[America/New_York]"
      )

      startOfWeek mustBe expectedStartOfWeek
      endOfWeek mustBe expectedEndOfWeek
    }
  }
}
