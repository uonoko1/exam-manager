package dto.request.examResult.jsonParser

import dto.request.exam.valueObject._
import dto.request.examResult.valueObject._
import dto.utils.dateTime.{CreatedAtRequestConverter, UpdatedAtRequestConverter}
import dto.request.utils.JsonFieldParser
import play.api.libs.json.JsValue

object ExamResultFieldConverter {

  private def createParseList(
      requestBody: Map[String, String]
  ): List[Either[String, Any]] = List(
    requestBody
      .get("examResultId")
      .map(ExamResultIdRequestConverter.validateAndCreate),
    requestBody.get("examId").map(ExamIdRequestConverter.validateAndCreate),
    requestBody.get("score").map(ScoreRequestConverter.validateAndCreate),
    requestBody
      .get("studentId")
      .map(StudentIdRequestConverter.validateAndCreate),
    requestBody
      .get("evaluation")
      .map(EvaluationRequestConverter.validateAndCreate),
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
