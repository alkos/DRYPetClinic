package eu.execom.dry.petclinic.api

import eu.execom.dry.petclinic._
import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.service._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.util._

class AuthenticationApi(val userDao: UserDao, val securedService: SecuredService, val permissionDao: PermissionDao) extends SecuredApi with Logging {

  def signUp(signUpDto: SignUpDto)(implicit slickSession: SlickSession): Try[AuthenticationResponseDto] = Try {
    logger.trace(s".signUp(signUpDto: $signUpDto)")

    val user = securedService.signUp(signUpDto.username, signUpDto.passwordHash).get
    new AuthenticationResponseDto(user.username, user.roleId, user.authenticationCode.get)
  }

  def signIn(signInDto: SignInDto)(implicit slickSession: SlickSession): Try[AuthenticationResponseDto] = Try {
    logger.trace(s".signIn(signInDto: $signInDto)")

    val user = securedService.signIn(signInDto.username, signInDto.passwordHash).get
    new AuthenticationResponseDto(user.username, user.roleId, user.authenticationCode.get)
  }

  def signOut(authenticationCode: String)(implicit slickSession: SlickSession): Try[Unit] = Try {
    logger.trace(s".signOut(authenticationCode: $authenticationCode)")
    secure(authenticationCode) { implicit user: User =>

      securedService.signOut(authenticationCode)
    }
  }

  def authenticate(authenticationDto: AuthenticationCodeDto)(implicit slickSession: SlickSession): Try[AuthenticationResponseDto] = Try {
    logger.trace(s".authenticate(authenticationDto: $authenticationDto)")

    val user = securedService.authenticate(authenticationDto.authenticationCode).get
    new AuthenticationResponseDto(user.username, user.roleId, user.authenticationCode.get)
  }

}

case class AuthenticationResponseDto(username: String, roleId: Int, authenticationCode: String) {
      
    if (username == null) throw AUTHENTICATIONRESPONSEDTO_USERNAME_IS_REQUIRED

    if (username.size < 0) throw AUTHENTICATIONRESPONSEDTO_USERNAME_MIN_SIZE
    if (username.size > 1024) throw AUTHENTICATIONRESPONSEDTO_USERNAME_MAX_SIZE    
    if (authenticationCode == null) throw AUTHENTICATIONRESPONSEDTO_AUTHENTICATIONCODE_IS_REQUIRED

    if (authenticationCode.size < 0) throw AUTHENTICATIONRESPONSEDTO_AUTHENTICATIONCODE_MIN_SIZE
    if (authenticationCode.size > 128) throw AUTHENTICATIONRESPONSEDTO_AUTHENTICATIONCODE_MAX_SIZE
}

object AuthenticationResponseDto {
  val USERNAME: String = "username"
  val ROLEID: String = "roleId"
  val AUTHENTICATIONCODE: String = "authenticationCode"
}

object AUTHENTICATIONRESPONSEDTO_USERNAME_MIN_SIZE extends DataConstraintException("AUTHENTICATIONRESPONSEDTO_USERNAME_MIN_SIZE")

object AUTHENTICATIONRESPONSEDTO_USERNAME_MAX_SIZE extends DataConstraintException("AUTHENTICATIONRESPONSEDTO_USERNAME_MAX_SIZE")

object AUTHENTICATIONRESPONSEDTO_USERNAME_IS_REQUIRED extends DataConstraintException("AUTHENTICATIONRESPONSEDTO_USERNAME_IS_REQUIRED")

object AUTHENTICATIONRESPONSEDTO_AUTHENTICATIONCODE_MIN_SIZE extends DataConstraintException("AUTHENTICATIONRESPONSEDTO_AUTHENTICATIONCODE_MIN_SIZE")

object AUTHENTICATIONRESPONSEDTO_AUTHENTICATIONCODE_MAX_SIZE extends DataConstraintException("AUTHENTICATIONRESPONSEDTO_AUTHENTICATIONCODE_MAX_SIZE")

object AUTHENTICATIONRESPONSEDTO_AUTHENTICATIONCODE_IS_REQUIRED extends DataConstraintException("AUTHENTICATIONRESPONSEDTO_AUTHENTICATIONCODE_IS_REQUIRED")

case class SignUpDto(username: String, passwordHash: String) {
      
    if (username == null) throw SIGNUPDTO_USERNAME_IS_REQUIRED

    if (username.size < 0) throw SIGNUPDTO_USERNAME_MIN_SIZE
    if (username.size > 1024) throw SIGNUPDTO_USERNAME_MAX_SIZE    
    if (passwordHash == null) throw SIGNUPDTO_PASSWORDHASH_IS_REQUIRED

    if (passwordHash.size < 0) throw SIGNUPDTO_PASSWORDHASH_MIN_SIZE
    if (passwordHash.size > 1024) throw SIGNUPDTO_PASSWORDHASH_MAX_SIZE
}

object SignUpDto {
  val USERNAME: String = "username"
  val PASSWORDHASH: String = "passwordHash"
}

object SIGNUPDTO_USERNAME_MIN_SIZE extends DataConstraintException("SIGNUPDTO_USERNAME_MIN_SIZE")

object SIGNUPDTO_USERNAME_MAX_SIZE extends DataConstraintException("SIGNUPDTO_USERNAME_MAX_SIZE")

object SIGNUPDTO_USERNAME_IS_REQUIRED extends DataConstraintException("SIGNUPDTO_USERNAME_IS_REQUIRED")

object SIGNUPDTO_PASSWORDHASH_MIN_SIZE extends DataConstraintException("SIGNUPDTO_PASSWORDHASH_MIN_SIZE")

object SIGNUPDTO_PASSWORDHASH_MAX_SIZE extends DataConstraintException("SIGNUPDTO_PASSWORDHASH_MAX_SIZE")

object SIGNUPDTO_PASSWORDHASH_IS_REQUIRED extends DataConstraintException("SIGNUPDTO_PASSWORDHASH_IS_REQUIRED")

case class SignInDto(username: String, passwordHash: String) {
      
    if (username == null) throw SIGNINDTO_USERNAME_IS_REQUIRED

    if (username.size < 0) throw SIGNINDTO_USERNAME_MIN_SIZE
    if (username.size > 1024) throw SIGNINDTO_USERNAME_MAX_SIZE    
    if (passwordHash == null) throw SIGNINDTO_PASSWORDHASH_IS_REQUIRED

    if (passwordHash.size < 0) throw SIGNINDTO_PASSWORDHASH_MIN_SIZE
    if (passwordHash.size > 1024) throw SIGNINDTO_PASSWORDHASH_MAX_SIZE
}

object SignInDto {
  val USERNAME: String = "username"
  val PASSWORDHASH: String = "passwordHash"
}

object SIGNINDTO_USERNAME_MIN_SIZE extends DataConstraintException("SIGNINDTO_USERNAME_MIN_SIZE")

object SIGNINDTO_USERNAME_MAX_SIZE extends DataConstraintException("SIGNINDTO_USERNAME_MAX_SIZE")

object SIGNINDTO_USERNAME_IS_REQUIRED extends DataConstraintException("SIGNINDTO_USERNAME_IS_REQUIRED")

object SIGNINDTO_PASSWORDHASH_MIN_SIZE extends DataConstraintException("SIGNINDTO_PASSWORDHASH_MIN_SIZE")

object SIGNINDTO_PASSWORDHASH_MAX_SIZE extends DataConstraintException("SIGNINDTO_PASSWORDHASH_MAX_SIZE")

object SIGNINDTO_PASSWORDHASH_IS_REQUIRED extends DataConstraintException("SIGNINDTO_PASSWORDHASH_IS_REQUIRED")

case class AuthenticationCodeDto(authenticationCode: String) {
      
    if (authenticationCode == null) throw AUTHENTICATIONCODEDTO_AUTHENTICATIONCODE_IS_REQUIRED

    if (authenticationCode.size < 0) throw AUTHENTICATIONCODEDTO_AUTHENTICATIONCODE_MIN_SIZE
    if (authenticationCode.size > 128) throw AUTHENTICATIONCODEDTO_AUTHENTICATIONCODE_MAX_SIZE
}

object AuthenticationCodeDto {
  val AUTHENTICATIONCODE: String = "authenticationCode"
}

object AUTHENTICATIONCODEDTO_AUTHENTICATIONCODE_MIN_SIZE extends DataConstraintException("AUTHENTICATIONCODEDTO_AUTHENTICATIONCODE_MIN_SIZE")

object AUTHENTICATIONCODEDTO_AUTHENTICATIONCODE_MAX_SIZE extends DataConstraintException("AUTHENTICATIONCODEDTO_AUTHENTICATIONCODE_MAX_SIZE")

object AUTHENTICATIONCODEDTO_AUTHENTICATIONCODE_IS_REQUIRED extends DataConstraintException("AUTHENTICATIONCODEDTO_AUTHENTICATIONCODE_IS_REQUIRED")
