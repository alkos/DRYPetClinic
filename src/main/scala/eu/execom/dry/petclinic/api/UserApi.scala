package eu.execom.dry.petclinic.api

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.service._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.util._

class UserApi(val userDao: UserDao, val userService: UserService, val securedService: SecuredService) extends SecuredApi with Logging {

  def readUser(requestDto: ReadUserDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[ReadUserResponseDto] = Try {
    logger.trace(s".readUser(requestDto: $requestDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode, UserRole.ADMIN :: Nil) { implicit user: User =>

      val result: Option[User] = userDao.findById(requestDto.id)

      result match {
        case Some(r) => new ReadUserResponseDto(r.id, r.role, r.username)
        case None => throw new IllegalArgumentException()
      }
    }
  }

  def createUser(saveDto: CreateUserDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[ReadUserResponseDto] = Try {
    logger.trace(s".createUser(saveDto: $saveDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode, UserRole.ADMIN :: Nil) { implicit user: User =>

      val entity: User = new User()
      entity.role = UserRole.USER
      entity.username = saveDto.username
      entity.passwordHash = saveDto.password
      userService.save(entity)

      val result: User = userDao.findById(entity.id).get

      new ReadUserResponseDto(result.id, result.role, result.username)
    }
  }

  def updateUser(updateDto: UpdateUserDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[ReadUserResponseDto] = Try {
    logger.trace(s".updateUser(updateDto: $updateDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode, UserRole.ADMIN :: Nil) { implicit user: User =>

      val entity: User = userDao.getById(updateDto.id)
      entity.role = UserRole.USER
      if (updateDto.password.isDefined) entity.passwordHash = updateDto.password.get
      userService.update(entity)

      val result: User = userDao.findById(entity.id).get

      new ReadUserResponseDto(result.id, result.role, result.username)
    }
  }

  def deleteUser(deleteDto: ReadUserDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[Unit] = Try {
    logger.trace(s".deleteUser(deleteDto: $deleteDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode, UserRole.ADMIN :: Nil) { implicit user: User =>

      val entity: User = userDao.getById(deleteDto.id)

      userService.delete(entity)
    }
  }

  def users(requestDto: UsersDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[SearchResultDto[UsersResponseDto]] = Try {
    logger.trace(s".users(requestDto: $requestDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode) { implicit user: User =>

      val result: List[User] = userDao.findAllPaged(requestDto.from, requestDto.maxRowCount)

      new SearchResultDto(
        result.map(r => new UsersResponseDto(r.id, r.username, r.role)),
        userDao.countAll())
    }
  }

  def adminUsers(requestDto: AdminUsersDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[SearchResultDto[AdminUsersResponseDto]] = Try {
    logger.trace(s".adminUsers(requestDto: $requestDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode) { implicit user: User =>

      val result: List[User] = userDao.findByRoleIsAdminPaged(requestDto.from, requestDto.maxRowCount)

      new SearchResultDto(
        result.map(r => new AdminUsersResponseDto(r.id, r.username, r.role)),
        userDao.countByRoleIsAdmin())
    }
  }

}

case class ReadUserDto(id: Int)

object ReadUserDto {
  val ID: String = "id"
}

case class ReadUserResponseDto(id: Int, role: UserRole, username: String)

object ReadUserResponseDto {
  val ID: String = "id"
  val ROLE: String = "role"
  val USERNAME: String = "username"
}

case class CreateUserDto(username: String, password: String)

object CreateUserDto {
  val USERNAME: String = "username"
  val PASSWORD: String = "password"
}

case class UpdateUserDto(id: Int, role: UserRole, password: Option[String])

object UpdateUserDto {
  val ID: String = "id"
  val ROLE: String = "role"
  val PASSWORD: String = "password"
}

case class UsersDto(from: Int, maxRowCount: Int)

object UsersDto {
  val FROM: String = "from"
  val MAXROWCOUNT: String = "maxRowCount"
}

case class UsersResponseDto(id: Int, username: String, role: UserRole)

object UsersResponseDto {
  val ID: String = "id"
  val USERNAME: String = "username"
  val ROLE: String = "role"
}

case class AdminUsersDto(from: Int, maxRowCount: Int)

object AdminUsersDto {
  val FROM: String = "from"
  val MAXROWCOUNT: String = "maxRowCount"
}

case class AdminUsersResponseDto(id: Int, username: String, role: UserRole)

object AdminUsersResponseDto {
  val ID: String = "id"
  val USERNAME: String = "username"
  val ROLE: String = "role"
}
