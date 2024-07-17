package dto.request.examResult.jsonParser

import javax.inject._
import dto.request.exam.valueObject.ExamIdRequestConverter
import dto.request.examResult.valueObject.StudentIdRequestConverter
import dto.request.exam.valueObject._
import dto.request.examResult.valueObject._
import dto.utils.dateTime.{CreatedAtRequestConverter, UpdatedAtRequestConverter}
import dto.request.utils.JsonFieldParser
import play.api.libs.json.JsValue

@Singleton
class ExamResultFieldConverter @Inject() (
    examIdRequestConverter: ExamIdRequestConverter,
    studentIdRequestConverter: StudentIdRequestConverter
) {

  private def createParseList(
      requestBody: Map[String, String]
  ): List[Either[String, Any]] = List(
    requestBody
      .get("examResultId")
      .map(ExamResultIdRequestConverter.validateAndCreate),
    requestBody.get("examId").map(examIdRequestConverter.validateAndCreate),
    requestBody
      .get("subject")
      .map(SubjectRequestConverter.validateAndCreate),
    requestBody.get("score").map(ScoreRequestConverter.validateAndCreate),
    requestBody
      .get("studentId")
      .map(studentIdRequestConverter.validateAndCreate),
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
