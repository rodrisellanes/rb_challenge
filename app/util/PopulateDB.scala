package util

import javax.inject.{Inject, Singleton}

import dal.{BoardRepository, LocationRepository, UserRepository}
import models.Location

@Singleton
class PopulateDB @Inject() (userRepo: UserRepository, boardRepo: BoardRepository, locationRepo: LocationRepository) {

  def createUser(): Unit = {
    userRepo.create("Rodrigo")
    userRepo.create("Martin")
  }

  def createBoards(): Unit = {
    boardRepo.create("Vacations", 1)
    boardRepo.create("City", 1)
    boardRepo.create("Work Travel", 1)

    boardRepo.create("NewPort Beaches", 2)
    boardRepo.create("Near Cities", 2)
  }

  def createLocations(): Unit = {
    locationRepo.create("Buenos Aires - AR", "2017-07-16 / 01:24", 7.2, "Showers", 468739, 1)
    locationRepo.create("Canberra - AU", "2017-07-16 / 01:26", 2.0, "Sunny", 1100968, 1)
    locationRepo.create("Sydney - AU", "2017-07-16 / 01:27", 11.6, "Cloudy", 1105779, 4)
    locationRepo.create("Berlin - GR", "2017-07-16 / 03:12", 6.1, "Mostly Sunny", 638242, 4)
    locationRepo.create("Berlin - GR", "2017-07-16 / 03:12", 6.1, "Mostly Sunny", 638242, 2)
    locationRepo.create("Austin - US", "2017-07-16 / 03:24", 18.9, "Cover", 2357536, 2)
    locationRepo.create("Boston - US", "2017-07-16 / 03:25", 16.6, "Cover", 2367105, 1)

  }

}
