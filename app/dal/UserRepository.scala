package dal

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import models.User

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import driver.api._

  /**
    * Here we define the table. Just un name for the user
    */
  private class UserTable(tag: Tag) extends Table[User](tag, "USER") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    // User projection
    def * = (id, name) <> ((User.apply _).tupled, User.unapply)
  }

  private val user = TableQuery[UserTable]

/**
  * Create a user with the given name
  */
  def create(name: String): Future[User] = db.run {
    (user.map(u => u.name)
      returning user.map(_.id)
      into((n, id) => User(id, n))
      ) += name
  }

  /**
    * List all the users in the database
    */
  def list(): Future[Seq[User]] = db.run {
    user.result
  }

}
