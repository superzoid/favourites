package service

import java.util.concurrent.TimeUnit.SECONDS
import javax.inject._

import monix.execution.CancelableFuture

import scala.concurrent.Future
import shade.memcached._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class CacheService {
  private val memcachedServer = System.getProperty("memcached.server", "localhost")
  private val memcachedPort = Integer.parseInt(System.getProperty("memcached.port", "11211"))
  private val ttl = System.getProperty("memcached.ttl", "300").toLong
  private val memcached = Memcached(Configuration(s"$memcachedServer:$memcachedPort"))

  def get(id: String): Future[Option[String]] = memcached.get[String](id)

  def set(id: String, s: String): Future[Boolean] = memcached.add(id, s, Duration(ttl, SECONDS))

  def remove(id:String): Future[Boolean] =  memcached.delete(id)
}
