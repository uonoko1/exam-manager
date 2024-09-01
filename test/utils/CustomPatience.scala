package utils

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Millis, Seconds, Span }

trait CustomPatience extends ScalaFutures {
  implicit override val patienceConfig = PatienceConfig(
    timeout = Span(300, Seconds),
    interval = Span(500, Millis)
  )
}
