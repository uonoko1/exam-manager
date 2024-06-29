package controllers

import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import usecases.ExamResultUsecase
import scala.concurrent.Future
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

class ExamResultControllerSpec
    extends PlaySpec
    with GuiceOneAppPerSuite
    with Injecting {
  val mockUsecase = mock[ExamResultUsecase]

  override def fakeApplication() = new GuiceApplicationBuilder()
    .overrides(bind[ExamResultUsecase].toInstance(mockUsecase))
    .build()

  "ExamResultController POST /exam-result/:subject/:score/:studentId" should {
    "return OK for valid parameters" in {
      when(mockUsecase.saveExamResult(anyString, anyInt, anyInt))
        .thenReturn(Future.successful("Passed"))
      val request = FakeRequest(POST, "/exam-result/Math/85/1")
      val result = route(app, request).get

      status(result) mustBe OK
      contentAsString(result) must include("Passed")
    }

    "return BadRequest for invalid parameters" in {
      val request = FakeRequest(POST, "/exam-result/Math/invalid/1")
      val result = route(app, request).get

      status(result) mustBe BAD_REQUEST
    }
  }
}
