package eu.execom.dry.petclinic.api

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.service.SecuredService
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
      
    if (username == null) throw AUTHENTICATION_RESPONSE_DTO_USERNAME_IS_REQUIRED

    if (username.size < 0) throw AUTHENTICATION_RESPONSE_DTO_USERNAME_MIN_SIZE
    if (username.size > 1024) throw AUTHENTICATION_RESPONSE_DTO_USERNAME_MAX_SIZE    
    if (authenticationCode == null) throw AUTHENTICATION_RESPONSE_DTO_AUTHENTICATION_CODE_IS_REQUIRED

    if (authenticationCode.size < 0) throw AUTHENTICATION_RESPONSE_DTO_AUTHENTICATION_CODE_MIN_SIZE
    if (authenticationCode.size > 128) throw AUTHENTICATION_RESPONSE_DTO_AUTHENTICATION_CODE_MAX_SIZE
}

object AuthenticationResponseDto {
  val USERNAME: String = "username"
  val ROLEID: String = "roleId"
  val AUTHENTICATIONCODE: String = "authenticationCode"
}

object AUTHENTICATION_RESPONSE_DTO_USERNAME_MIN_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_USERNAME_MIN_SIZE")

object AUTHENTICATION_RESPONSE_DTO_USERNAME_MAX_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_USERNAME_MAX_SIZE")

object AUTHENTICATION_RESPONSE_DTO_USERNAME_IS_REQUIRED extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_USERNAME_IS_REQUIRED")

object AUTHENTICATION_RESPONSE_DTO_AUTHENTICATION_CODE_MIN_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_AUTHENTICATION_CODE_MIN_SIZE")

object AUTHENTICATION_RESPONSE_DTO_AUTHENTICATION_CODE_MAX_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_AUTHENTICATION_CODE_MAX_SIZE")

object AUTHENTICATION_RESPONSE_DTO_AUTHENTICATION_CODE_IS_REQUIRED extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_AUTHENTICATION_CODE_IS_REQUIRED")

case class SignUpDto(username: String, passwordHash: String) {
      
    if (username == null) throw SIGN_UP_DTO_USERNAME_IS_REQUIRED

    if (username.size < 0) throw SIGN_UP_DTO_USERNAME_MIN_SIZE
    if (username.size > 1024) throw SIGN_UP_DTO_USERNAME_MAX_SIZE    
    if (passwordHash == null) throw SIGN_UP_DTO_PASSWORD_HASH_IS_REQUIRED

    if (passwordHash.size < 0) throw SIGN_UP_DTO_PASSWORD_HASH_MIN_SIZE
    if (passwordHash.size > 1024) throw SIGN_UP_DTO_PASSWORD_HASH_MAX_SIZE
}

object SignUpDto {
  val USERNAME: String = "username"
  val PASSWORDHASH: String = "passwordHash"
}

object SIGN_UP_DTO_USERNAME_MIN_SIZE extends DataConstraintException("SIGN_UP_DTO_USERNAME_MIN_SIZE")

object SIGN_UP_DTO_USERNAME_MAX_SIZE extends DataConstraintException("SIGN_UP_DTO_USERNAME_MAX_SIZE")

object SIGN_UP_DTO_USERNAME_IS_REQUIRED extends DataConstraintException("SIGN_UP_DTO_USERNAME_IS_REQUIRED")

object SIGN_UP_DTO_PASSWORD_HASH_MIN_SIZE extends DataConstraintException("SIGN_UP_DTO_PASSWORD_HASH_MIN_SIZE")

object SIGN_UP_DTO_PASSWORD_HASH_MAX_SIZE extends DataConstraintException("SIGN_UP_DTO_PASSWORD_HASH_MAX_SIZE")

object SIGN_UP_DTO_PASSWORD_HASH_IS_REQUIRED extends DataConstraintException("SIGN_UP_DTO_PASSWORD_HASH_IS_REQUIRED")

case class SignInDto(username: String, passwordHash: String) {
      
    if (username == null) throw SIGN_IN_DTO_USERNAME_IS_REQUIRED

    if (username.size < 0) throw SIGN_IN_DTO_USERNAME_MIN_SIZE
    if (username.size > 1024) throw SIGN_IN_DTO_USERNAME_MAX_SIZE    
    if (passwordHash == null) throw SIGN_IN_DTO_PASSWORD_HASH_IS_REQUIRED

    if (passwordHash.size < 0) throw SIGN_IN_DTO_PASSWORD_HASH_MIN_SIZE
    if (passwordHash.size > 1024) throw SIGN_IN_DTO_PASSWORD_HASH_MAX_SIZE
}

object SignInDto {
  val USERNAME: String = "username"
  val PASSWORDHASH: String = "passwordHash"
}

object SIGN_IN_DTO_USERNAME_MIN_SIZE extends DataConstraintException("SIGN_IN_DTO_USERNAME_MIN_SIZE")

object SIGN_IN_DTO_USERNAME_MAX_SIZE extends DataConstraintException("SIGN_IN_DTO_USERNAME_MAX_SIZE")

object SIGN_IN_DTO_USERNAME_IS_REQUIRED extends DataConstraintException("SIGN_IN_DTO_USERNAME_IS_REQUIRED")

object SIGN_IN_DTO_PASSWORD_HASH_MIN_SIZE extends DataConstraintException("SIGN_IN_DTO_PASSWORD_HASH_MIN_SIZE")

object SIGN_IN_DTO_PASSWORD_HASH_MAX_SIZE extends DataConstraintException("SIGN_IN_DTO_PASSWORD_HASH_MAX_SIZE")

object SIGN_IN_DTO_PASSWORD_HASH_IS_REQUIRED extends DataConstraintException("SIGN_IN_DTO_PASSWORD_HASH_IS_REQUIRED")

case class AuthenticationCodeDto(authenticationCode: String) {
      
    if (authenticationCode == null) throw AUTHENTICATION_CODE_DTO_AUTHENTICATION_CODE_IS_REQUIRED

    if (authenticationCode.size < 0) throw AUTHENTICATION_CODE_DTO_AUTHENTICATION_CODE_MIN_SIZE
    if (authenticationCode.size > 128) throw AUTHENTICATION_CODE_DTO_AUTHENTICATION_CODE_MAX_SIZE
}

object AuthenticationCodeDto {
  val AUTHENTICATIONCODE: String = "authenticationCode"
}

object AUTHENTICATION_CODE_DTO_AUTHENTICATION_CODE_MIN_SIZE extends DataConstraintException("AUTHENTICATION_CODE_DTO_AUTHENTICATION_CODE_MIN_SIZE")

object AUTHENTICATION_CODE_DTO_AUTHENTICATION_CODE_MAX_SIZE extends DataConstraintException("AUTHENTICATION_CODE_DTO_AUTHENTICATION_CODE_MAX_SIZE")

object AUTHENTICATION_CODE_DTO_AUTHENTICATION_CODE_IS_REQUIRED extends DataConstraintException("AUTHENTICATION_CODE_DTO_AUTHENTICATION_CODE_IS_REQUIRED")
