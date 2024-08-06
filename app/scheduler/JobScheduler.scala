package scheduler

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import org.apache.pekko.actor.ActorSystem
import usecases.examResult.ExamResultUsecase
import utils.SystemClock

@Singleton
class JobScheduler @Inject() (
    actorSystem: ActorSystem,
    examResultUsecase: ExamResultUsecase,
    systemClock: SystemClock,
    isTestMode: Boolean = false // テストモードフラグを追加
)(implicit ec: ExecutionContext) {

  def calculateInitialDelay(): FiniteDuration = {
    if (isTestMode) {
      Duration.Zero // テストモードでは遅延なし
    } else {
      val now = systemClock.now()
      val nextSunday = now
        .`with`(
          java.time.temporal.TemporalAdjusters
            .nextOrSame(java.time.DayOfWeek.SUNDAY)
        )
        .plusDays(1)
        .withHour(0)
        .withMinute(0)
        .withSecond(0)
        .withNano(0)
      val duration = java.time.Duration.between(now, nextSunday)
      FiniteDuration(duration.toMillis, MILLISECONDS)
    }
  }

  val period: FiniteDuration =
    if (isTestMode) 1.second else FiniteDuration(7, DAYS)

  private val scheduleRunnable: () => Unit = () => {
    examResultUsecase.evaluateResults().map {
      case Right(_)    => println("Scheduled job completed successfully.")
      case Left(error) => println(s"Scheduled job failed with error: $error")
    }
  }

  if (isTestMode) {
    actorSystem.scheduler.scheduleOnce(calculateInitialDelay()) {
      scheduleRunnable()
    }
  } else {
    actorSystem.scheduler.scheduleWithFixedDelay(
      calculateInitialDelay(),
      period
    ) {
      new Runnable {
        def run(): Unit = scheduleRunnable()
      }
    }
  }
}
