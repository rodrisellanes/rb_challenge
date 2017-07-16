package models

import play.api.libs.json._

case class Board_X_Location(id: Long, location_id: Long, board_id: Long)

object Board_X_Location {

  implicit val board_x_locationFormat = Json.format[Board_X_Location]
}
