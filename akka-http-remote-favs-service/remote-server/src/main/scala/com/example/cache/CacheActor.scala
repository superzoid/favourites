package com.example.cache

import akka.actor.{Actor, ActorLogging, Props}
import com.example.cache.CacheActor._
import com.example.cache.CacheService.FAIL

import scala.util.{Failure, Success}

class CacheActor(cache: CacheService) extends Actor with ActorLogging {
  import scala.concurrent.ExecutionContext.Implicits.global


  override def aroundPreStart(): Unit = log.info("Starting up "+this.self.path)

  override def receive: Receive = {
    case Get(id) => {
      log.info(s"looking for $id")
      val caller = sender()
      cache.get(id).onComplete {
        case Success(maybeFavourites) => {
          log.info(s"found $maybeFavourites")
          caller ! maybeFavourites
        }
        case Failure(_) => caller ! None
      }
    }
    case Add(favourites) => {
      val caller = sender()
      cache.add(favourites).onComplete {
        case Success(response) => {
          log.info(s"saved $favourites")
          caller ! response
        }
        case Failure(_) => caller ! FAIL
      }
    }
    case Delete(id) => {
      val caller = sender()
      cache.delete(id).onComplete {
        case Success(response) => {
          log.info(s"deleted $id")
          caller ! response
        }
        case Failure(_) => caller ! FAIL
      }
    }
    case Update(favourites) => {
      val caller = sender()
      cache.update(favourites).onComplete {
        case Success(response) => {
          log.info(s"updated $favourites")
          caller ! response
        }
        case Failure(_) => caller ! FAIL
      }
    }
  }
}

object CacheActor {
  def name = "CacheActor"
  def props(cache: CacheService): Props = Props(new CacheActor(cache))

  case class Get(id: String)
  case class Add(favourites: Favorites)
  case class Delete(id: String)
  case class Update(favorites: Favorites)
}
