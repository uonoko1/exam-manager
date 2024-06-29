package infrastructure.db.table

import slick.jdbc.JdbcProfile
import slick.lifted.{ProvenShape, TableQuery, Tag}
import slick.sql.SqlProfile.ColumnOption.NotNull
import slick.jdbc.MySQLProfile.api._
import dto.infrastructure.examResult.entity.ExamResultDto

// ExamResultテーブルの定義
class ExamResultTable(tag: Tag)(implicit val profile: JdbcProfile)
    extends Table[ExamResultDto](tag, "exam_results") {
  import profile.api._

  // テーブルの列定義
  def examId: Rep[String] = column[String]("exam_id", O.PrimaryKey)
  def subject: Rep[String] = column[String]("subject", NotNull)
  def score: Rep[Int] = column[Int]("score", NotNull)
  def studentId: Rep[Int] = column[Int]("student_id", NotNull)
  def evaluation: Rep[String] = column[String]("evaluation", NotNull)

  // テーブルの全列をマッピングするメソッド
  def * : ProvenShape[ExamResultDto] = (
    examId,
    subject,
    score,
    studentId,
    evaluation
  ) <> ((ExamResultDto.apply _).tupled, ExamResultDto.unapply)
}

// ExamResultTableオブジェクト
object ExamResultTable {
  val examResults = TableQuery[ExamResultTable]
}
