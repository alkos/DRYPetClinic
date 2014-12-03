package eu.execom.dry.petclinic.api

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.service._

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

trait SecuredApi {

  val securedService: SecuredService

  def secure[T](authCode: String, accessRoles: List[UserRole] = Nil)(f: User => T)(implicit slickSession: SlickSession): T = {
    val user = securedService.authenticate(authCode).getOrElse(throw CREDENTIALS_ARE_INVALID)
    if (accessRoles.nonEmpty && !accessRoles.contains(user.role)) throw INSUFFICIENT_RIGHTS
    f(user)
  }

}
