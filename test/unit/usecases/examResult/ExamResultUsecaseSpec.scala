package usecases.examResult

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

import domain.examResult.entity.ExamResult
import domain.exam.valueObject.ExamId
import domain.examResult.valueObject._
import domain.exam.valueObject._
import domain.evaluator.`trait`.Evaluator
import domain.evaluationPeriodProvider.`trait`.EvaluationPeriodProvider
import domain.utils.dateTime.{ CreatedAt, UpdatedAt }
import domain.exam.entity.Exam
import usecases.examResult.repository.ExamResultRepository
import usecases.examResult.logic.examResultUpdater.`trait`.ExamResultUpdater
import usecases.exam.logic.examUpdater.`trait`.ExamUpdater
import usecases.exam.repository.ExamRepository
import utils.{ SystemClock, UlidGenerator }
import utils.CustomPatience

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.time.ZonedDateTime
import dto.infrastructure.examResult.valueObject.ExamResultIdDto
import dto.request.exam.valueObject._
import dto.request.examResult.valueObject._

class ExamResultUsecaseSpec
    extends AnyWordSpec
    with ScalaFutures
    with Matchers
    with CustomPatience {
  val mockExamResultRepository = mock(classOf[ExamResultRepository])
  val mockExamRepository = mock(classOf[ExamRepository])
  val mockEvaluator = mock(classOf[Evaluator])
  val mockEvaluationPeriodProvider = mock(classOf[EvaluationPeriodProvider])
  val mockExamResultUpdater = mock(classOf[ExamResultUpdater])
  val mockExamUpdater = mock(classOf[ExamUpdater])
  val mockUlidGenerator = mock(classOf[UlidGenerator])
  val mockSystemClock = mock(classOf[SystemClock])

  val usecase = new ExamResultUsecase(
    mockExamResultRepository,
    mockExamRepository,
    mockEvaluator,
    mockEvaluationPeriodProvider,
    mockExamResultUpdater,
    mockExamUpdater,
    mockUlidGenerator,
    mockSystemClock
  )

  val ulidString = "01ARZ3NDEKTSV4RRFFQ69G5FAV"

  "ExamResultUsecase#saveExamResult" should {
    val examId = ExamId(ulidString)
    val subject = Subject.Math
    val score = Score(85)
    val studentId = StudentId("student-id")
    val examResultId = ExamResultId(ulidString)

    val examIdRequestDto = ExamIdRequestDto("exam-id")
    val subjectRequestDto = SubjectRequestDto.Math
    val scoreRequestDto = ScoreRequestDto(85)
    val studentIdRequestDto = StudentIdRequestDto("student-id")
    val examResultIdRequestDto = ExamResultIdDto(ulidString)

    "return the saved ExamResult when given valid input" in {
      val fixedTime =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")
      val examResult = ExamResult(
        examResultId,
        examId,
        score,
        studentId,
        Evaluation.NotEvaluated,
        CreatedAt(fixedTime),
        UpdatedAt(fixedTime)
      )

      when(mockUlidGenerator.generate()).thenReturn(ulidString)
      when(mockSystemClock.now()).thenReturn(fixedTime)
      when(mockExamResultRepository.save(any[ExamResult]))
        .thenReturn(Future.successful(Right(examResult)))

      val result = usecase.saveExamResult(examIdRequestDto, subjectRequestDto, scoreRequestDto, studentIdRequestDto)

      whenReady(result) { res =>
        res mustBe Right(examResult)
        verify(mockExamResultRepository).save(any[ExamResult])
      }
    }

    "handle repository save failure" in {
      val fixedTime =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")

      when(mockUlidGenerator.generate()).thenReturn(ulidString)
      when(mockSystemClock.now()).thenReturn(fixedTime)
      when(mockExamResultRepository.save(any[ExamResult]))
        .thenReturn(Future.successful(Left("Database error")))

      val result = usecase.saveExamResult(examIdRequestDto, subjectRequestDto, scoreRequestDto, studentIdRequestDto)

      whenReady(result) { res =>
        res mustBe Left("Database error")
      }
    }
  }

  "ExamResultUsecase#findById" should {
    val examResultId = ExamResultId("examResult-id")
    val examResultIdRequestDto = ExamResultIdRequestDto("examResult-id")

    "return the ExamResult when given existing ExamId" in {
      val examResult = Some(
        ExamResult(
          ExamResultId(ulidString),
          ExamId("exam-id"),
          Score(85),
          StudentId("student-id"),
          Evaluation.NotEvaluated,
          CreatedAt(ZonedDateTime.now()),
          UpdatedAt(ZonedDateTime.now())
        )
      )

      when(mockExamResultRepository.findById(examResultId))
        .thenReturn(Future.successful(Right(examResult)))

      val result = usecase.findById(examResultIdRequestDto)

      whenReady(result) { res =>
        res mustBe Right(examResult)
        verify(mockExamResultRepository).findById(examResultId)
      }
    }

    "handle repository findById failure" in {
      when(mockExamResultRepository.findById(examResultId))
        .thenReturn(Future.successful(Left("Database error")))

      val result = usecase.findById(examResultIdRequestDto)

      whenReady(result) { res =>
        res mustBe Left("Database error")
      }
    }
  }

  "ExamResultUsecase#evaluateResults" should {
    val startDate = ZonedDateTime.now().minusDays(7)
    val endDate = ZonedDateTime.now()

    "evaluate examResults successfully" in {
      val fixedTime =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")
      val exams = Seq(
        Exam(
          ExamId("exam-id"),
          Subject.Math,
          DueDate(ZonedDateTime.now().minusDays(1)),
          EvaluationStatus.NotEvaluated,
          CreatedAt(ZonedDateTime.now().minusDays(10)),
          UpdatedAt(ZonedDateTime.now().minusDays(1))
        )
      )
      val examResults = Seq(
        ExamResult(
          ExamResultId(ulidString),
          ExamId("exam-id"),
          Score(85),
          StudentId("student-id"),
          Evaluation.NotEvaluated,
          CreatedAt(fixedTime),
          UpdatedAt(fixedTime)
        )
      )
      val evaluations = Map(examResults.head -> Evaluation.Excellent)

      when(mockEvaluationPeriodProvider.getEvaluationPeriod(any[ZonedDateTime]))
        .thenReturn((startDate, endDate))
      when(mockExamRepository.findByDueDate(startDate, endDate))
        .thenReturn(Future.successful(Right(exams)))
      when(mockExamResultRepository.findByExamId(any[ExamId]))
        .thenReturn(Future.successful(Right(examResults)))
      when(mockEvaluator.evaluate(any[Exam], any[Seq[ExamResult]]))
        .thenReturn(evaluations)
      when(
        mockExamResultUpdater.updateEvaluations(
          any[Seq[ExamResult]],
          any[Map[ExamResult, Evaluation]],
          any[ZonedDateTime]
        )
      )
        .thenReturn(Future.successful(Right(examResults)))
      when(
        mockExamUpdater.updateEvaluations(
          any[Exam],
          any[Seq[ExamResult]],
          any[Seq[ExamResult]]
        )
      )
        .thenReturn(Future.successful(Right(exams.head)))

      val result = usecase.evaluateResults()

      whenReady(result) { res =>
        res mustBe Right(())
      }
    }

    "handle examRepository#findbyDueDate failure" in {
      when(mockEvaluationPeriodProvider.getEvaluationPeriod(any[ZonedDateTime]))
        .thenReturn((startDate, endDate))
      when(mockExamRepository.findByDueDate(startDate, endDate))
        .thenReturn(Future.successful(Left("Database error")))

      val result = usecase.evaluateResults()

      whenReady(result) { res =>
        res mustBe Left("Database error")
      }
    }

    "handle examResultRepository#findByExamId failure" in {
      val fixedTime =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")
      val exams = Seq(
        Exam(
          ExamId("exam-id"),
          Subject.Math,
          DueDate(ZonedDateTime.now().minusDays(1)),
          EvaluationStatus.NotEvaluated,
          CreatedAt(ZonedDateTime.now().minusDays(10)),
          UpdatedAt(ZonedDateTime.now().minusDays(1))
        )
      )

      when(mockEvaluationPeriodProvider.getEvaluationPeriod(any[ZonedDateTime]))
        .thenReturn((startDate, endDate))
      when(mockExamRepository.findByDueDate(startDate, endDate))
        .thenReturn(Future.successful(Right(exams)))
      when(mockExamResultRepository.findByExamId(any[ExamId]))
        .thenReturn(Future.successful(Left("Failed to retrieve exam results")))

      val result = usecase.evaluateResults()

      whenReady(result) { res =>
        res mustBe Left("Failed to retrieve exam results")
      }
    }

    "handle examResultUpdater#updateEvaluations failure" in {
      val fixedTime =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")
      val exams = Seq(
        Exam(
          ExamId("exam-id"),
          Subject.Math,
          DueDate(ZonedDateTime.now().minusDays(1)),
          EvaluationStatus.NotEvaluated,
          CreatedAt(ZonedDateTime.now().minusDays(10)),
          UpdatedAt(ZonedDateTime.now().minusDays(1))
        )
      )
      val examResults = Seq(
        ExamResult(
          ExamResultId(ulidString),
          ExamId("exam-id"),
          Score(85),
          StudentId("student-id"),
          Evaluation.NotEvaluated,
          CreatedAt(fixedTime),
          UpdatedAt(fixedTime)
        )
      )
      val evaluations = Map(examResults.head -> Evaluation.Excellent)

      when(mockEvaluationPeriodProvider.getEvaluationPeriod(any[ZonedDateTime]))
        .thenReturn((startDate, endDate))
      when(mockExamRepository.findByDueDate(startDate, endDate))
        .thenReturn(Future.successful(Right(exams)))
      when(mockExamResultRepository.findByExamId(any[ExamId]))
        .thenReturn(Future.successful(Right(examResults)))
      when(mockEvaluator.evaluate(any[Exam], any[Seq[ExamResult]]))
        .thenReturn(evaluations)
      when(
        mockExamResultUpdater.updateEvaluations(
          any[Seq[ExamResult]],
          any[Map[ExamResult, Evaluation]],
          any[ZonedDateTime]
        )
      )
        .thenReturn(Future.successful(Left("Failed to update evaluations")))

      val result = usecase.evaluateResults()

      whenReady(result) { res =>
        res mustBe Left("Failed to update evaluations")
      }
    }

    "handle examUpdater#updateEvaluations failure" in {
      val fixedTime =
        ZonedDateTime.parse("2024-07-21T12:00:00+09:00[Asia/Tokyo]")
      val exams = Seq(
        Exam(
          ExamId("exam-id"),
          Subject.Math,
          DueDate(ZonedDateTime.now().minusDays(1)),
          EvaluationStatus.NotEvaluated,
          CreatedAt(ZonedDateTime.now().minusDays(10)),
          UpdatedAt(ZonedDateTime.now().minusDays(1))
        )
      )
      val examResults = Seq(
        ExamResult(
          ExamResultId(ulidString),
          ExamId("exam-id"),
          Score(85),
          StudentId("student-id"),
          Evaluation.NotEvaluated,
          CreatedAt(fixedTime),
          UpdatedAt(fixedTime)
        )
      )
      val evaluations = Map(examResults.head -> Evaluation.Excellent)

      when(mockEvaluationPeriodProvider.getEvaluationPeriod(any[ZonedDateTime]))
        .thenReturn((startDate, endDate))
      when(mockExamRepository.findByDueDate(startDate, endDate))
        .thenReturn(Future.successful(Right(exams)))
      when(mockExamResultRepository.findByExamId(any[ExamId]))
        .thenReturn(Future.successful(Right(examResults)))
      when(mockEvaluator.evaluate(any[Exam], any[Seq[ExamResult]]))
        .thenReturn(evaluations)
      when(
        mockExamResultUpdater.updateEvaluations(
          any[Seq[ExamResult]],
          any[Map[ExamResult, Evaluation]],
          any[ZonedDateTime]
        )
      )
        .thenReturn(Future.successful(Right(examResults)))
      when(
        mockExamUpdater.updateEvaluations(
          any[Exam],
          any[Seq[ExamResult]],
          any[Seq[ExamResult]]
        )
      )
        .thenReturn(Future.successful(Left("Failed to update exam")))

      val result = usecase.evaluateResults()

      whenReady(result) { res =>
        res mustBe Left("Failed to update exam")
      }
    }
  }
}
