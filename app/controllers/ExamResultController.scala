package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import usecases.ExamResultUsecase
import scala.util.{Try, Success, Failure}
import dto.request.examResult.entity.ExamResultRequestConverter
import play.api.libs.json.JsValue
import views.html.defaultpages.badRequest
import domain.examResult.valueObject._
import usecases.ExamResultUsecase
import dto.infrastructure.examResult.valueObject.ExamIdDto
import dto.request.examResult.valueObject.ExamIdRequestConverter
import play.api.libs.json.Json

@Singleton
class ExamResultController @Inject() (
    cc: ControllerComponents,
    examResultUsecase: ExamResultUsecase
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {
  def saveExamResult: Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      ExamResultRequestConverter.validateAndCreate(
        request.body: JsValue
      ) match {
        case Right((subject: Subject, score: Score, studentId: StudentId)) =>
          examResultUsecase
            .saveExamResult(subject, score, studentId)
            .map { result =>
              Ok(result)
            }
            .recover { case ex =>
              InternalServerError(
                s"Failed to save exam result: ${ex.getMessage}"
              )
            }
        case Right(_) =>
          Future.successful(BadRequest("Invalid parameters"))
        case Left(errors) =>
          Future.successful(BadRequest(s"Invalid parameters: $errors"))
      }
  }

  def getExamResult(examId: String): Action[AnyContent] = Action.async {
    implicit request =>
      ExamIdRequestConverter.validateAndCreate(examId) match {
        case Right(examId) =>
          examResultUsecase
            .findExamResultById(examId)
            .map {
              case Some(examResult) =>
                val response = ExamResultResponseDto.fromDomain(examResult)
                Ok(Json.toJson(response))
              case None =>
                NotFound(s"Exam result with id $examId not found")
            }
            .recover { case ex =>
              InternalServerError(
                s"Failed to fetch exam result: ${ex.getMessage}"
              )
            }
      }
  }
}
