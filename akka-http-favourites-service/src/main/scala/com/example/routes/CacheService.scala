package com.example.routes

import java.io._
import java.util.concurrent.TimeUnit._

import com.example.routes.CacheService.{FAIL, OK, CacheResponse}
import shade.memcached._

import scala.None
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.reflect.ClassTag
import scala.util.control.NonFatal


trait CacheService {
  def add(favorites: Favorites) : Future[CacheResponse]
  def update(favorites: Favorites) : Future[CacheResponse]
  def delete(id: String) : Future[CacheResponse]
  def get(id: String) : Future[Option[Favorites]]
}

object CacheService {
  sealed trait CacheResponse
  case object OK extends CacheResponse
  case object FAIL extends CacheResponse
}

class MapCacheService extends CacheService {
  private var cache = Map[String,Favorites]()

  override def add(favorites: Favorites) = {
    cache = cache + (favorites.customerNo -> favorites)
    Future{OK}
  }

  override def get(id: String): Future[Option[Favorites]] = Future{cache.get(id)}

  override def delete(id: String) = {
    cache = cache - id
    Future{OK}
  }

  override def update(favorites: Favorites) = {
    cache = cache + (favorites.customerNo -> favorites)
    Future{OK}
  }

}

class MemCacheService extends CacheService {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val memcachedServer = System.getProperty("memcached.server", "localhost")
  private val memcachedPort = Integer.parseInt(System.getProperty("memcached.port", "11211"))
  private val ttl = System.getProperty("memcached.ttl", "300").toLong
  private val memcached = Memcached(Configuration(s"$memcachedServer:$memcachedPort"))

  implicit val codec = new FavoritesCodec()

  override def add(favorites: Favorites) = {
    memcached.add(favorites.customerNo, favorites, Duration(ttl, SECONDS))
    Future{OK}
  }

  override def update(favorites: Favorites) = {
    memcached.set(favorites.customerNo, favorites, Duration(ttl, SECONDS))
    Future{OK}
  }

  override def get(id: String) = {
    memcached.get(id).map{
      case Some(f) => Option(f)
      case _       => None
    }
  }

  override def delete(id: String) = {
    memcached.delete(id)
    Future{OK}
  }
}

