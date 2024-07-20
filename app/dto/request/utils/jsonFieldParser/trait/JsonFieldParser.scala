package dto.request.utils.jsonFieldParser.`trait`

import play.api.libs.json.JsValue

trait JsonFieldParser {
  def extractFields(
      json: JsValue,
      createParseList: Map[String, String] => List[Either[String, Any]]
  ): Either[String, List[Any]]

  def toTuple(
      validatedFields: List[Any]
  ): Tuple
}
