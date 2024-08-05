package dto.utils.dateTime.dateTimeConverter.impl

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import utils.CustomPatience
import java.time._

class DateTimeConverterImplSpec
    extends AnyWordSpec
    with Matchers
    with CustomPatience {
  val converter = new DateTimeConverterImpl

  "DateTimeConverterImpl" should {
    "successfully convert a valid date-time string to ZonedDateTime" in {
      val zonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/London"))
      val validDateTimeString = zonedDateTime.toString
      val result = converter.stringToZonedDateTime(validDateTimeString)
      result mustBe Right(zonedDateTime)
    }

    "return an error message for an invalid date-time string" in {
      val invalidDateTimeString = "invalid-date-time"
      val result = converter.stringToZonedDateTime(invalidDateTimeString)
      result mustBe a[Left[String, ZonedDateTime]]
      result match {
        case Left(error) => error must include("Invalid input. Error:")
        case _           => fail("Expected Left but got Right")
      }
    }

    "handle an empty date-time string" in {
      val emptyDateTimeString = ""
      val result = converter.stringToZonedDateTime(emptyDateTimeString)
      result mustBe a[Left[String, ZonedDateTime]]
      result match {
        case Left(error) => error must include("Invalid input. Error:")
        case _           => fail("Expected Left but got Right")
      }
    }

    "handle null date-time string" in {
      val nullDateTimeString: String = null
      val result = converter.stringToZonedDateTime(nullDateTimeString)
      result mustBe a[Left[String, ZonedDateTime]]
      result match {
        case Left(error) => error must include("Invalid input. Error:")
        case _           => fail("Expected Left but got Right")
      }
    }

    "return an error message for a LocalDateTime string" in {
      val localDateTime = LocalDateTime.now()
      val localDateTimeString = localDateTime.toString
      val result = converter.stringToZonedDateTime(localDateTimeString)
      result mustBe a[Left[String, ZonedDateTime]]
      result match {
        case Left(error) => error must include("Invalid input. Error:")
        case _           => fail("Expected Left but got Right")
      }
    }

    "return an error message for a LocalDate string" in {
      val localDate = LocalDate.now()
      val localDateString = localDate.toString
      val result = converter.stringToZonedDateTime(localDateString)
      result mustBe a[Left[String, ZonedDateTime]]
      result match {
        case Left(error) => error must include("Invalid input. Error:")
        case _           => fail("Expected Left but got Right")
      }
    }

    "return an error message for a LocalTime string" in {
      val localTime = LocalTime.now()
      val localTimeString = localTime.toString
      val result = converter.stringToZonedDateTime(localTimeString)
      result mustBe a[Left[String, ZonedDateTime]]
      result match {
        case Left(error) => error must include("Invalid input. Error:")
        case _           => fail("Expected Left but got Right")
      }
    }

    "return an error message for an OffsetDateTime string" in {
      val offsetDateTime = OffsetDateTime.now()
      val offsetDateTimeString = offsetDateTime.toString
      val result = converter.stringToZonedDateTime(offsetDateTimeString)
      result mustBe a[Left[String, ZonedDateTime]]
      result match {
        case Left(error) =>
          error mustBe ("Invalid input. Error: OffsetDateTime format is not allowed.")
        case _ => fail("Expected Left but got Right")
      }
    }

    "return an error message for an Instant string" in {
      val instant = Instant.now()
      val instantString = instant.toString
      val result = converter.stringToZonedDateTime(instantString)
      result mustBe a[Left[String, ZonedDateTime]]
      result match {
        case Left(error) =>
          error mustBe (
            "Invalid input. Error: Instant format is not allowed."
          )
        case _ => fail("Expected Left but got Right")
      }
    }
  }
}
