package com.example.routes

import java.util.concurrent.TimeUnit

import com.example.routes.CacheService.OK
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Created by andy on 15/06/2017.
 */
class MemCacheServiceTest extends FunSuite {
  val timeout = Duration(30, TimeUnit.MILLISECONDS)

  test("testService") {
    val service = new MemCacheService()
    val favs = Favorites("customer", List(Favorite("sku1", 1)))
    val addResult: OK.type = Await.result(service.add(favs), timeout)
    assert(addResult == OK)

    assert(favs == getFavourites(service).get)

    val updatedFavs = Favorites(favs.customerNo, Favorite("sku2", 2) :: favs.favs)
    val updateResult: OK.type = Await.result(service.update(updatedFavs), timeout)
    assert(updateResult == OK)
    assert(updatedFavs == getFavourites(service).get)

    val deleteResult: OK.type = Await.result(service.delete("customer"), timeout)
    assert(deleteResult == OK)

    val missingFavourites: Option[Favorites] = getFavourites(service)
    assert(missingFavourites.isEmpty)
  }

  def getFavourites(service: MemCacheService): Option[Favorites] = {
    val cachedFavs: Option[Favorites] = Await.result(service.get("customer"), timeout)
    cachedFavs
  }
}
