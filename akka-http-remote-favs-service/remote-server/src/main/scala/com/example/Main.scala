package com.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.example.cache.{CacheActor, MemCacheService}

import scala.io.StdIn

object Main {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val cache = new MemCacheService()
    val cacheActor = system.actorOf(CacheActor.props(cache), CacheActor.name)

    println(s"Server online\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
  }

  // Here you can define all the different routes you want to have served by this web server
  // Note that routes might be defined in separated traits like the current case

}
