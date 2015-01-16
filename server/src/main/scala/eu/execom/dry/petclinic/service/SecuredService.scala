package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import org.joda.time.DateTime

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.util.Try

class SecuredService(val userDao: UserDao, val userSessionDao: UserSessionDao, val passwordEncoder: PasswordEncoder, val mailSender: MailSender, val facebookApiConsumer: FacebookApiConsumer, val googleApiConsumer: GoogleApiConsumer, val appEmail: String, val appName: String, val appUrl: String) {

  def createUserSession(user: User)(implicit session: SlickSession): UserSession = {
    val userSession = new UserSession(user, passwordEncoder.encode(user.toString, new DateTime()), new DateTime().plusDays(1), passwordEncoder.encode(user.toString, new DateTime()),new DateTime().plusMonths(1))

    userSessionDao.save(userSession)

    userSession
  }

  def signUp(email: String, password: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(email != null && password != null)

    mailSender.sendEmail(email, email, appEmail, appName, "Registration", s"Successful registration for $email!")

    val user = new User
    user.email = email
    user.passwordHash = Some(passwordEncoder.encode(password, email))
    //TODO add other properties
    userDao.save(user)

    val userSession = createUserSession(user)

    (user, userSession)
  }

  def signIn(email: String, password: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(email != null && password != null)

    val user = userDao.findByEmail(email).getOrElse(throw CREDENTIALS_ARE_INVALID)
    val encodedPassword = passwordEncoder.encode(password, email)
    if (!encodedPassword.equals(user.passwordHash.get)) {
      throw CREDENTIALS_ARE_INVALID
    }

    val userSession = createUserSession(user)

    (user, userSession)
  }

  def googleSignIn(googleId: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(googleId != null)

    val user = userDao.findByGoogleId(Some(googleId)) match {
      case Some(existing) => existing
      case None =>
        val googleToken = googleApiConsumer.fetchGoogleUser(googleId).getOrElse(throw CREDENTIALS_ARE_INVALID)
        val newUser = new User
        newUser.googleId = Some(googleId)
        newUser.email = googleToken.getPayload.getEmail
        //TODO add other properties
        userDao.save(newUser)

        newUser
    }

    val userSession = createUserSession(user)

    (user, userSession)
  }

  def facebookSignIn(facebookId: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(facebookId != null)

    val user = userDao.findByFacebookId(Some(facebookId)) match {
      case Some(existing) => existing
      case None =>
        val facebookUser = facebookApiConsumer.fetchFacebookUser(facebookId).getOrElse(throw CREDENTIALS_ARE_INVALID)
        val newUser = new User()
        newUser.email = facebookUser.getEmail
        newUser.facebookId = Some(facebookUser.getId)
        //TODO add other properties
        userDao.save(newUser)

        newUser
    }

    val userSession = createUserSession(user)

    (user, userSession)
  }

  def signOut(accessToken: String)(implicit session: SlickSession): Try[User] = Try {
    require(accessToken != null)

    val userSession = userSessionDao.findByAccessToken(accessToken).getOrElse(throw CREDENTIALS_ARE_INVALID)
    val user = userSession.user

    userSessionDao.deleteById(userSession.id)

    user
  }

  def authenticate(accessToken: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(accessToken != null)

    val userSession = userSessionDao.findByAccessToken(accessToken).getOrElse(throw CREDENTIALS_ARE_INVALID)
    if (userSession.accessTokenExpires.isAfterNow) throw ACCESS_TOKEN_IS_EXPIRED

    val user = userSession.user

    (user, userSession)
  }

  def refreshToken(refreshToken: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(refreshToken != null)

    val userSession = userSessionDao.findByRefreshToken(refreshToken).getOrElse(throw REFRESH_TOKEN_IS_INVALID)
    if (userSession.refreshTokenExpires.isAfterNow) throw REFRESH_TOKEN_IS_EXPIRED

    val user = userSession.user

    userSession.accessToken = passwordEncoder.encode(user.toString, new DateTime())
    userSession.accessTokenExpires = new DateTime().plusDays(1)

    userSession.refreshToken = passwordEncoder.encode(user.toString, new DateTime())
    userSession.refreshTokenExpires = new DateTime().plusMonths(1)

    userSessionDao.update(userSession)

    (user, userSession)
  }

}
