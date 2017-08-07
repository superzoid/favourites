import java.util.concurrent.TimeUnit

import akka.actor.{ActorSelection, ActorSystem}
import com.example.cache.CacheActor.{Add, Delete, Get}
import com.example.cache.{Favorite, Favorites}
import akka.pattern._
import akka.util.Timeout

import scala.util.{Failure, Success}

object TestMain {
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(1, TimeUnit.SECONDS)

  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-test-system")
    val ref = system.actorSelection("akka.tcp://my-system@127.0.0.1:2552/user/CacheActor")
    val f = Favorites("one", List(Favorite("sku1", 1)))

    ref ! Add(f)

    getFavourites(ref)

    val response = ref ? Delete("one")
    response.onComplete {
      case Success(data) => println("Deleted? "+data)
      case Failure(ex) => ex.printStackTrace()
    }

  }

  private def getFavourites(ref: ActorSelection) = {
    val response = ref ? Get("one")
    response.onComplete {
      case Success(data) => println(data)
      case Failure(ex) => ex.printStackTrace()
    }
  }
}
