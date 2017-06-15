package grails.fav.ws

import net.spy.memcached.MemcachedClient
import grails.transaction.Transactional

@Transactional
class FavouriteService {
    static int fiveMinutes = 60*5
    def memcachedServer = System.getProperty("memcached.server", "localhost")
    def memcachedPort = Integer.parseInt(System.getProperty("memcached.port", "11211"))
    def memcacheClient = new MemcachedClient(new InetSocketAddress(memcachedServer, memcachedPort))

    def get(String id) {
        memcacheClient.get(id)
    }

    def add(String id, String json) {
        memcacheClient.add(id, fiveMinutes, json)
    }

    def delete(String id) {
        memcacheClient.delete(id)
    }

    def update(String id, String json) {
        memcacheClient.delete(id)
        add(id, json)
    }
}
