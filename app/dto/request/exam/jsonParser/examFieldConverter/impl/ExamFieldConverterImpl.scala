package dto.request.exam.jsonParser.examFieldConverter.impl

import javax.inject._
import dto.request.exam.valueObject._
import dto.request.utils.dateTime._
import dto.utils.dateTime.createdAtRequestConverter.`trait`.CreatedAtRequestConverter
import dto.utils.dateTime.updatedAtRequestConverter.`trait`.UpdatedAtRequestConverter
import dto.request.exam.jsonParser.examFieldConverter.`trait`.ExamFieldConverter
import dto.request.utils.jsonFieldParser.`trait`.JsonFieldParser
import play.api.libs.json.JsValue

@Singleton
class ExamFieldConverterImpl @Inject() (
    examIdRequestDtoFactory: ExamIdRequestDtoFactory,
    dueDateRequestFactory: DueDateRequestFactory,
    createdAtRequestFactory: CreatedAtRequestFactory,
    updatedAtRequestFactory: UpdatedAtRequestFactory,
    jsonFieldParser: JsonFieldParser
) extends ExamFieldConverter {

  private def createParseList(
      requestBody: Map[String, String]
  ): List[Either[String, Any]] = List(
    requestBody
      .get("examId")
      .map(examIdRequestDtoFactory.create),
    requestBody
      .get("subject")
      .map(SubjectRequestDto.create),
    requestBody
      .get("dueDate")
      .map(dueDateRequestFactory.create),
    requestBody
      .get("evaluationStatus")
      .map(EvaluationStatusRequestDto.create),
    requestBody
      .get("createdAt")
      .map(createdAtRequestFactory.create),
    requestBody
      .get("updatedAt")
      .map(updatedAtRequestFactory.create)
  ).flatten

  override def convertAndValidate(json: JsValue): Either[String, Tuple] =
    jsonFieldParser.extractFields(json, createParseList).map { parsedFields =>
      jsonFieldParser.toTuple(parsedFields)
    }
}
