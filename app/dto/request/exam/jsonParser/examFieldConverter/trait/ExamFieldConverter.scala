package dto.request.exam.jsonParser.examFieldConverter.`trait`

import play.api.libs.json.JsValue

trait ExamFieldConverter {
  def convertAndValidate(json: JsValue): Either[String, Tuple]
}
