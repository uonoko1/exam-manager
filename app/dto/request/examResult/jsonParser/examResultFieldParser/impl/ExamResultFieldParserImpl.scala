package dto.request.examResult.jsonParser.examResultFieldParser.impl

import javax.inject._
import dto.request.exam.valueObject._
import dto.request.examResult.valueObject._
import dto.request.utils.dateTime._
import dto.utils.dateTime.createdAtRequestConverter.`trait`.CreatedAtRequestConverter
import dto.utils.dateTime.updatedAtRequestConverter.`trait`.UpdatedAtRequestConverter
import dto.request.examResult.jsonParser.examResultFieldParser.`trait`.ExamResultFieldParser
import dto.request.utils.jsonFieldParser.`trait`.JsonFieldParser
import play.api.libs.json.JsValue

@Singleton
class ExamResultFieldParserImpl @Inject() (
    examResultIdRequestDtoFactory: ExamResultIdRequestDtoFactory,
    examIdRequestDtoFactory: ExamIdRequestDtoFactory,
    studentIdRequestDtoFactory: StudentIdRequestDtoFactory,
    createdAtRequestFactory: CreatedAtRequestFactory,
    updatedAtRequestFactory: UpdatedAtRequestFactory,
    jsonFieldParser: JsonFieldParser
) extends ExamResultFieldParser {

  private def createParseList(
      requestBody: Map[String, String]
  ): List[Either[String, Any]] = List(
    requestBody
      .get("examResultId")
      .map(examResultIdRequestDtoFactory.create),
    requestBody
      .get("examId")
      .map(examIdRequestDtoFactory.create),
    requestBody
      .get("subject")
      .map(SubjectRequestDto.create),
    requestBody
      .get("score")
      .map(ScoreRequestDto.create),
    requestBody
      .get("studentId")
      .map(studentIdRequestDtoFactory.create),
    requestBody
      .get("evaluation")
      .map(EvaluationRequestDto.create),
    requestBody
      .get("createdAt")
      .map(createdAtRequestFactory.create),
    requestBody
      .get("updatedAt")
      .map(updatedAtRequestFactory.create)
  ).flatten

  override def parse(json: JsValue): Either[String, Tuple] =
    jsonFieldParser.extractFields(json, createParseList).map { parsedFields =>
      jsonFieldParser.toTuple(parsedFields)
    }
}
