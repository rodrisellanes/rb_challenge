package dal

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import models.Location

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LocationRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import driver.api._

  /**
    * Here we define the table. Just un name for the user
    */
  private class LocationTable(tag: Tag) extends Table[Location](tag, "LOCATION") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def city = column[String]("city")
    def date = column[String]("date")
    def temp = column[Double]("temp")
    def text = column[String]("text")
    def woeid = column[Long]("woeid")
    def board = column[Long]("board")
    // Location projection
    def * = (id, city, date, temp, text, woeid, board) <> ((Location.apply _).tupled, Location.unapply)
  }

  private val location = TableQuery[LocationTable]

  /**
    * Create a location into a board
    */
  def create(city: String, date: String, temp: Double, text: String, woeid: Long, board: Long): Future[Location] = db.run {
    (location.map(l => (l.city, l.date, l.temp, l.text, l.woeid, l.board))
      returning location.map(_.id)
      into((locSorted, id) => Location(id, locSorted._1, locSorted._2, locSorted._3, locSorted._4, locSorted._5, locSorted._6))
      ) += (city, date, temp, text, woeid, board)
  }

  /**
    * Update a specific location
    */
  def update(id: Long, locationUp: Location): Future[Unit] = {
    db.run(location.filter(_.id === id).update(locationUp)).map(_ => ())
  }

  /**
    * Delete a specific location
    */
  def delete(id: Long): Future[Unit] = {
    db.run(location.filter(_.id === id).delete).map(_ => ())
  }

  /**
    * List all locations in the database
    */
  def list(): Future[Seq[Location]] = db.run {
    location.result
  }

  /**
    * List all locations from a board
    */
  def listByBoard(board_id: Long): Future[Seq[Location]] = {
    db.run(location.filter(_.board === board_id).result)
  }

}
