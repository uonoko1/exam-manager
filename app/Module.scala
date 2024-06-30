import com.google.inject.AbstractModule
import java.time.Clock
import play.api.inject.{ApplicationLifecycle, Binding}
import play.api.{Configuration, Environment}
import infrastructure.db.repositories.ExamResultRepositoryImpl
import usecases.repositories.ExamResultRepository
import utils.UlidGenerator
import infrastructure.libs.UlidGeneratorImpl
import domain.evaluator.`trait`.Evaluator
import domain.evaluator.impl.QuartileEvaluator

class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ExamResultRepository])
      .to(classOf[ExamResultRepositoryImpl])
      .asEagerSingleton()
    bind(classOf[UlidGenerator])
      .to(classOf[UlidGeneratorImpl])
      .asEagerSingleton()
    bind(classOf[Evaluator])
      .to(classOf[QuartileEvaluator])
      .asEagerSingleton()
  }
}
