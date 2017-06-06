package fav.ws.spring.boot.api.services;

import fav.ws.spring.boot.api.model.Favourites;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryFavouritesRepo implements FavouritesRepository {
    private ConcurrentHashMap<String, Favourites> map = new ConcurrentHashMap<>();

    @Override
    public Optional<Favourites> get(String id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public void delete(String id) {
        map.remove(id);
    }

    @Override
    public void update(Favourites favourites) {
        map.put(favourites.getCustomerNo(), favourites);
    }

    @Override
    public void add(Favourites favourites) {
        map.put(favourites.getCustomerNo(), favourites);
    }
}
