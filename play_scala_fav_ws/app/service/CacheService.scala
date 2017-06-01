package service

import java.net.InetSocketAddress

import javax.inject._

import net.spy.memcached.MemcachedClient

@Singleton
class CacheService {
  private val memcachedServer = System.getProperty("memcached.server", "localhost")
  private val memcachedPort = Integer.parseInt(System.getProperty("memcached.port", "11211"))
  private val ttl = Integer.parseInt(System.getProperty("memcached.ttl", "300"))
  private val memcached = new MemcachedClient(new InetSocketAddress(memcachedServer, memcachedPort))

  def get(id: String): Option[String] = {
    val raw = memcached.get(id)

    if (raw != null) {
      Some(raw.toString)
    } else {
      None
    }
  }

  def set(id: String, s: String): Unit = {
    memcached.add(id, ttl, s)
  }

  def remove(id:String): Unit =  {
    memcached.delete(id)
  }


}
