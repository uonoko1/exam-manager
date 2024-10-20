package dto.request.examResult.jsonParser.examResultFieldParser.`trait`

import play.api.libs.json.JsValue

trait ExamResultFieldParser {
  def parse(json: JsValue): Either[String, Tuple]
}
