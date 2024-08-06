package infrastructure.db

import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DatabaseConfig @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val profile: JdbcProfile = dbConfig.profile
  val db = dbConfig.db

  import profile.api._

  def run[T](action: DBIO[T]): Future[T] = db.run(action)
}
