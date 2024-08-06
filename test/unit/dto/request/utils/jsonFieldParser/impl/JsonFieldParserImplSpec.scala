package dto.request.utils.jsonFieldParser.impl

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import utils.CustomPatience
import play.api.libs.json.Json

class JsonFieldParserImplSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with CustomPatience {

  val parser = new JsonFieldParserImpl

  def createParseList(
      requestBody: Map[String, String]
  ): List[Either[String, Any]] = {
    List(
      requestBody.get("field1").map(Right(_)),
      requestBody.get("field2").map(Right(_)),
      requestBody.get("field3").map(Right(_)),
      requestBody.get("field4").map(Left(_)),
      requestBody.get("field5").map(Left(_))
    ).flatten
  }

  "JsonFieldParserImpl" should {
    "return a tuple of extracted fields when given JSON as input contains parseList's fields" in {
      val json = Json.parse(
        """
          |{
          |  "field1": "value1",
          |  "field2": "value2",
          |  "field3": "value3"
          |}
          |""".stripMargin
      )

      val result = parser.extractFields(json, createParseList)

      result match {
        case Right(validatedFields) =>
          parser.toTuple(validatedFields) mustBe ("value1", "value2", "value3")
        case Left(error) =>
          fail(s"Expected Right but got Left with error: $error")
      }
    }

    "return a Invalid JSON format error when the given JSON format as input cannot be converted to the expected type" in {
      val json = Json.parse(
        """
          |{
          |  "field1": "value1",
          |  "field2": 123,
          |  "field3": "value3"
          |}
          |""".stripMargin
      )

      val result = parser.extractFields(json, createParseList)

      result mustBe Left("Invalid JSON format")
    }

    "return errors when conversion errors occured" in {
      val json = Json.parse(
        """
          |{
          |  "field1": "value1",
          |  "field4": "value4",
          |  "field5": "value5"
          |}
          |""".stripMargin
      )

      val result = parser.extractFields(json, createParseList)

      result mustBe Left("value4, value5")
    }
  }
}
