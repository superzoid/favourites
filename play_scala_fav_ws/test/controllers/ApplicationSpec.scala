package controllers

import org.specs2.mock._
import play.api.mvc._
import play.api.test._
import service.CacheService

import scala.concurrent.Future

class ApplicationSpec extends PlaySpecification with Results with Mockito{

  "The application" should {
    "return 404 for missing favourites" in {
      val mockCacheService = mock[CacheService]
      mockCacheService.get("1") returns Future.successful{Option.empty}
      true must equalTo(true)
      val controller = new Application(mockCacheService)

      val result: Future[Result] = controller.get("1").apply(FakeRequest())
      status(result) must equalTo(404)
    }

  }
}

