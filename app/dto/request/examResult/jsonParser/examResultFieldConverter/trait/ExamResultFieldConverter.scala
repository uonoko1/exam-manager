package dto.request.examResult.jsonParser.examResultFieldConverter.`trait`

import play.api.libs.json.JsValue

trait ExamResultFieldConverter {
  def convertAndValidate(json: JsValue): Either[String, Tuple]
}
