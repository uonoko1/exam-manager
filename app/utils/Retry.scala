package utils

import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._
import org.apache.pekko.actor.Scheduler
import java.sql.SQLTransientConnectionException

object Retry {
  def withRetry[T](
      operation: => Future[T],
      retries: Int,
      delay: FiniteDuration
  )(implicit ec: ExecutionContext, scheduler: Scheduler): Future[T] = {
    operation.recoverWith {
      case ex: SQLTransientConnectionException if retries > 0 =>
        println(
          s"Retrying... attempts left: $retries, due to error: ${ex.getMessage}"
        )
        org.apache.pekko.pattern.after(delay, scheduler) {
          withRetry(operation, retries - 1, delay)
        }
    }
  }
}
