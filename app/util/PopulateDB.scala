package util

import javax.inject.Inject

import dal.{BoardRepository, LocationRepository, UserRepository}

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
    locationRepo.create("Buenos Aires", "2017-07-16 / 01:24", 7.2, "Showers", 468739)
    locationRepo.create("Canberra", "2017-07-16 / 01:26", 2.0, "Sunny", 1100968)
    locationRepo.create("Sydney", "2017-07-16 / 01:27", 11.6, "Cloundy", 1105779)
  }

}
