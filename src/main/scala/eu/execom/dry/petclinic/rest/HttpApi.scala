package eu.execom.dry.petclinic.rest

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.persistence._
import org.joda.time.DateTime
import org.json4s._
import org.scalatra.GZipSupport
import org.scalatra.json._

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.slick.jdbc.JdbcBackend.Database

class HttpApi(val slickDb: Database, val authenticationApi: AuthenticationApi, val userApi: UserApi) extends AbstractSecuredServlet with JacksonJsonSupport with GZipSupport with CacheControlSupport {

  before() {
    contentType = formats("json")
  }


  case class CreateUserBodyDTO(username: String, password: String)
  post("/users") {
    slickDb.withTransaction {  implicit session: SlickSession =>
      logger.trace("Rest url: /users type: POST")

      val body:CreateUserBodyDTO = parsedBody.extract[CreateUserBodyDTO]
      logger.trace("Body:" + body)
      val authenticationCode: String = securityToken

      val response = userApi.createUser(new CreateUserDto(body.username, body.password), authenticationCode).get
      logger.trace(s"Response: $response")
      response
    }
  }

  get("/users/:id") {
    slickDb.withSession {  implicit session: SlickSession =>
      logger.trace("Rest url: /users/:id type: GET")
      noCache()

      val id: Int = params.as[Int]("id")
      logger.trace("Id:" + id)
      val authenticationCode: String = securityToken

      val response = userApi.readUser(new ReadUserDto(id), authenticationCode).get
      logger.trace(s"Response: $response")
      response
    }
  }

  case class UpdateUserBodyDTO(role: UserRole, password: Option[String])
  put("/users/:id") {
    slickDb.withTransaction {  implicit session: SlickSession =>
      logger.trace("Rest url: /users/:id type: PUT")

      val id: Int = params.as[Int]("id")
      logger.trace("Id:" + id)
      val body:UpdateUserBodyDTO = parsedBody.extract[UpdateUserBodyDTO]
      logger.trace("Body:" + body)
      val authenticationCode: String = securityToken

      val response = userApi.updateUser(new UpdateUserDto(id, body.role, body.password), authenticationCode).get
      logger.trace(s"Response: $response")
      response
    }
  }

  delete("/users/:id") {
    slickDb.withTransaction {  implicit session: SlickSession =>
      logger.trace("Rest url: /users/:id type: DELETE")

      val id: Int = params.as[Int]("id")
      logger.trace("Id:" + id)
      val authenticationCode: String = securityToken

      val response = userApi.deleteUser(new ReadUserDto(id), authenticationCode).get
      logger.trace(s"Response: $response")
      response
    }
  }

  get("/users") {
    slickDb.withSession {  implicit session: SlickSession =>
      logger.trace("Rest url: /users type: GET")
      noCache()

      val from: Int = params.as[Int]("from")
      logger.trace("From:" + from)
      val maxRowCount: Int = params.as[Int]("maxRowCount")
      logger.trace("MaxRowCount:" + maxRowCount)
      val authenticationCode: String = securityToken

      val response = userApi.allUsers(new AllUsersDto(from, maxRowCount), authenticationCode).get
      logger.trace(s"Response: $response")
      response
    }
  }

  case class SignUpBodyDTO(username: String, passwordHash: String)
  post("/signUp") {
    slickDb.withSession {  implicit session: SlickSession =>
      logger.trace("Rest url: /signUp type: POST")

      val body:SignUpBodyDTO = parsedBody.extract[SignUpBodyDTO]
      logger.trace("Body:" + body)

      val response = authenticationApi.signUp(new SignUpDto(body.username, body.passwordHash)).get
      logger.trace(s"Response: $response")
      securityToken = response.authenticationCode
      response
    }
  }

  case class SignInBodyDTO(username: String, passwordHash: String)
  post("/signIn") {
    slickDb.withSession {  implicit session: SlickSession =>
      logger.trace("Rest url: /signIn type: POST")

      val body:SignInBodyDTO = parsedBody.extract[SignInBodyDTO]
      logger.trace("Body:" + body)

      val response = authenticationApi.signIn(new SignInDto(body.username, body.passwordHash)).get
      logger.trace(s"Response: $response")
      securityToken = response.authenticationCode
      response
    }
  }

  post("/signOut") {
    slickDb.withTransaction {  implicit session: SlickSession =>
      logger.trace("Rest url: /signOut type: POST")

      val authenticationCode: String = securityToken

      val response = authenticationApi.signOut(authenticationCode).get
      logger.trace(s"Response: $response")
      securityToken = ""
      response
    }
  }

  case class AuthenticateBodyDTO(authenticationCode: String)
  post("/authenticate") {
    slickDb.withTransaction {  implicit session: SlickSession =>
      logger.trace("Rest url: /authenticate type: POST")

      val body:AuthenticateBodyDTO = parsedBody.extract[AuthenticateBodyDTO]
      logger.trace("Body:" + body)

      val response = authenticationApi.authenticate(new AuthenticationCodeDto(body.authenticationCode)).get
      logger.trace(s"Response: $response")
      response
    }
  }
}
