package infrastructure.db

import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DatabaseConfig @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext) {
  // Slickのデータベース設定を取得
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val profile: JdbcProfile = dbConfig.profile

  import dbConfig._
  import profile.api._

  // DBIOアクションを実行するためのヘルパーメソッド
  def run[T](action: DBIO[T]): Future[T] = db.run(action)
}
