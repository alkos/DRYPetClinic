package eu.execom.dry.petclinic.service

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.hazelcast.config.Config
import com.hazelcast.config.GroupConfig
import com.hazelcast.core.Hazelcast
import eu.execom.dry.petclinic.persistence.SlickPersistenceConfiguration

trait ServiceConfiguration extends SlickPersistenceConfiguration {

  def smtpUrl: String
  def smtpPort: Int
  def smtpUserName: String
  def smtpPassword: String
  def smtpSslOnConnect: Boolean
  lazy val mailSender = new MailSender(smtpUrl, smtpPort, smtpUserName, smtpPassword, smtpSslOnConnect)

  lazy val passwordEncoder = new PasswordEncoder

  lazy val facebookApiConsumer: FacebookApiConsumer = new FacebookApiConsumer()

  def googleWebClientId: String
  def googleWebClientSecret: String
  lazy val netHttpTransport = new NetHttpTransport()
  lazy val jacksonFactory = new JacksonFactory()
  lazy val googleApiConsumer: GoogleApiConsumer = new GoogleApiConsumer(googleWebClientId, googleWebClientSecret, netHttpTransport, jacksonFactory)

  def appEmail: String
  def appName: String
  def appUrl: String
  lazy val securedService: SecuredService = new SecuredService(userDao, userSessionDao, passwordEncoder, mailSender, facebookApiConsumer, googleApiConsumer, appEmail, appName, appUrl)

  def hazelcastGroupName: String
  def hazelcastGroupPassword: String
  def hazelcastLoggingType: String

  val hazelcastGroupConfig = new GroupConfig()
  hazelcastGroupConfig.setName(hazelcastGroupName)
  hazelcastGroupConfig.setName(hazelcastGroupPassword)
  val hazelcastConfig = new Config()
  hazelcastConfig.setGroupConfig(hazelcastGroupConfig)
  hazelcastConfig.setProperty("hazelcast.logging.type", hazelcastLoggingType)
  lazy val hazelcast = Hazelcast.newHazelcastInstance(hazelcastConfig)

  lazy val eventBus = new EventBus(hazelcast)

  //services
  lazy val userService: UserService = new UserService(userDao, eventBus)
  lazy val userSessionService: UserSessionService = new UserSessionService(userSessionDao)
  lazy val roleService: RoleService = new RoleService(roleDao)
  lazy val permissionService: PermissionService = new PermissionService(permissionDao)
  lazy val ownerService: ownerService = new ownerService(ownerDao)
  lazy val petTypeService: PetTypeService = new PetTypeService(petTypeDao)
  lazy val petHistoryService: PetHistoryService = new PetHistoryService(petHistoryDao)
  lazy val petService: PetService = new PetService(petDao, petHistoryService)
  lazy val vetSpecialtyService: VetSpecialtyService = new VetSpecialtyService(vetSpecialtyDao)
  lazy val vetService: VetService = new VetService(vetDao)
  lazy val vetSpecialtiesService: VetSpecialtiesService = new VetSpecialtiesService(vetSpecialtiesDao)
  lazy val visitService: VisitService = new VisitService(visitDao)
}
