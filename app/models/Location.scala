package models

import play.api.libs.json.Json

case class Location(id: Long, city: String, date: String, temp: Double, text: String, woeid: Long, board: Long)

object Location {

  implicit val locationFormat = Json.format[Location]

  val locations: List[Location] = List(
    Location(1, "Buenos Aires - AR", "", 0.0, "", 468739, -1),
    Location(2, "Canberra - AU", "", 0.0, "", 1100968, -1),
    Location(3, "Sydney - AU", "", 0.0, "", 1105779, -1),
    Location(4, "Berlin - GR", "", 0.0, "", 638242, -1),
    Location(5, "Austin - US", "", 0.0, "", 2357536, -1),
    Location(6, "Boston - US", "", 0.0, "", 2367105, -1)
  )
}
