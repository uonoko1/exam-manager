package dto.request.utils.jsonFieldParser.impl

import javax.inject.Singleton
import play.api.libs.json.JsValue
import dto.request.utils.jsonFieldParser.`trait`.JsonFieldParser

@Singleton
class JsonFieldParserImpl extends JsonFieldParser {

  override def extractFields(
      json: JsValue,
      createParseList: Map[String, String] => List[Either[String, Any]]
  ): Either[String, List[Any]] = {
    json.asOpt[Map[String, String]] match {
      case Some(requestBody) =>
        val parseResults = createParseList(requestBody)
        val errors = parseResults.collect { case Left(error) => error }
        if (errors.nonEmpty) {
          Left(errors.mkString(", "))
        } else {
          Right(parseResults.collect { case Right(value) => value })
        }
      case None =>
        Left("Invalid JSON format")
    }
  }

  override def toTuple(
      validatedFields: List[Any]
  ): Tuple = {
    validatedFields.foldRight[Tuple](EmptyTuple)(_ *: _)
  }
}
