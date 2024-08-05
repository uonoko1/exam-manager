package scheduler

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.testkit.TestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers._

import usecases.examResult.ExamResultUsecase
import utils.SystemClock
import utils.CustomPatience

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import java.time.ZonedDateTime

class JobSchedulerSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with BeforeAndAfterAll
    with CustomPatience {

  lazy val system: ActorSystem = ActorSystem("JobSchedulerSpec")

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "JobScheduler" should {
    "calculate the initial delay correctly" in {
      val mockExamResultUsecase = mock(classOf[ExamResultUsecase])
      val mockSystemClock = mock(classOf[SystemClock])

      val fixedNow =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")
      when(mockSystemClock.now()).thenReturn(fixedNow)

      val scheduler =
        new JobScheduler(system, mockExamResultUsecase, mockSystemClock)

      val initialDelay = scheduler.calculateInitialDelay()

      val expectedNextSunday = fixedNow
        .`with`(
          java.time.temporal.TemporalAdjusters
            .nextOrSame(java.time.DayOfWeek.SUNDAY)
        )
        .plusDays(1)
        .withHour(0)
        .withMinute(0)
        .withSecond(0)
        .withNano(0)
      val expectedDuration =
        java.time.Duration.between(fixedNow, expectedNextSunday)
      val expectedInitialDelay: FiniteDuration =
        FiniteDuration(expectedDuration.toMillis, MILLISECONDS)

      initialDelay mustBe expectedInitialDelay
    }
  }
}
