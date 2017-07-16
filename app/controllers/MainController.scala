package controllers


import dal.{BoardRepository, LocationRepository, UserRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import util.PopulateDB
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

class MainController @Inject()(userRepo: UserRepository, boardRepo: BoardRepository, locationRepo: LocationRepository, messagesApi: MessagesApi)
                                       (implicit ec: ExecutionContext) extends Controller {

  //TODO: Research this `with I18nSupport`

  /**
    * The mapping for the user, board, location form.
    */
  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

  val boardForm: Form[CreateBoardForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "user_id" -> number
    )(CreateBoardForm.apply)(CreateBoardForm.unapply)
  }

  val locationForm: Form[CreateLocationForm] = Form {
    mapping(
      "id" -> number,
      "board_id" -> number
    )(CreateLocationForm.apply)(CreateLocationForm.unapply)
  }

  /**
    * Populate Database
    */
 def populate = Action {
   val populateDB = new PopulateDB(userRepo, boardRepo, locationRepo)
   populateDB.createUser()
   populateDB.createBoards()
   populateDB.createLocations()
   Ok(views.html.home("Weather Boards", "Database populated"))
 }

  def home = Action {
    Ok(views.html.home("Weather Boards", "Welcome"))
  }

  /**
    * USER
    */
  def getUsers = Action.async {
    userRepo.list().map { users =>
//      Ok(views.html.users(Json.toJson(users)))
      Ok(Json.toJson(users))
    }
  }

  /**
    * BOARD
    */
  def boardsHome(user_id: Long) = Action { implicit request =>
    Ok(views.html.boards(boardForm, user_id))
  }

  def getBoards(user_id: Long) = Action.async {
    boardRepo.listByUser(user_id).map { board =>
      Ok(Json.toJson(board))
    }
  }

  def addBoard() = Action.async { implicit request =>
    boardForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.home("Weather Boards", errorForm.toString)))
      },
      board => {
        boardRepo.create(board.name, board.user_id).map { _ =>
          // If successful, we simply redirect to the home page.
          Ok(views.html.home("Weather Boards", "Board Added"))
        }
      }
    )
  }

  /**
    * LOCATION
    */

  def locationHome(user_id: Long, board_id: Long) = Action { implicit request =>
    Ok(views.html.locations(locationForm, user_id, board_id))
  }

  def getLocations(user_id: Long, board_id: Long) = Action.async {
    locationRepo.listByBoard(board_id).map { location =>
      Ok(Json.toJson(location))
    }
  }

  def addLocation() = Action.async { implicit request =>
    locationForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.home("Weather Boards", errorForm.toString)))
      },
      location => {
        locationRepo.create("Bella Vista - AR" + location.id, "2017-07-16 / 01:24", 7.2, "Showers", 468739, location.board_id).map { _ =>
          // If successful, we simply redirect to the home page.
          Ok(views.html.home("Weather Boards", "Location Added"))
        }
      }
    )
  }
  def updateLocations(user_id: Long, board_id: Long) = Action {
    Ok("UPDATE - Missing Implementation")
  }
  def deleteLocation() = Action.async { implicit request =>
    locationForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.home("Weather Boards", errorForm.toString)))
      },
      location => {
        locationRepo.delete(location.id).map { _ =>
          // If successful, we simply redirect to the home page.
          Ok(views.html.home("Weather Boards", "Location " + location.id + "has been deleted"))
        }
      }
    )
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

case class CreateUserForm(name: String)
case class CreateBoardForm(name: String, user_id: Int)
case class CreateLocationForm(id: Int, board_id: Int)
