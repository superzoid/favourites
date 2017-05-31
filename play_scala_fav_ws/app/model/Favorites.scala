package model

case class Favourites(customerNo : String, favs: List[Favourite])
case class Favourite(sku:String, purchaseCount:Int)
