package infrastructure.libs

import javax.inject._
import utils.UlidGenerator
import wvlet.airframe.ulid.ULID
import scala.util.Try

@Singleton
class UlidGeneratorImpl @Inject() extends UlidGenerator {
  override def generate(): String = ULID.newULID.toString

  override def isValid(ulid: String): Boolean =
    Try(ULID.fromString(ulid)).isSuccess
}
