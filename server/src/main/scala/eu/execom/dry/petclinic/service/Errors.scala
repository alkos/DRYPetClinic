package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.util._

object CREDENTIALS_ARE_INVALID extends UnauthorizedException("CREDENTIALS_ARE_INVALID")

object INSUFFICIENT_RIGHTS extends UnauthorizedException("INSUFFICIENT_RIGHTS")
