package infrastructure.db.table

import slick.jdbc.JdbcProfile
import slick.lifted.{ ProvenShape, TableQuery, Tag }
import slick.sql.SqlProfile.ColumnOption.NotNull
import slick.jdbc.MySQLProfile.api._
import dto.infrastructure.exam.entity.ExamDto
import java.time.ZonedDateTime

class ExamTable(tag: Tag)(implicit val profile: JdbcProfile)
    extends Table[ExamDto](tag, "exams") {
  import profile.api._

  def examId: Rep[String] = column[String]("exam_id", O.PrimaryKey)
  def subject: Rep[String] = column[String]("subject", NotNull)
  def dueDate: Rep[ZonedDateTime] = column[ZonedDateTime]("due_date", NotNull)
  def evaluationStatus: Rep[String] =
    column[String]("evaluation_status", NotNull)
  def createdAt: Rep[ZonedDateTime] =
    column[ZonedDateTime]("created_at", NotNull)
  def updatedAt: Rep[ZonedDateTime] =
    column[ZonedDateTime]("updated_at", NotNull)

  def * : ProvenShape[ExamDto] = (
    examId,
    subject,
    dueDate,
    evaluationStatus,
    createdAt,
    updatedAt
  ).mapTo[ExamDto]
}

object ExamTable {
  val exams = TableQuery[ExamTable]
}
