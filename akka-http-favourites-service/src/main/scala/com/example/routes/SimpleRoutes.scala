package com.example.routes

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.http.scaladsl.server.directives.OnSuccessMagnet
import akka.pattern.ask
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.example.CacheActor
import com.example.routes.CacheService.{FAIL, OK}
import spray.json._

import scala.concurrent.Future

final case class Favorite(sku:String, purchaseCount: Int)
final case class Favorites(customerNo: String, favs: List[Favorite])

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val favouriteFormat = jsonFormat2(Favorite)
  implicit val favouritesFormat = jsonFormat2(Favorites)
}

class SimpleRoutes(cacheActor: ActorRef) extends JsonSupport{
  implicit val timeout = Timeout(15, TimeUnit.MILLISECONDS)
  // This `val` holds one route (of possibly many more that will be part of your Web App)
  lazy val simpleRoutes =
    pathPrefix("favourites") {
      pathEndOrSingleSlash {
        post {
          entity(as[Favorites]) { favs =>
            val future = cacheActor ? CacheActor.Add(favs)
            onSuccess(future) {
              case OK => complete(StatusCodes.Created)
              case FAIL => complete(StatusCodes.InternalServerError)

            }
          }
        }
      }
    } ~
    pathPrefix ("favourites" / Segment) { id =>
      pathEndOrSingleSlash {
        get {
          val future: Future[Option[Favorites]] = ask(cacheActor, CacheActor.Get(id)).mapTo[Option[Favorites]]
          onSuccess(future) {
            case Some(favs) => complete(favs)
            case None       => complete(StatusCodes.NotFound)
          }
        } ~
        delete {
          val future = cacheActor ? CacheActor.Delete(id)
          onSuccess(future) {
            case OK   => complete(StatusCodes.NoContent)
            case FAIL => complete(StatusCodes.NotFound)
          }
        } ~
        put {
          entity(as[Favorites]) { favs =>
            val future = cacheActor ? CacheActor.Update(favs)
            onSuccess(future) {
              case OK   => complete(StatusCodes.OK)
              case FAIL => complete(StatusCodes.InternalServerError)
            }
          }

        }
      }
    }
}
