package dto.request.examResult.entity

import domain.examResult.valueObject._
import play.api.libs.json.JsValue
import dto.request.examResult.valueObject._

object ExamResultRequestConverter {

  protected def parseAndValidateFields(
      json: JsValue
  ): List[Either[String, Any]] = {
    json.asOpt[Map[String, String]] match {
      case Some(requestBody) =>
        List(
          requestBody
            .get("examId")
            .map(ExamIdRequestConverter.validateAndCreate)
            .getOrElse(Right(null)),
          requestBody
            .get("subject")
            .map(SubjectRequestConverter.validateAndCreate)
            .getOrElse(Right(null)),
          requestBody
            .get("score")
            .map(ScoreRequestConverter.validateAndCreate)
            .getOrElse(Right(null)),
          requestBody
            .get("studentId")
            .map(StudentIdRequestConverter.validateAndCreate)
            .getOrElse(Right(null))
        )
      case None =>
        List(Left("Invalid JSON format"))
    }
  }

  def validateAndCreate(json: JsValue): Either[String, Any] = {
    parseAndValidateFields(json)
      .partition(_.isLeft) match {
      case (Nil, rights) =>
        rights.collect { case Right(value) if value != null => value } match {
          case List(a)          => Right(Tuple1(a))
          case List(a, b)       => Right((a, b))
          case List(a, b, c)    => Right((a, b, c))
          case List(a, b, c, d) => Right((a, b, c, d))
          case _                => Left("Unexpected number of valid results")
        }
      case (lefts, _) =>
        val errors = lefts.collect { case Left(error) => error }
        Left(errors.mkString(", "))
    }
  }
}
