import com.google.inject.AbstractModule
import java.time.Clock
import play.api.inject.{ApplicationLifecycle, Binding}
import play.api.{Configuration, Environment}
import infrastructure.db.repositories.{
  ExamResultRepositoryImpl,
  ExamRepositoryImpl
}
import usecases.examResult.repository.ExamResultRepository
import usecases.exam.repository.ExamRepository
import utils.UlidGenerator
import infrastructure.libs.UlidGeneratorImpl
import infrastructure.providers._
import infrastructure.db.repositories._
import org.apache.pekko.actor.Scheduler
import domain.evaluator.`trait`.Evaluator
import domain.evaluator.impl.QuartileEvaluator
import usecases.examResult.logic.examResultUpdater.`trait`.ExamResultUpdater
import usecases.examResult.logic.examResultUpdater.impl.ExamResultUpdaterImpl
import usecases.exam.logic.examUpdater.`trait`.ExamUpdater
import usecases.exam.logic.examUpdater.impl.ExamUpdaterImpl
import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import domain.evaluationPeriodProvider.impl.WeeklyEvaluationPeriodProvider
import dto.request.exam.valueObject.ExamIdRequestConverter
import dto.request.examResult.valueObject.StudentIdRequestConverter
import dto.request.examResult.jsonParser.ExamResultFieldConverter

class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ExamResultRepository])
      .to(classOf[ExamResultRepositoryImpl])
      .asEagerSingleton()
    bind(classOf[ExamRepository])
      .to(classOf[ExamRepositoryImpl])
      .asEagerSingleton()
    bind(classOf[UlidGenerator])
      .to(classOf[UlidGeneratorImpl])
      .asEagerSingleton()
    bind(classOf[Evaluator])
      .to(classOf[QuartileEvaluator])
      .asEagerSingleton()
    bind(classOf[ExamResultUpdater])
      .to(classOf[ExamResultUpdaterImpl])
      .asEagerSingleton()
    bind(classOf[ExamUpdater])
      .to(classOf[ExamUpdaterImpl])
      .asEagerSingleton()
    bind(classOf[EvaluationPeriodProvider])
      .to(classOf[WeeklyEvaluationPeriodProvider])
      .asEagerSingleton()
    bind(classOf[Scheduler])
      .toProvider(classOf[SchedulerProvider])
      .asEagerSingleton()
    bind(classOf[ExamIdRequestConverter]).asEagerSingleton()
    bind(classOf[StudentIdRequestConverter]).asEagerSingleton()
    bind(classOf[ExamResultFieldConverter]).asEagerSingleton()
  }
}
