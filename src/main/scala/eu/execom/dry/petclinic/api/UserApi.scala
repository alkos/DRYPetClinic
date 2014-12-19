package eu.execom.dry.petclinic.api

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.service._
import eu.execom.dry.petclinic.util.Logging
import org.joda.time._

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.util._

class UserApi(val userDao: UserDao, val userService: UserService, val securedService: SecuredService, val permissionDao: PermissionDao) extends SecuredApi with Logging {

  def readUser(requestDto: ReadUserDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[ReadUserResponseDto] = Try {
    logger.trace(s".readUser(requestDto: $requestDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode, AccessRight.ADD_USER) { implicit user: User =>

      val result: Option[User] = userDao.findById(requestDto.id)

      result match {
        case Some(r) => new ReadUserResponseDto(r.id, r.roleId, r.username)
        case None => throw new IllegalArgumentException()
      }
    }
  }

  def createUser(saveDto: CreateUserDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[ReadUserResponseDto] = Try {
    logger.trace(s".createUser(saveDto: $saveDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode, AccessRight.ADD_USER) { implicit user: User =>

      val entity: User = new User()
      entity.roleId = saveDto.role
      entity.username = saveDto.username
      entity.passwordHash = saveDto.password
      userService.save(entity)

      val result: User = userDao.findById(entity.id).get

      new ReadUserResponseDto(result.id, result.roleId, result.username)
    }
  }

  def updateUser(updateDto: UpdateUserDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[ReadUserResponseDto] = Try {
    logger.trace(s".updateUser(updateDto: $updateDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode, AccessRight.ADD_USER) { implicit user: User =>

      val entity: User = userDao.getById(updateDto.id)
      entity.roleId = updateDto.role
      if (updateDto.password.isDefined) entity.passwordHash = updateDto.password.get
      userService.update(entity)

      val result: User = userDao.findById(entity.id).get

      new ReadUserResponseDto(result.id, result.roleId, result.username)
    }
  }

  def deleteUser(deleteDto: ReadUserDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[Unit] = Try {
    logger.trace(s".deleteUser(deleteDto: $deleteDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode, AccessRight.ADD_USER) { implicit user: User =>

      val entity: User = userDao.getById(deleteDto.id)

      userService.delete(entity)
    }
  }

  def users(requestDto: UsersDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[SearchResultDto[UsersResponseDto]] = Try {
    logger.trace(s".users(requestDto: $requestDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode) { implicit user: User =>

      val result: List[User] = userDao.findAllPaged(requestDto.from, requestDto.maxRowCount)

      new SearchResultDto(
        result.map(r => new UsersResponseDto(r.id, r.username, r.roleId)),
        userDao.countAll())
    }
  }

  def adminUsers(requestDto: AdminUsersDto, authenticationCode: String)(implicit slickSession: SlickSession): Try[SearchResultDto[AdminUsersResponseDto]] = Try {
    logger.trace(s".adminUsers(requestDto: $requestDto, authenticationCode: $authenticationCode)")
    secure(authenticationCode) { implicit user: User =>

      val result: List[User] = userDao.findByRolePaged(requestDto.role, requestDto.from, requestDto.maxRowCount)

      new SearchResultDto(
        result.map(r => new AdminUsersResponseDto(r.id, r.username, r.roleId)),
        userDao.countByRole(requestDto.role))
    }
  }

}

case class ReadUserDto(id: Int)

object ReadUserDto {
  val ID: String = "id"
}

case class ReadUserResponseDto(id: Int, role: Int, username: String)

object ReadUserResponseDto {
  val ID: String = "id"
  val ROLE: String = "role"
  val USERNAME: String = "username"
}

case class CreateUserDto(role: Int, username: String, password: String)

object CreateUserDto {
  val ROLE: String = "role"
  val USERNAME: String = "username"
  val PASSWORD: String = "password"
}

case class UpdateUserDto(id: Int, role: Int, password: Option[String])

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

case class UsersResponseDto(id: Int, username: String, roleId: Int)

object UsersResponseDto {
  val ID: String = "id"
  val USERNAME: String = "username"
  val ROLEID: String = "roleId"
}

case class AdminUsersDto(role: Int, from: Int, maxRowCount: Int)

object AdminUsersDto {
  val ROLE: String = "role"
  val FROM: String = "from"
  val MAXROWCOUNT: String = "maxRowCount"
}

case class AdminUsersResponseDto(id: Int, username: String, roleId: Int)

object AdminUsersResponseDto {
  val ID: String = "id"
  val USERNAME: String = "username"
  val ROLEID: String = "roleId"
}
