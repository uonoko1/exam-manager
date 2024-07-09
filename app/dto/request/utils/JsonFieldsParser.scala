package dto.request.utils

import play.api.libs.json.JsValue

object JsonFieldParser {

  def extractFields(
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

  def toTuple(
      validatedFields: List[Any]
  ): Tuple = {
    val filteredFields = validatedFields.collect {
      case Right(value) if value != null => value
    }
    filteredFields.foldRight[Tuple](EmptyTuple)(_ *: _)
  }
}
