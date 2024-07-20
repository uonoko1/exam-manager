package dto.request.examResult.jsonParser.examResultFieldConverter.impl

import javax.inject._
import dto.request.exam.valueObject.examIdRequestConverter.`trait`.ExamIdRequestConverter
import dto.request.exam.valueObject.subjectRequestConverter.`trait`.SubjectRequestConverter
import dto.request.examResult.valueObject.studentIdRequestConverter.`trait`.StudentIdRequestConverter
import dto.request.examResult.valueObject.examResultIdRequestConverter.`trait`.ExamResultIdRequestConverter
import dto.request.examResult.valueObject.scoreRequestConverter.`trait`.ScoreRequestConverter
import dto.request.examResult.valueObject.evaluationRequestConverter.`trait`.EvaluationRequestConverter
import dto.utils.dateTime.createdAtRequestConverter.`trait`.CreatedAtRequestConverter
import dto.utils.dateTime.updatedAtRequestConverter.`trait`.UpdatedAtRequestConverter
import dto.request.examResult.jsonParser.examResultFieldConverter.`trait`.ExamResultFieldConverter
import dto.request.utils.jsonFieldParser.`trait`.JsonFieldParser
import play.api.libs.json.JsValue

@Singleton
class ExamResultFieldConverterImpl @Inject() (
    examIdRequestConverter: ExamIdRequestConverter,
    studentIdRequestConverter: StudentIdRequestConverter,
    examResultIdRequestConverter: ExamResultIdRequestConverter,
    subjectRequestConverter: SubjectRequestConverter,
    scoreRequestConverter: ScoreRequestConverter,
    evaluationRequestConverter: EvaluationRequestConverter,
    createdAtRequestConverter: CreatedAtRequestConverter,
    updatedAtRequestConverter: UpdatedAtRequestConverter,
    jsonFieldParser: JsonFieldParser
) extends ExamResultFieldConverter {

  private def createParseList(
      requestBody: Map[String, String]
  ): List[Either[String, Any]] = List(
    requestBody
      .get("examResultId")
      .map(examResultIdRequestConverter.validateAndCreate),
    requestBody.get("examId").map(examIdRequestConverter.validateAndCreate),
    requestBody
      .get("subject")
      .map(subjectRequestConverter.validateAndCreate),
    requestBody.get("score").map(scoreRequestConverter.validateAndCreate),
    requestBody
      .get("studentId")
      .map(studentIdRequestConverter.validateAndCreate),
    requestBody
      .get("evaluation")
      .map(evaluationRequestConverter.validateAndCreate),
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
