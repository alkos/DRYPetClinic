package eu.execom.dry.petclinic.api

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.service._

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

trait SecuredApi {

  val securedService: SecuredService
  val permissionDao: PermissionDao

  def secure[T](authCode: String, accessRights: AccessRight*)(f: User => T)(implicit slickSession: SlickSession): T = {
    val (user, client) = securedService.authenticate(authCode).getOrElse(throw CREDENTIALS_ARE_INVALID)

    if (client.accessTokenExpires.isAfterNow) throw ACCESS_TOKEN_IS_EXPIRED
    if (accessRights.nonEmpty && permissionDao.findByRoleAccessRights(user.roleId, accessRights.toList).isEmpty) throw INSUFFICIENT_RIGHTS
    f(user)
  }

}
