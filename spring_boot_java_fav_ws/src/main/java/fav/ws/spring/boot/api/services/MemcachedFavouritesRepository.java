package fav.ws.spring.boot.api.services;

import fav.ws.spring.boot.api.model.Favourites;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
@Primary
public class MemcachedFavouritesRepository implements FavouritesRepository {
    private static final int fiveMins = 60*5;
    private final MemcachedClient client;

    public MemcachedFavouritesRepository() {
        try {
            String memcachedServer = System.getProperty("memcached.server", "localhost");
            int memcachedPort = Integer.parseInt(System.getProperty("memcached.port", "11211"));
            client = new MemcachedClient(new InetSocketAddress(memcachedServer, memcachedPort));
        } catch (IOException e) {
            throw new InvalidStateException("Cannot start memcached client");
        }
    }

    @Override
    public Optional<Favourites> get(String id) {
        Favourites favs = (Favourites) client.get(id);
        return Optional.ofNullable(favs);
    }

    @Override
    public void delete(String id) {
        try {
            client.delete(id).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void update(Favourites favourites) {
        try {
            delete(favourites.getCustomerNo());
            client.add(favourites.getCustomerNo(), fiveMins, favourites).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void add(Favourites favourites) {
        try {
            client.add(favourites.getCustomerNo(), fiveMins, favourites).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CacheException(e);
        }
    }

    public class CacheException extends RuntimeException{

        public CacheException(Exception e) {
            super(e);
        }
    }
}
