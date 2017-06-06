package fav.ws.spring.boot.api.controllers;

import fav.ws.spring.boot.api.model.Favourites;
import fav.ws.spring.boot.api.services.FavouritesRepository;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FavouritesControllerTest {
    private FavouritesRepository repo = mock(FavouritesRepository.class);
    private FavouritesController controller = new FavouritesController(repo);

    @Test(expected = HttpServerErrorException.class)
    public void getWithMissingFavourites() throws Exception {
        when(repo.get(anyString())).thenReturn(Optional.empty());
        controller.get("1");
    }

    @Test
    public void post() throws Exception {
//        Favourites favourites = new Favourites("1", Collections.emptyList());
//        final ResponseEntity<Favourites> post = controller.post(favourites);
//        assertThat( post.getStatusCode(), is( equalTo( HttpStatus.CREATED)));
    }

    @Test(expected = HttpServerErrorException.class)
    public void putWithMissingFavourites() throws Exception {
        Favourites favourites = new Favourites("1", Collections.emptyList());
        when(repo.get(anyString())).thenReturn(Optional.empty());
        controller.put(favourites, "1");
    }

    @Test(expected = HttpServerErrorException.class)
    public void deleteWithMissingFavourites() throws Exception {
        Favourites favourites = new Favourites("1", Collections.emptyList());
        when(repo.get(anyString())).thenReturn(Optional.empty());
        controller.delete("1");
    }

    @Test
    public void get() throws Exception {
        Favourites favourites = new Favourites("1", Collections.emptyList());
        when(repo.get(anyString())).thenReturn(Optional.of(favourites));
        final Favourites favourites1 = controller.get("1");
        assertEquals(favourites, favourites1);
    }

    @Test
    public void put() throws Exception {
        Favourites favourites = new Favourites("1", Collections.emptyList());
        when(repo.get(anyString())).thenReturn(Optional.of(favourites));
        controller.put(favourites, "1");

        verify(repo).update(favourites);
    }

    @Test
    public void delete() throws Exception {
        Favourites favourites = new Favourites("1", Collections.emptyList());
        when(repo.get(anyString())).thenReturn(Optional.of(favourites));
        controller.delete("1");

        verify(repo).delete("1");
    }

}