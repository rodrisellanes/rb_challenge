package models

import play.api.libs.json.Json

case class Location(id: Long, city: String, date: String, temp: Double, text: String, woeid: Long)

object Location {

  implicit val locationFormat = Json.format[Location]
}
