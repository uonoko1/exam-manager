package infrastructure.providers

import com.google.inject.{ Inject, Provider }
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.actor.Scheduler

import javax.inject.Singleton

@Singleton
class SchedulerProvider @Inject() (actorSystem: ActorSystem)
    extends Provider[Scheduler] {
  override def get(): Scheduler = actorSystem.scheduler
}
