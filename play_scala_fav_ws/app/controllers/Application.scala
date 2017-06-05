package controllers

import javax.inject.Inject

import model._
import play.api.libs.json._
import play.api.mvc._
import service.CacheService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.Logger

class Application @Inject() (cache: CacheService) extends Controller {
  implicit val favouriteWrites: Writes[Favourite] = Json.writes[Favourite]
  implicit val favouritesWrites: Writes[Favourites] = Json.writes[Favourites]
  implicit val favouriteFormat: Format[Favourite] = Json.format[Favourite]
  implicit val favouritesFormat: Format[Favourites] = Json.format[Favourites]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def get(customerNo: String) = Action.async {
    Logger info s"Getting favourites for $customerNo"
    val futureMaybeFavourites = cache.get(customerNo)

    val res:Future[Result] = futureMaybeFavourites.map {
      case Some(favs) => Ok(Json.parse(favs))
      case None => NotFound
    }.recover{case e: Exception => {
      Logger.error(s"Cannot get favourites fo $customerNo", e)
      InternalServerError
    }}

    res
  }

  def put(customerNo: String) = Action.async(parse.json) { request =>
    val body = request.body.toString()
    Logger info s"Adding json : $body"
    val futureMaybeFavourites = cache get customerNo

    val res: Future[Result] = futureMaybeFavourites.map{
      case Some(_) => updateFavourites(customerNo, body)
      case None => NotFound
    }.recover{case _ => InternalServerError}

    res
  }

  def updateFavourites(customerNo:String, json: String): Status = {
    cache remove customerNo
    cache.set(customerNo, json)
    Ok
  }

  def post = Action.async(parse.json) { request =>
    val favouritesFromJson: JsResult[Favourites] = Json.fromJson[Favourites](request.body)
    favouritesFromJson match {
      case JsSuccess(f: Favourites, path: JsPath) =>
        val cancelableFuture = cache.set(f.customerNo, request.body.toString())
        cancelableFuture.map{ set =>
          if(set) {
            Created
          } else {
            InternalServerError
          }
        }.recover{case _ => InternalServerError}
      case JsError(_) => Future{BadRequest}
    }
  }

  def delete(customerNo: String) = Action.async {
    val futureMaybeFavourites = cache get customerNo
    val res: Future[Result] = futureMaybeFavourites.map{
      case Some(_) =>
        cache remove customerNo
        NoContent
      case None => NotFound
    }.recover{
      case _ => InternalServerError
    }
    res
  }
}