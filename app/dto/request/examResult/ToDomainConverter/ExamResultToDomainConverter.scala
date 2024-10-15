package dto.request.examResult.ToDomainConverter

import javax.inject._
import dto.request.examResult.valueObject._
import dto.request.exam.valueObject._
import dto.request.utils.dateTime._
import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.utils.dateTime._
import scala.util.Try

object ExamResultToDomainConverter {

  def convert[T <: Tuple](args: Option[?]*): Either[String, T] = {
    val resultList = args.collect {
      case Some(dto: ExamResultIdRequestDto) => ExamResultId.create(dto.value)
      case Some(dto: ExamIdRequestDto)       => ExamId.create(dto.value)
      case Some(dto: SubjectRequestDto)      => Subject.create(dto.value)
      case Some(dto: ScoreRequestDto)        => Score.create(dto.value)
      case Some(dto: StudentIdRequestDto)    => StudentId.create(dto.value)
      case Some(dto: EvaluationRequestDto)   => Evaluation.create(dto.value)
      case Some(dto: CreatedAtRequestDto)    => Right(CreatedAt.create(dto.value))
      case Some(dto: UpdatedAtRequestDto)    => Right(UpdatedAt.create(dto.value))
    }

    val failures = resultList.collect { case Left(error) => error }
    if (failures.nonEmpty) {
      Left(failures.mkString(", "))
    } else {
      val tuple = resultList.collect { case Right(value) => value }.asInstanceOf[Seq[Any]].toTuple.asInstanceOf[T]
      Right(tuple)
    }
  }

  implicit class SeqToTuple(seq: Seq[Any]) {
    def toTuple: Product = seq match {
      case Seq()              => EmptyTuple
      case Seq(a)             => Tuple1(a)
      case Seq(a, b)          => (a, b)
      case Seq(a, b, c)       => (a, b, c)
      case Seq(a, b, c, d)    => (a, b, c, d)
      case Seq(a, b, c, d, e) => (a, b, c, d, e)
      case _                  => throw new IllegalArgumentException("Unsupported tuple size")
    }
  }
}
