package dto.request.exam.jsonParser.examFieldConverter.impl

import javax.inject._
import dto.request.exam.valueObject.dueDateRequestConverter.`trait`.DueDateRequestConverter
import dto.request.exam.valueObject.evaluationStatusRequestConverter.`trait`.EvaluationStatusRequestConverter
import dto.request.exam.valueObject.examIdRequestConverter.`trait`.ExamIdRequestConverter
import dto.request.exam.valueObject.subjectRequestConverter.`trait`.SubjectRequestConverter
import dto.utils.dateTime.createdAtRequestConverter.`trait`.CreatedAtRequestConverter
import dto.utils.dateTime.updatedAtRequestConverter.`trait`.UpdatedAtRequestConverter
import dto.request.exam.jsonParser.examFieldConverter.`trait`.ExamFieldConverter
import dto.request.utils.jsonFieldParser.`trait`.JsonFieldParser
import play.api.libs.json.JsValue

@Singleton
class ExamFieldConverterImpl @Inject() (
    examIdRequestConverter: ExamIdRequestConverter,
    subjectRequestConverter: SubjectRequestConverter,
    dueDateRequestConverter: DueDateRequestConverter,
    evaluationStatusRequestConverter: EvaluationStatusRequestConverter,
    createdAtRequestConverter: CreatedAtRequestConverter,
    updatedAtRequestConverter: UpdatedAtRequestConverter,
    jsonFieldParser: JsonFieldParser
) extends ExamFieldConverter {

  private def createParseList(
      requestBody: Map[String, String]
  ): List[Either[String, Any]] = List(
    requestBody.get("examId").map(examIdRequestConverter.validateAndCreate),
    requestBody.get("subject").map(subjectRequestConverter.validateAndCreate),
    requestBody.get("dueDate").map(dueDateRequestConverter.validateAndCreate),
    requestBody
      .get("evaluationStatus")
      .map(evaluationStatusRequestConverter.validateAndCreate),
    requestBody
      .get("createdAt")
      .map(createdAtRequestConverter.validateAndCreate),
    requestBody
      .get("updatedAt")
      .map(updatedAtRequestConverter.validateAndCreate)
  ).flatten

  override def convertAndValidate(json: JsValue): Either[String, Tuple] = {
    jsonFieldParser.extractFields(json, createParseList).map { parsedFields =>
      jsonFieldParser.toTuple(parsedFields)
    }
  }
}
