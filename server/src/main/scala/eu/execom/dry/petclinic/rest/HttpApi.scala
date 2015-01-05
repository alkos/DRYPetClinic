package eu.execom.dry.petclinic.rest

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.service.EventBus
import eu.execom.dry.petclinic.util._
import org.joda.time.DateTime
import org.json4s._
import org.scalatra.GZipSupport
import org.scalatra.json._

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.slick.jdbc.JdbcBackend.Database

object JSON_REQUEST_REQUIRED_EXCEPTION extends BadRequestException("JSON_REQUEST_REQUIRED_EXCEPTION")

class HttpApi(val slickDb: Database, val eventBus: EventBus, val authenticationApi: AuthenticationApi, val userApi: UserApi) extends AbstractSecuredServlet with ApiCaller with JacksonJsonSupport with GZipSupport with CacheControlSupport {

  before() {
    contentType = formats("json")
  }

  post("/users") {
    transaction  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /users type: POST")

      if (requestFormat !="json") throw JSON_REQUEST_REQUIRED_EXCEPTION
      val body:CreateUserDto = parsedBody match {
        case JNothing => throw JSON_REQUEST_REQUIRED_EXCEPTION
        case jsonBody => jsonBody.extract[CreateUserDto]
      }
      logger.trace("Body:" + body)
      val authenticationCode: String = securityToken

      val response = userApi.createUser(body, authenticationCode)(slickSession).get
      logger.trace(s"Response: $response")
      response
    }
  }

  get("/users/:id") {
    session  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /users/:id type: GET")
      noCache()

      val id: Int = params.getAs[Int]("id").getOrElse(throw new BadRequestException("MISSING_ID_PROPERTY"))
      logger.trace("Id:" + id)
      val authenticationCode: String = securityToken

      val response = userApi.readUser(new ReadUserDto(id), authenticationCode)(slickSession).get
      logger.trace(s"Response: $response")
      response
    }
  }

  put("/users/:id") {
    transaction  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /users/:id type: PUT")

      val id: Int = params.getAs[Int]("id").getOrElse(throw new BadRequestException("MISSING_ID_PROPERTY"))
      logger.trace("Id:" + id)
      if (requestFormat !="json") throw JSON_REQUEST_REQUIRED_EXCEPTION
      val body:UpdateUserBodyDTO = parsedBody match {
        case JNothing => throw JSON_REQUEST_REQUIRED_EXCEPTION
        case jsonBody => jsonBody.extract[UpdateUserBodyDTO]
      }
      logger.trace("Body:" + body)
      val authenticationCode: String = securityToken

      val response = userApi.updateUser(new UpdateUserDto(id, body.role, body.password), authenticationCode)(slickSession).get
      logger.trace(s"Response: $response")
      response
    }
  }

  delete("/users/:id") {
    transaction  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /users/:id type: DELETE")

      val id: Int = params.getAs[Int]("id").getOrElse(throw new BadRequestException("MISSING_ID_PROPERTY"))
      logger.trace("Id:" + id)
      val authenticationCode: String = securityToken

      val response = userApi.deleteUser(new ReadUserDto(id), authenticationCode)(slickSession).get
      logger.trace(s"Response: $response")
      response
    }
  }

  get("/users") {
    session  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /users type: GET")
      noCache()

      val from: Int = params.getAs[Int]("from").getOrElse(throw new BadRequestException("MISSING_FROM_PROPERTY"))
      logger.trace("From:" + from)
      val maxRowCount: Int = params.getAs[Int]("maxRowCount").getOrElse(throw new BadRequestException("MISSING_MAXROWCOUNT_PROPERTY"))
      logger.trace("MaxRowCount:" + maxRowCount)
      val authenticationCode: String = securityToken

      val response = userApi.users(new UsersDto(from, maxRowCount), authenticationCode)(slickSession).get
      logger.trace(s"Response: $response")
      response
    }
  }

  get("/adminUsers") {
    session  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /adminUsers type: GET")
      noCache()

      val role: Int = params.getAs[Int]("role").getOrElse(throw new BadRequestException("MISSING_ROLE_PROPERTY"))
      logger.trace("Role:" + role)
      val from: Int = params.getAs[Int]("from").getOrElse(throw new BadRequestException("MISSING_FROM_PROPERTY"))
      logger.trace("From:" + from)
      val maxRowCount: Int = params.getAs[Int]("maxRowCount").getOrElse(throw new BadRequestException("MISSING_MAXROWCOUNT_PROPERTY"))
      logger.trace("MaxRowCount:" + maxRowCount)
      val authenticationCode: String = securityToken

      val response = userApi.adminUsers(new AdminUsersDto(role, from, maxRowCount), authenticationCode)(slickSession).get
      logger.trace(s"Response: $response")
      response
    }
  }

  post("/signUp") {
    session  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /signUp type: POST")

      if (requestFormat !="json") throw JSON_REQUEST_REQUIRED_EXCEPTION
      val body:SignUpDto = parsedBody match {
        case JNothing => throw JSON_REQUEST_REQUIRED_EXCEPTION
        case jsonBody => jsonBody.extract[SignUpDto]
      }
      logger.trace("Body:" + body)

      val response = authenticationApi.signUp(body)(slickSession).get
      logger.trace(s"Response: $response")
      securityToken = response.accessToken
      response
    }
  }

  post("/signIn") {
    session  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /signIn type: POST")

      if (requestFormat !="json") throw JSON_REQUEST_REQUIRED_EXCEPTION
      val body:SignInDto = parsedBody match {
        case JNothing => throw JSON_REQUEST_REQUIRED_EXCEPTION
        case jsonBody => jsonBody.extract[SignInDto]
      }
      logger.trace("Body:" + body)

      val response = authenticationApi.signIn(body)(slickSession).get
      logger.trace(s"Response: $response")
      securityToken = response.accessToken
      response
    }
  }

  post("/signOut") {
    transaction  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /signOut type: POST")

      val authenticationCode: String = securityToken

      val response = authenticationApi.signOut(authenticationCode)(slickSession).get
      logger.trace(s"Response: $response")
      securityToken = ""
      response
    }
  }

  post("/authenticate") {
    transaction  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /authenticate type: POST")

      if (requestFormat !="json") throw JSON_REQUEST_REQUIRED_EXCEPTION
      val body:AccessTokenDto = parsedBody match {
        case JNothing => throw JSON_REQUEST_REQUIRED_EXCEPTION
        case jsonBody => jsonBody.extract[AccessTokenDto]
      }
      logger.trace("Body:" + body)

      val response = authenticationApi.authenticate(body)(slickSession).get
      logger.trace(s"Response: $response")
      response
    }
  }

  post("/refreshToken") {
    transaction  { (slickSession: SlickSession) =>
      logger.trace("Rest url: /refreshToken type: POST")

      if (requestFormat !="json") throw JSON_REQUEST_REQUIRED_EXCEPTION
      val body:RefreshTokenDto = parsedBody match {
        case JNothing => throw JSON_REQUEST_REQUIRED_EXCEPTION
        case jsonBody => jsonBody.extract[RefreshTokenDto]
      }
      logger.trace("Body:" + body)

      val response = authenticationApi.refreshToken(body)(slickSession).get
      logger.trace(s"Response: $response")
      securityToken = response.accessToken
      response
    }
  }
}

case class UpdateUserBodyDTO(role: Int, password: Option[String]) {

if (password.isDefined) {
    if (password.get.size < 0) throw UPDATE_USER_BODY_DTO_PASSWORD_MIN_SIZE
    if (password.get.size > 1024) throw UPDATE_USER_BODY_DTO_PASSWORD_MAX_SIZE
  }
}

object UpdateUserBodyDTO {
  val ROLE: String = "role"
  val PASSWORD: String = "password"
}

object UPDATE_USER_BODY_DTO_PASSWORD_MIN_SIZE extends DataConstraintException("UPDATE_USER_BODY_DTO_PASSWORD_MIN_SIZE")

object UPDATE_USER_BODY_DTO_PASSWORD_MAX_SIZE extends DataConstraintException("UPDATE_USER_BODY_DTO_PASSWORD_MAX_SIZE")
