package infrastructure.db.repositories

import javax.inject.{Inject, Singleton}
import infrastructure.db.DatabaseConfig
import infrastructure.db.table.ExamResultTable
import scala.concurrent.{ExecutionContext, Future}
import usecases.repositories.ExamResultRepository
import domain.examResult.entity.ExamResult
import dto.infrastructure.examResult.entity.ExamResultDto
import domain.examResult.valueObject.ExamId

// ExamResultRepositoryImplクラス
@Singleton
class ExamResultRepositoryImpl @Inject() (dbConfig: DatabaseConfig)(implicit
    ec: ExecutionContext
) extends ExamResultRepository {
  import dbConfig._
  import profile.api._

  // ExamResultを保存するメソッド
  override def save(examResult: ExamResult): Future[Unit] = {
    val dto = ExamResultDto.fromDomain(examResult)
    val query = ExamResultTable.examResults += dto
    run(query).map(_ => ())
  }

  // examIdでExamResultを検索するメソッド
  override def findById(examId: ExamId): Future[Option[ExamResult]] = {
    val query =
      ExamResultTable.examResults
        .filter(_.examId === examId.value)
        .result
        .headOption
    run(query).flatMap {
      case Some(dto) =>
        ExamResultDto.toDomain(dto) match {
          case Right(examResult) => Future.successful(Some(examResult))
          case Left(error)       => Future.failed(new Exception(error))
        }
      case None => Future.successful(None)
    }
  }
}
