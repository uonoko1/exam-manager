package scheduler

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import org.apache.pekko.actor.ActorSystem
import usecases.examResult.ExamResultUsecase
import java.time.ZonedDateTime

@Singleton
class JobScheduler @Inject() (
    actorSystem: ActorSystem,
    examResultUsecase: ExamResultUsecase
)(implicit ec: ExecutionContext) {

  private def calculateInitialDelay(): FiniteDuration = {
    val now = ZonedDateTime.now()
    val nextSunday = now
      .`with`(
        java.time.temporal.TemporalAdjusters
          .nextOrSame(java.time.DayOfWeek.SUNDAY)
      )
      .withHour(24)
      .withMinute(0)
      .withSecond(0)
    val duration = java.time.Duration.between(now, nextSunday)
    FiniteDuration(duration.toMillis, MILLISECONDS)
  }

  private val period: FiniteDuration = FiniteDuration(7, DAYS)

  actorSystem.scheduler.scheduleWithFixedDelay(
    calculateInitialDelay(),
    period
  )(new Runnable {
    def run(): Unit = {
      examResultUsecase.evaluateResults.map {
        case Right(_)    => println("Scheduled job completed successfully.")
        case Left(error) => println(s"Scheduled job failed with error: $error")
      }
    }
  })
}
