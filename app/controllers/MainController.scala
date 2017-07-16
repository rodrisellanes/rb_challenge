package controllers


import dal.{BoardRepository, LocationRepository, UserRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import util.PopulateDB

import scala.concurrent.ExecutionContext
import javax.inject.Inject

class MainController @Inject()(userRepo: UserRepository, boardRepo: BoardRepository, locationRepo: LocationRepository, messagesApi: MessagesApi)
                                       (implicit ec: ExecutionContext) extends Controller {

  //TODO: Research this `with I18nSupport`

  case class CreateUserForm(id: String)

  /**
    * The mapping for the user, board, location form.
    */
  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

 def populate = Action {
   val populateDB = new PopulateDB(userRepo, boardRepo, locationRepo)
   populateDB.createUser()
   populateDB.createBoards()
   populateDB.createLocations()
   Ok("Database populated")
 }

  /**
    * USER
    */
  def getUsers = Action.async {
    userRepo.list().map { users =>
      Ok(Json.toJson(users))
    }
  }

  /**
    * BOARD
    */
  def getBoards(user_id: Long) = Action.async {
    boardRepo.listByUser(user_id).map { board =>
      Ok(Json.toJson(board))
    }
  }

  def addBoard(user_id: Long) = Action {
    boardRepo.create("New Board", user_id)
    Ok("Board created")
  }

  /**
    * LOCATION
    */

  def getLocations(user_id: Long, board_id: Long) = Action.async {
    locationRepo.list().map { location =>
      Ok(Json.toJson(location))
    }
  }

  def addLocation(user_id: Long, board_id: Long) = Action {
    Ok("ADD - Missing Implementation")
  }
  def updateLocations(user_id: Long, board_id: Long) = Action {
    Ok("UPDATE - Missing Implementation")
  }
  def deleteLocation(user_id: Long, board_id: Long, location_id: Long) = Action {
    Ok("DELETE - Missing Implementation")
  }


  /**
    * TEST HTTP REST
    */

  def getAllLocations() = Action.async {
    locationRepo.list().map { location =>
      Ok(Json.toJson(location))
    }
  }
}
