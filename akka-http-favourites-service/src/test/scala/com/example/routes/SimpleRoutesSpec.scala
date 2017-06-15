package com.example.routes

import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport.defaultNodeSeqUnmarshaller
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestProbe
import org.scalatest.{ Matchers, WordSpec }

import scala.xml.NodeSeq

class SimpleRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with JsonSupport{
  val probe = TestProbe()
  val ref = probe.ref
  val route = new SimpleRoutes(ref)

  "SimpleRoute" should {
    "handle a DELETE request to `/favourites`" in {
      Delete("/favourites/12345") ~> route.simpleRoutes ~> check {
        handled shouldBe true
      }
    }
    "handle a GET request to `/favourites`" in {
      Get("/favourites/12345") ~> route.simpleRoutes ~> check {
        handled shouldBe true
      }
    }

    "handle a POST request to `/favourites`" in {
      Post("/favourites", Favorites("12345", List(Favorite("123",1)))) ~> route.simpleRoutes ~> check {
        handled shouldBe true
      }
    }

  }

}
