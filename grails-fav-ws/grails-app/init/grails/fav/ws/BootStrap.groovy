package grails.fav.ws

import grails.converters.JSON
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        def result = '################## running in UNCLEAR mode.'
        println "Application starting ... "
        switch (Environment.current) {
            case Environment.DEVELOPMENT:
                result = 'now running in DEV mode.'
                break;
            case Environment.TEST:
                result = 'now running in TEST mode.'
                break;
            case Environment.PRODUCTION:
                result = 'now running in PROD mode.'
                break;
        }
        println "current environment: $Environment.current"
        println "$result"

        JSON.registerObjectMarshaller(Favourites) {
            def output = [:]
            output['customerNo'] = it.customerNo
            output['favs'] = convert(it.favs)
            return output;
        }
    }

    def convert(favs){
        favs.collect {[sku:it.sku, purchaseCount: it.purchaseCount]}
    }

    def destroy = {
        println "Application shutting down... "
    }

}
