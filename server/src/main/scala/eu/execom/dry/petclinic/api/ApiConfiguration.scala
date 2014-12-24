package eu.execom.dry.petclinic.api

import eu.execom.dry.petclinic.service._

trait ApiConfiguration extends ServiceConfiguration {

  lazy val authenticationApi: AuthenticationApi = new AuthenticationApi(userDao, securedService, permissionDao)
  lazy val userApi: UserApi = new UserApi(userDao, userService, securedService, permissionDao)
}
