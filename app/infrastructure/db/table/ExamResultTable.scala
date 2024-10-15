package infrastructure.db.table

import slick.jdbc.JdbcProfile
import slick.lifted.{ ProvenShape, TableQuery, Tag }
import slick.sql.SqlProfile.ColumnOption.NotNull
import slick.jdbc.MySQLProfile.api._
import dto.infrastructure.examResult.entity.ExamResultDto
import java.time.ZonedDateTime

class ExamResultTable(tag: Tag)(implicit val profile: JdbcProfile)
    extends Table[ExamResultDto](tag, "exam_results") {
  import profile.api._

  def examResultId: Rep[String] = column[String]("exam_result_id", O.PrimaryKey)
  def examId: Rep[String] = column[String]("exam_id", NotNull)
  def score: Rep[Int] = column[Int]("score", NotNull)
  def studentId: Rep[String] = column[String]("student_id", NotNull)
  def evaluation: Rep[String] = column[String]("evaluation", NotNull)
  def createdAt: Rep[ZonedDateTime] =
    column[ZonedDateTime]("created_at", NotNull)
  def updatedAt: Rep[ZonedDateTime] =
    column[ZonedDateTime]("updated_at", NotNull)

  def * : ProvenShape[ExamResultDto] = (
    examResultId,
    examId,
    score,
    studentId,
    evaluation,
    createdAt,
    updatedAt
  ).mapTo[ExamResultDto]
}

object ExamResultTable {
  val examResults = TableQuery[ExamResultTable]
}
