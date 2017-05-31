package grails.fav.ws


import grails.rest.*
import grails.converters.*

class FavouritesController extends RestfulController{
    static responseFormats = ['json', 'xml']

    FavouritesController() {
        super(Favourites)
    }

    def index() {}

    @Override
    Object show() {
        def favs = Favourites.find { customerNo == params.id }
        if( favs == null ){
            response.status = 404
            return response
        } else {
            params.id = favs.id
            def j = favs as JSON
            j.render(response)
        }
    }

    @Override
    Object delete() {
        def favs = Favourites.find { customerNo == params.id }
        if( favs == null ) {
            response.status = 404
            return response
        }
        params.id = favs.id
        return super.delete()
    }

    @Override
    Object update() {
        def favs = Favourites.find { customerNo == params.id }
        if( favs == null ) {
            response.status = 404
            return response
        }
        params.id = favs.id
        return super.update()
    }

    @Override
    protected Object queryForResource(Serializable id) {
        resource
        return super.queryForResource(id)
    }

    @Override
    Object save() {
        super.save()
    }

    @Override
    protected Object getObjectToBind() {
        def bind = super.getObjectToBind()

        return bind
    }
}
