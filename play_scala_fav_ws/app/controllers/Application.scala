package controllers

import javax.inject.Inject

import model._
import play.api.libs.json._
import play.api.mvc._
import service.CacheService

class Application @Inject() (cache: CacheService) extends Controller {
  implicit val favouriteWrites: Writes[Favourite] = Json.writes[Favourite]
  implicit val favouritesWrites: Writes[Favourites] = Json.writes[Favourites]
  implicit val favouriteFormat: Format[Favourite] = Json.format[Favourite]
  implicit val favouritesFormat: Format[Favourites] = Json.format[Favourites]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def get(customerNo: String) = Action {
    val maybeFavourites: Option[String] = cache.get(customerNo)
    maybeFavourites match {
      case Some(favourites) => Ok(Json.parse(favourites))
      case None => NotFound
    }
  }

  def put(customerNo: String): Action[JsValue] = Action(parse.json) { request =>
    cache.get(customerNo) match {
      case Some(_) => {
        val favouritesFromJson: JsResult[Favourites] = Json.fromJson[Favourites](request.body)
        favouritesFromJson match {
          case JsSuccess(f: Favourites,_) => {
            cache.remove(f.customerNo)
            cache.set(f.customerNo, request.body.toString())
            Ok
          }
          case e: JsError => BadRequest
        }
      }
      case None => NotFound
    }
  }

  def post: Action[JsValue] = Action(parse.json) { request =>
    val favouritesFromJson: JsResult[Favourites] = Json.fromJson[Favourites](request.body)
    favouritesFromJson match {
      case JsSuccess(f: Favourites, path: JsPath) =>
        cache.set(f.customerNo, request.body.toString())
        Created
      case JsError(_) => BadRequest
    }
  }

  def delete(customerNo: String) = Action {
    val maybeFavourites: Option[String] = cache.get(customerNo)
    maybeFavourites match {
      case Some(_) =>
        cache.remove(customerNo)
        NoContent
      case None => NotFound
    }
  }
}