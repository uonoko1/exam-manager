package utils

import play.api.Configuration
import java.util.UUID

object TestDatabaseConfig {

  def generateUniqueDbName(): String = s"test_db_${UUID.randomUUID().toString}"

  def buildTestConfiguration(dbName: String): Configuration =
    Configuration(
      "slick.dbs.default.db.url" -> s"jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1",
      "slick.dbs.default.driver" -> "slick.jdbc.H2Profile$",
      "slick.dbs.default.db.driver" -> "org.h2.Driver",
      "slick.dbs.default.db.user" -> "sa",
      "slick.dbs.default.db.password" -> ""
    )
}
