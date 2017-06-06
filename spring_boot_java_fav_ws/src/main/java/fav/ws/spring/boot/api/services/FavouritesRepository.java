package fav.ws.spring.boot.api.services;

import fav.ws.spring.boot.api.model.Favourites;

import java.util.Optional;

public interface FavouritesRepository {
    Optional<Favourites> get(String id);
    void delete(String id);
    void update(Favourites favourites);
    void add(Favourites favourites);
}
