package grails.fav.ws

import com.google.gson.Gson
import grails.rest.*
import net.spy.memcached.MemcachedClient

class FavouritesController extends RestfulController{
    static int fiveMinutes = 60*5
    def memcachedServer = System.getProperty("memcached.server", "localhost")
    def memcachedPort = Integer.parseInt(System.getProperty("memcached.port", "11211"))

    def memcacheClient = new MemcachedClient(new InetSocketAddress(memcachedServer, memcachedPort))
    def gson = new Gson()

    FavouritesController() {
        super(Favourites)
    }

    def index() {}

    private getFavs(String id) {
        def favs = memcacheClient.get(id)
        if(favs != null) {
            return favs
        }
        
       return null
    }

    @Override
    Object show() {
        def favs = getFavs(params.id)
        if( favs == null ){
            response.status = 404
            return response
        }

        render contentType: "application/json", text: favs, status: 200
    }

    @Override
    Object delete() {
        def id = params.id
        def favs = getFavs(id)
        if( favs == null ) {
            response.status = 404
            return response
        }

        memcacheClient.delete(id)
        render status: 204
    }

    @Override
    Object update() {
        def id = params.id
        def favs = getFavs(id)
        if( favs == null ) {
            response.status = 404
            return response
        }
        memcacheClient.delete(id)
        memcacheClient.add(id, fiveMinutes, request.getJSON().toString())
        render status: 200
    }

    @Override
    protected Object queryForResource(Serializable id) {
        resource
        return super.queryForResource(id)
    }

    @Override
    Object save() {
        def n = this.request.getJSON()
        memcacheClient.add(n.get("customerNo"), fiveMinutes, n.toString())
        response.setStatus(201)

        response
    }

    @Override
    protected Object getObjectToBind() {
        def bind = super.getObjectToBind()

        return bind
    }
}
