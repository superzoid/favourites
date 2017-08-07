package com.example

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.example.cache.CacheActor.{Add, Delete, Get, Update}
import com.example.cache.CacheService.{FAIL, OK}
import com.example.cache.{CacheActor, CacheService, Favorite, Favorites}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Future

class CacheActorTest extends TestKit(ActorSystem("CacheActorTest")) with ImplicitSender
    with WordSpecLike with Matchers with BeforeAndAfterAll with MockFactory {
  val mockService = mock[CacheService]
  import concurrent.ExecutionContext.Implicits.global

  "An CacheActor" must {

    "send favorites to the cache" in {
      val actor = system.actorOf(CacheActor.props(mockService), CacheActor.name)
      val f = Favorites("one", List(Favorite("sku1", 1)))
      (mockService.add(_)).expects(f).returning(Future { OK })
      actor ! Add(f)
      expectMsg(OK)
    }

    "updates favorites in the cache" in {
      val actor = system.actorOf(CacheActor.props(mockService))
      val f = Favorites("one", List(Favorite("sku1", 1)))
      (mockService.update(_)).expects(f).returning(Future { OK })
      actor ! Update(f)
      expectMsg(OK)
    }

    "deletes favorites to the cache" in {
      val actor = system.actorOf(CacheActor.props(mockService))
      (mockService.delete(_)).expects("one").returning(Future { OK })
      actor ! Delete("one")
      expectMsg(OK)
    }

    "gets favorites from the cache" in {
      val actor = system.actorOf(CacheActor.props(mockService))
      val f = Favorites("one", List(Favorite("sku1", 1)))
      (mockService.get(_)).expects("one").returning(Future { Option(f) })
      actor ! Get("one")
      expectMsg(Some(f))
    }

    val ex = new RuntimeException()

    "returns fail on send favorites to the cache" in {
      val actor = system.actorOf(CacheActor.props(mockService))
      val f = Favorites("one", List(Favorite("sku1", 1)))
      (mockService.add(_)).expects(f).returning(Future.failed(ex))
      actor ! Add(f)
      expectMsg(FAIL)
    }

    "returns fail on updates favorites in the cache" in {
      val actor = system.actorOf(CacheActor.props(mockService))
      val f = Favorites("one", List(Favorite("sku1", 1)))
      (mockService.update(_)).expects(f).returning(Future.failed(ex))
      actor ! Update(f)
      expectMsg(FAIL)
    }

    "returns fail on deletes favorites to the cache" in {
      val actor = system.actorOf(CacheActor.props(mockService))
      (mockService.delete(_)).expects("one").returning(Future.failed(ex))
      actor ! Delete("one")
      expectMsg(FAIL)
    }

    "returns fail on gets favorites from the cache" in {
      val actor = system.actorOf(CacheActor.props(mockService))
      val f = Favorites("one", List(Favorite("sku1", 1)))
      (mockService.get(_)).expects("one").returning(Future.failed(ex))
      actor ! Get("one")
      expectMsg(None)
    }

  }

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
}
