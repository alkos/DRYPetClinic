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

    val (user, client) = securedService.signUp(signUpDto.username, signUpDto.passwordHash).get
    new AuthenticationResponseDto(user.username, user.roleId, client.accessToken, client.refreshToken)
  }

  def signIn(signInDto: SignInDto)(implicit slickSession: SlickSession): Try[AuthenticationResponseDto] = Try {
    logger.trace(s".signIn(signInDto: $signInDto)")

    val (user, client) = securedService.signIn(signInDto.username, signInDto.passwordHash).get
    new AuthenticationResponseDto(user.username, user.roleId, client.accessToken, client.refreshToken)
  }

  def signOut(authenticationCode: String)(implicit slickSession: SlickSession): Try[Unit] = Try {
    logger.trace(s".signOut(authenticationCode: $authenticationCode)")
    secure(authenticationCode) { implicit user: User =>

      securedService.signOut(authenticationCode)
    }
  }

  def authenticate(authenticationDto: AccessTokenDto)(implicit slickSession: SlickSession): Try[AuthenticationResponseDto] = Try {
    logger.trace(s".authenticate(authenticationDto: $authenticationDto)")

    val (user, client) = securedService.authenticate(authenticationDto.accessToken).get
    new AuthenticationResponseDto(user.username, user.roleId, client.accessToken, client.refreshToken)
  }

  def refreshToken(refreshTokenDto: RefreshTokenDto)(implicit slickSession: SlickSession): Try[AuthenticationResponseDto] = Try {
    logger.trace(s".refreshToken(refreshTokenDto: $refreshTokenDto)")

    val (user, client) = securedService.refreshToken(refreshTokenDto.refreshToken).get
    new AuthenticationResponseDto(user.username, user.roleId, client.accessToken, client.refreshToken)
  }

}

case class AuthenticationResponseDto(username: String, roleId: Int, accessToken: String, refreshToken: String) {

  if (username == null) throw AUTHENTICATION_RESPONSE_DTO_USERNAME_IS_REQUIRED
  if (username.size < 0) throw AUTHENTICATION_RESPONSE_DTO_USERNAME_MIN_SIZE
  if (username.size > 1024) throw AUTHENTICATION_RESPONSE_DTO_USERNAME_MAX_SIZE

  if (accessToken == null) throw AUTHENTICATION_RESPONSE_DTO_ACCESS_TOKEN_IS_REQUIRED
  if (accessToken.size < 0) throw AUTHENTICATION_RESPONSE_DTO_ACCESS_TOKEN_MIN_SIZE
  if (accessToken.size > 128) throw AUTHENTICATION_RESPONSE_DTO_ACCESS_TOKEN_MAX_SIZE

  if (refreshToken == null) throw AUTHENTICATION_RESPONSE_DTO_REFRESH_TOKEN_IS_REQUIRED
  if (refreshToken.size < 0) throw AUTHENTICATION_RESPONSE_DTO_REFRESH_TOKEN_MIN_SIZE
  if (refreshToken.size > 128) throw AUTHENTICATION_RESPONSE_DTO_REFRESH_TOKEN_MAX_SIZE
}

object AuthenticationResponseDto {
  val USERNAME: String = "username"
  val ROLEID: String = "roleId"
  val ACCESSTOKEN: String = "accessToken"
  val REFRESHTOKEN: String = "refreshToken"
}

object AUTHENTICATION_RESPONSE_DTO_USERNAME_IS_REQUIRED extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_USERNAME_IS_REQUIRED")

object AUTHENTICATION_RESPONSE_DTO_USERNAME_MIN_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_USERNAME_MIN_SIZE")

object AUTHENTICATION_RESPONSE_DTO_USERNAME_MAX_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_USERNAME_MAX_SIZE")

object AUTHENTICATION_RESPONSE_DTO_ACCESS_TOKEN_IS_REQUIRED extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_ACCESS_TOKEN_IS_REQUIRED")

object AUTHENTICATION_RESPONSE_DTO_ACCESS_TOKEN_MIN_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_ACCESS_TOKEN_MIN_SIZE")

object AUTHENTICATION_RESPONSE_DTO_ACCESS_TOKEN_MAX_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_ACCESS_TOKEN_MAX_SIZE")

object AUTHENTICATION_RESPONSE_DTO_REFRESH_TOKEN_IS_REQUIRED extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_REFRESH_TOKEN_IS_REQUIRED")

object AUTHENTICATION_RESPONSE_DTO_REFRESH_TOKEN_MIN_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_REFRESH_TOKEN_MIN_SIZE")

object AUTHENTICATION_RESPONSE_DTO_REFRESH_TOKEN_MAX_SIZE extends DataConstraintException("AUTHENTICATION_RESPONSE_DTO_REFRESH_TOKEN_MAX_SIZE")

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

object SIGN_UP_DTO_USERNAME_IS_REQUIRED extends DataConstraintException("SIGN_UP_DTO_USERNAME_IS_REQUIRED")

object SIGN_UP_DTO_USERNAME_MIN_SIZE extends DataConstraintException("SIGN_UP_DTO_USERNAME_MIN_SIZE")

object SIGN_UP_DTO_USERNAME_MAX_SIZE extends DataConstraintException("SIGN_UP_DTO_USERNAME_MAX_SIZE")

object SIGN_UP_DTO_PASSWORD_HASH_IS_REQUIRED extends DataConstraintException("SIGN_UP_DTO_PASSWORD_HASH_IS_REQUIRED")

object SIGN_UP_DTO_PASSWORD_HASH_MIN_SIZE extends DataConstraintException("SIGN_UP_DTO_PASSWORD_HASH_MIN_SIZE")

object SIGN_UP_DTO_PASSWORD_HASH_MAX_SIZE extends DataConstraintException("SIGN_UP_DTO_PASSWORD_HASH_MAX_SIZE")

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

object SIGN_IN_DTO_USERNAME_IS_REQUIRED extends DataConstraintException("SIGN_IN_DTO_USERNAME_IS_REQUIRED")

object SIGN_IN_DTO_USERNAME_MIN_SIZE extends DataConstraintException("SIGN_IN_DTO_USERNAME_MIN_SIZE")

object SIGN_IN_DTO_USERNAME_MAX_SIZE extends DataConstraintException("SIGN_IN_DTO_USERNAME_MAX_SIZE")

object SIGN_IN_DTO_PASSWORD_HASH_IS_REQUIRED extends DataConstraintException("SIGN_IN_DTO_PASSWORD_HASH_IS_REQUIRED")

object SIGN_IN_DTO_PASSWORD_HASH_MIN_SIZE extends DataConstraintException("SIGN_IN_DTO_PASSWORD_HASH_MIN_SIZE")

object SIGN_IN_DTO_PASSWORD_HASH_MAX_SIZE extends DataConstraintException("SIGN_IN_DTO_PASSWORD_HASH_MAX_SIZE")

case class AccessTokenDto(accessToken: String) {

  if (accessToken == null) throw ACCESS_TOKEN_DTO_ACCESS_TOKEN_IS_REQUIRED
  if (accessToken.size < 0) throw ACCESS_TOKEN_DTO_ACCESS_TOKEN_MIN_SIZE
  if (accessToken.size > 128) throw ACCESS_TOKEN_DTO_ACCESS_TOKEN_MAX_SIZE
}

object AccessTokenDto {
  val ACCESSTOKEN: String = "accessToken"
}

object ACCESS_TOKEN_DTO_ACCESS_TOKEN_IS_REQUIRED extends DataConstraintException("ACCESS_TOKEN_DTO_ACCESS_TOKEN_IS_REQUIRED")

object ACCESS_TOKEN_DTO_ACCESS_TOKEN_MIN_SIZE extends DataConstraintException("ACCESS_TOKEN_DTO_ACCESS_TOKEN_MIN_SIZE")

object ACCESS_TOKEN_DTO_ACCESS_TOKEN_MAX_SIZE extends DataConstraintException("ACCESS_TOKEN_DTO_ACCESS_TOKEN_MAX_SIZE")

case class RefreshTokenDto(refreshToken: String) {

  if (refreshToken == null) throw REFRESH_TOKEN_DTO_REFRESH_TOKEN_IS_REQUIRED
  if (refreshToken.size < 0) throw REFRESH_TOKEN_DTO_REFRESH_TOKEN_MIN_SIZE
  if (refreshToken.size > 128) throw REFRESH_TOKEN_DTO_REFRESH_TOKEN_MAX_SIZE
}

object RefreshTokenDto {
  val REFRESHTOKEN: String = "refreshToken"
}

object REFRESH_TOKEN_DTO_REFRESH_TOKEN_IS_REQUIRED extends DataConstraintException("REFRESH_TOKEN_DTO_REFRESH_TOKEN_IS_REQUIRED")

object REFRESH_TOKEN_DTO_REFRESH_TOKEN_MIN_SIZE extends DataConstraintException("REFRESH_TOKEN_DTO_REFRESH_TOKEN_MIN_SIZE")

object REFRESH_TOKEN_DTO_REFRESH_TOKEN_MAX_SIZE extends DataConstraintException("REFRESH_TOKEN_DTO_REFRESH_TOKEN_MAX_SIZE")
