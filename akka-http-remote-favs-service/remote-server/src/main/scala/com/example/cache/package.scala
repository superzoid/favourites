package com.example

package object cache {
  final case class Favorite(sku: String, purchaseCount: Int)
  final case class Favorites(customerNo: String, favs: List[Favorite])
}
