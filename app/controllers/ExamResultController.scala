package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import usecases.examResult.ExamResultUsecase
import scala.util.{Try, Success, Failure}
import dto.request.examResult.jsonParser.examResultFieldConverter.`trait`.ExamResultFieldConverter
import play.api.libs.json.JsValue
import views.html.defaultpages.badRequest
import domain.examResult.valueObject._
import domain.exam.valueObject._
import dto.infrastructure.exam.valueObject.ExamIdDto
import dto.request.exam.valueObject.examIdRequestConverter.`trait`.ExamIdRequestConverter
import dto.response.examResult.entity.ExamResultResponseDto
import play.api.libs.json.Json

@Singleton
class ExamResultController @Inject() (
    cc: ControllerComponents,
    examResultUsecase: ExamResultUsecase,
    examResultFieldConverter: ExamResultFieldConverter,
    examIdRequestConverter: ExamIdRequestConverter
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def saveExamResult: Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      examResultFieldConverter.convertAndValidate(request.body) match {
        case Right(
              (
                examId: ExamId,
                subject: Subject,
                score: Score,
                studentId: StudentId
              )
            ) =>
          examResultUsecase
            .saveExamResult(examId, subject, score, studentId)
            .map {
              case Right(savedExamResult) =>
                Ok(
                  Json.toJson(ExamResultResponseDto.fromDomain(savedExamResult))
                )
              case Left(error) =>
                BadRequest(s"Failed to save exam result: $error")
            }
            .recover { case ex =>
              InternalServerError(
                s"Failed to save exam result: ${ex.getMessage}"
              )
            }
        case Right(v) =>
          val a = v
          Future.successful(BadRequest("Invalid parameters"))
        case Left(errors) =>
          Future.successful(BadRequest(s"Invalid parameters: $errors"))
      }
  }

  def getExamResult(examId: String): Action[AnyContent] = Action.async {
    implicit request =>
      examIdRequestConverter.validateAndCreate(examId) match {
        case Right(examId) =>
          examResultUsecase
            .findById(examId)
            .map {
              case Right(Some(examResult)) =>
                Ok(Json.toJson(ExamResultResponseDto.fromDomain(examResult)))
              case Right(None) =>
                NotFound(s"Exam result with id $examId not found")
              case Left(error) =>
                BadRequest(s"Failed to fetch exam result: $error")
            }
            .recover { case ex =>
              InternalServerError(
                s"Failed to fetch exam result: ${ex.getMessage}"
              )
            }
        case Left(error) =>
          Future.successful(BadRequest(s"Invalid exam ID: $error"))
      }
  }
}
