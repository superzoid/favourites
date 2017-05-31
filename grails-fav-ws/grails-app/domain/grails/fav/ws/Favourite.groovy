package grails.fav.ws

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
class Favourite {

    String sku
    Integer purchaseCount

    static constraints = {
        sku blank: false, nullable: false
        purchaseCount nullable: false
    }
}
