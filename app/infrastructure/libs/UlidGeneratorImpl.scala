package infrastructure.libs

import javax.inject._
import utils.UlidGenerator
import wvlet.airframe.ulid.ULID

@Singleton
class UlidGeneratorImpl @Inject() extends UlidGenerator {
  override def generate(): String = ULID.newULID.toString
}
