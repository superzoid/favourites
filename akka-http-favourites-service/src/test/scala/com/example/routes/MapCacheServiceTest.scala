package com.example.routes

import java.util.concurrent.TimeUnit

import com.example.routes.CacheService.OK
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MapCacheServiceTest extends FunSuite {
  val timeout = Duration(1,TimeUnit.MILLISECONDS)

  test("test map backed cache") {
    val cache = new MapCacheService()
    val favs = Favorites("one", List(Favorite("sku1",1)))

    val addResult = Await.result(cache.add(favs), timeout)
    assert(addResult == OK)

    val cachedFavs: Option[Favorites] = get(cache)
    assert(favs == cachedFavs.get)

    val updatedFavs = Favorites(favs.customerNo, Favorite("sku1",1) :: favs.favs)
    val updateResult = Await.result(cache.update(updatedFavs), timeout)
    val updatedCacheFavs = get(cache)
    assert(updatedCacheFavs.get == updatedFavs)

    val deleteResult = Await.result(cache.delete("one"), timeout)
    assert(deleteResult == OK)
    val deletedFavs = get(cache)
    assert(deletedFavs.isEmpty)

  }

  def get(cache: MapCacheService): Option[Favorites] = {
    Await.result(cache.get("one"), timeout)
  }
}
