package eu.execom.dry.petclinic.api

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.service._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.util._

class AuthenticationApi(val userDao: UserDao, val securedService: SecuredService) extends SecuredApi with Logging {

  def signUp(signUpDto: SignUpDto)(implicit slickSession: SlickSession): Try[AuthenticationResponseDto] = Try {
    logger.trace(s".signUp(signUpDto: $signUpDto)")

    val user = securedService.signUp(signUpDto.username, signUpDto.passwordHash).get
    new AuthenticationResponseDto(user.username, user.role, user.authenticationCode.get)
  }

  def signIn(signInDto: SignInDto)(implicit slickSession: SlickSession): Try[AuthenticationResponseDto] = Try {
    logger.trace(s".signIn(signInDto: $signInDto)")

    val user = securedService.signIn(signInDto.username, signInDto.passwordHash).get
    new AuthenticationResponseDto(user.username, user.role, user.authenticationCode.get)
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
    new AuthenticationResponseDto(user.username, user.role, user.authenticationCode.get)
  }

}

case class AuthenticationResponseDto(username: String, role: UserRole, authenticationCode: String)

object AuthenticationResponseDto {
  val USERNAME: String = "username"
  val ROLE: String = "role"
  val AUTHENTICATIONCODE: String = "authenticationCode"
}

case class SignUpDto(username: String, passwordHash: String)

object SignUpDto {
  val USERNAME: String = "username"
  val PASSWORDHASH: String = "passwordHash"
}

case class SignInDto(username: String, passwordHash: String)

object SignInDto {
  val USERNAME: String = "username"
  val PASSWORDHASH: String = "passwordHash"
}

case class AuthenticationCodeDto(authenticationCode: String)

object AuthenticationCodeDto {
  val AUTHENTICATIONCODE: String = "authenticationCode"
}
