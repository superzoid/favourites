package grails.fav.ws

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = "id")
@EqualsAndHashCode
class Favourites {
    static hasMany = [favs: Favourite]
    List favs
    String customerNo

    static constraints = {
        customerNo nullable: false, empty: false
        favs nullable: false, minSize: 1
    }
}
