import com.google.inject.AbstractModule
import javax.inject._
import java.time.Clock
import play.api.inject.{ApplicationLifecycle, Binding}
import play.api.{Configuration, Environment}
import infrastructure.db.repositories.{
  ExamResultRepositoryImplOnDb,
  ExamRepositoryImplOnDb
}
import usecases.examResult.repository.ExamResultRepository
import usecases.exam.repository.ExamRepository
import utils.UlidGenerator
import infrastructure.libs.UlidGeneratorImpl
import infrastructure.providers._
import infrastructure.db.repositories._
import org.apache.pekko.actor.Scheduler
import domain.evaluator.`trait`.Evaluator
import domain.evaluator.impl.QuartileEvaluatorImpl
import usecases.examResult.logic.examResultUpdater.`trait`.ExamResultUpdater
import usecases.examResult.logic.examResultUpdater.impl.ExamResultUpdaterImpl
import usecases.exam.logic.examUpdater.`trait`.ExamUpdater
import usecases.exam.logic.examUpdater.impl.ExamUpdaterImpl
import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import domain.evaluationPeriodProvider.impl.WeeklyEvaluationPeriodProviderImpl
import dto.request.exam.valueObject.examIdRequestConverter.impl.ExamIdRequestConverterImpl
import dto.request.exam.valueObject.examIdRequestConverter.`trait`.ExamIdRequestConverter
import dto.request.examResult.valueObject.studentIdRequestConverter.impl.StudentIdRequestConverterImpl
import dto.request.examResult.jsonParser.examResultFieldConverter.impl.ExamResultFieldConverterImpl
import dto.request.exam.valueObject.dueDateRequestConverter.impl.DueDateRequestConverterImpl
import dto.request.exam.valueObject.evaluationStatusRequestConverter.impl.EvaluationStatusRequestConverterImpl
import dto.request.exam.valueObject.subjectRequestConverter.impl.SubjectRequestConverterImpl
import dto.request.examResult.valueObject.evaluationRequestConverter.impl.EvaluationRequestConverterImpl
import dto.request.examResult.valueObject.examResultIdRequestConverter.impl.ExamResultIdRequestConverterImpl
import dto.request.examResult.valueObject.scoreRequestConverter.impl.ScoreRequestConverterImpl
import dto.utils.dateTime.createdAtRequestConverter.impl.CreatedAtRequestConverterImpl
import dto.utils.dateTime.updatedAtRequestConverter.impl.UpdatedAtRequestConverterImpl
import dto.utils.dateTime.dateTimeConverter.impl.DateTimeConverterImpl
import dto.request.examResult.valueObject.studentIdRequestConverter.`trait`.StudentIdRequestConverter
import dto.request.examResult.jsonParser.examResultFieldConverter.`trait`.ExamResultFieldConverter
import dto.request.exam.valueObject.dueDateRequestConverter.`trait`.DueDateRequestConverter
import dto.request.exam.valueObject.evaluationStatusRequestConverter.`trait`.EvaluationStatusRequestConverter
import dto.request.exam.valueObject.subjectRequestConverter.`trait`.SubjectRequestConverter
import dto.request.examResult.valueObject.evaluationRequestConverter.`trait`.EvaluationRequestConverter
import dto.request.examResult.valueObject.examResultIdRequestConverter.`trait`.ExamResultIdRequestConverter
import dto.request.examResult.valueObject.scoreRequestConverter.`trait`.ScoreRequestConverter
import dto.utils.dateTime.createdAtRequestConverter.`trait`.CreatedAtRequestConverter
import dto.utils.dateTime.updatedAtRequestConverter.`trait`.UpdatedAtRequestConverter
import dto.utils.dateTime.dateTimeConverter.`trait`.DateTimeConverter
import dto.request.utils.jsonFieldParser.`trait`.JsonFieldParser
import dto.request.utils.jsonFieldParser.impl.JsonFieldParserImpl
import domain.evaluationPeriodProvider.impl.WeeklyEvaluationPeriodProviderImpl
import utils.{SystemClock, SystemClockImpl}

class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ExamResultRepository])
      .to(classOf[ExamResultRepositoryImplOnDb])
      .asEagerSingleton()
    bind(classOf[ExamRepository])
      .to(classOf[ExamRepositoryImplOnDb])
      .asEagerSingleton()
    bind(classOf[UlidGenerator])
      .to(classOf[UlidGeneratorImpl])
      .asEagerSingleton()
    bind(classOf[Evaluator])
      .to(classOf[QuartileEvaluatorImpl])
      .asEagerSingleton()
    bind(classOf[ExamResultUpdater])
      .to(classOf[ExamResultUpdaterImpl])
      .asEagerSingleton()
    bind(classOf[ExamUpdater])
      .to(classOf[ExamUpdaterImpl])
      .asEagerSingleton()
    bind(classOf[EvaluationPeriodProvider])
      .to(classOf[WeeklyEvaluationPeriodProviderImpl])
      .asEagerSingleton()
    bind(classOf[Scheduler])
      .toProvider(classOf[SchedulerProvider])
      .asEagerSingleton()
    bind(classOf[ExamIdRequestConverter])
      .to(classOf[ExamIdRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[StudentIdRequestConverter])
      .to(classOf[StudentIdRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[ExamResultFieldConverter])
      .to(classOf[ExamResultFieldConverterImpl])
      .asEagerSingleton()
    bind(classOf[DueDateRequestConverter])
      .to(classOf[DueDateRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[EvaluationStatusRequestConverter])
      .to(classOf[EvaluationStatusRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[SubjectRequestConverter])
      .to(classOf[SubjectRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[EvaluationRequestConverter])
      .to(classOf[EvaluationRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[ExamResultIdRequestConverter])
      .to(classOf[ExamResultIdRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[ScoreRequestConverter])
      .to(classOf[ScoreRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[CreatedAtRequestConverter])
      .to(classOf[CreatedAtRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[UpdatedAtRequestConverter])
      .to(classOf[UpdatedAtRequestConverterImpl])
      .asEagerSingleton()
    bind(classOf[DateTimeConverter])
      .to(classOf[DateTimeConverterImpl])
      .asEagerSingleton()
    bind(classOf[JsonFieldParser]).to(classOf[JsonFieldParserImpl])
    bind(classOf[SystemClock]).to(classOf[SystemClockImpl]).asEagerSingleton()
  }
}
