package spec;

import com.google.gson.Gson;
import model.Favourites;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static model.Favourite.fav;
import static org.hamcrest.CoreMatchers.hasItems;

public class FavouritesSpec {
    private Gson gson = new Gson();

    @Test
    public void handlesMissingFavourites() {
        when().
            get("/favourites/ngcyeugnhxfgeryusncgfbrbc").
        then().
            statusCode(404);
    }

    @Test
    public void addsFavourites() {
        final Favourites f = createFavourites();

        addFavourites(f);

        verifyFavouritesJson(f);
    }

    private void verifyFavouritesJson(Favourites f) {
        verifyFavourites(f.getCustomerNo());
    }

    @Test
    public void deletesFavourites() {
        final Favourites f = createFavourites();

        addFavourites(f);

        verifyFavourites(f.getCustomerNo());

        removeFavourites(f.getCustomerNo());

        verifyDeleted(f);

    }

    @Test
    public void updatesFavourites() {
        final Favourites f = createFavourites();

        addFavourites(f);

        verifyFavourites(f.getCustomerNo(), "1", "2");

        updateFavourites(f.getCustomerNo(), f);

        verifyFavourites(f.getCustomerNo(), "1", "2", "3");
    }

    private void removeFavourites(String customerNo) {
        System.out.println("Deleting favourites for "+customerNo);
        when().
            delete("/favourites/" + customerNo).
        then().
            statusCode(204);
    }

    private Favourites createFavourites() {
        final String customerNo = UUID.randomUUID().toString();
        return new Favourites(customerNo, fav("1", 1), fav("2", 1));
    }

    private void updateFavourites(String customerNo, Favourites f) {
        f.add(fav("3", 1));

        final String json = gson.toJson(f);
        System.out.println("Updating json to : ");
        System.out.println(json);

        given().
            contentType(JSON).
            body(json).
        when().
            put("/favourites/" + customerNo).
        then().
            statusCode(200);
    }

    private void verifyDeleted(Favourites f) {
        when().
            get("/favourites/" + f.getCustomerNo()).
        then().
            statusCode(404);
    }

    private void verifyFavourites(String customerNo, String... items) {
        when().
            get("/favourites/" + customerNo).
        then().
            statusCode(200).
        and().
            body("favs.sku", hasItems(items));
    }

    private void addFavourites(Favourites favourites) {
        final String json = gson.toJson(favourites);
        System.out.println("Creating json : ");
        System.out.println(json);
        given().
            body(json).
            contentType(JSON).
        when().
            post("/favourites/").
        then().
            statusCode(201);
    }
}
