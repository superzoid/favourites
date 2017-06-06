package fav.ws.spring.boot.api.controllers;

import fav.ws.spring.boot.api.model.Favourites;
import fav.ws.spring.boot.api.services.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.websocket.server.PathParam;

@RestController
public class FavouritesController {
    private final FavouritesRepository repo;

    @Autowired
    public FavouritesController(FavouritesRepository repo) {
        this.repo = repo;
    }

    @RequestMapping(path = "/favourites/{id}", method = RequestMethod.GET)
    public Favourites get(@PathVariable("id") String id) throws Exception{
        return getFavourites(id);
    }

    @RequestMapping(path = "/favourites", method = RequestMethod.POST)
    public ResponseEntity<Favourites> post(@RequestBody Favourites favourites) throws Exception{
        repo.add(favourites);
        return new ResponseEntity<>(favourites, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/favourites/{id}", method = RequestMethod.PUT)
    public void put(@RequestBody Favourites f, @PathVariable("id") String id) throws Exception{
        getFavourites(id);
        repo.update(f);
    }

    @RequestMapping(path = "/favourites/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") String id) throws Exception{
        getFavourites(id);
        repo.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private Favourites getFavourites(String id) {
        return repo.get(id).orElseThrow(NotFoundException::new);
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such favourites")
    public class NotFoundException extends RuntimeException {

    }
}
