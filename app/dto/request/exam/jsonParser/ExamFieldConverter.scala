package dto.request.exam.jsonParser

import javax.inject._
import dto.request.exam.valueObject._
import dto.utils.dateTime.{CreatedAtRequestConverter, UpdatedAtRequestConverter}
import play.api.libs.json.JsValue
import dto.request.utils.JsonFieldParser

@Singleton
class ExamFieldConverter @Inject() (
    examIdRequestConverter: ExamIdRequestConverter
) {

  private def createParseList(
      requestBody: Map[String, String]
  ): List[Either[String, Any]] = List(
    requestBody.get("examId").map(examIdRequestConverter.validateAndCreate),
    requestBody.get("subject").map(SubjectRequestConverter.validateAndCreate),
    requestBody.get("dueDate").map(DueDateRequestConverter.validateAndCreate),
    requestBody
      .get("evaluationStatus")
      .map(EvaluationStatusRequestConverter.validateAndCreate),
    requestBody
      .get("createdAt")
      .map(CreatedAtRequestConverter.validateAndCreate),
    requestBody
      .get("updatedAt")
      .map(UpdatedAtRequestConverter.validateAndCreate)
  ).flatten

  def convertAndValidate(json: JsValue): Either[String, Tuple] = {
    JsonFieldParser.extractFields(json, createParseList).map { parsedFields =>
      JsonFieldParser.toTuple(parsedFields)
    }
  }
}
