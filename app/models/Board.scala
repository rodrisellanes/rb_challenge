package models

import play.api.libs.json.Json

case class Board(id: Long, name: String, user: Long)

object Board {

  implicit val boardFormat = Json.format[Board]

}
