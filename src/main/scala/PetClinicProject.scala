import eu.execom.dry.generator.Project
import eu.execom.dry.generator.api.CrudProperty

object PetClinicProject extends Project("eu.execom.dry", "petclinic") {

  developer("dvesin")

  //enumerations
  val userRoleEnum = enumeration("UserRole")
  val ADMIN = userRoleEnum.value("ADMIN")
  val USER = userRoleEnum.value("USER")

  //model
  val user = sqlModel("User")
  val userId = user.int("id").primaryKey
  val (userAuthCode, userRole) = user.roleSecured("role", userRoleEnum)
  val (userUsername, userPassword) = user.signInWithUserName("username", "passwordHash")

  val owner = sqlModel("owner")
  val ownerId = owner.int("id").primaryKey
  val ownerFirstName = owner.text("firstName").minSize(1).maxSize(40)
  val ownerLastName = owner.text("lastName").minSize(1).maxSize(60)
  val ownerAddress = owner.text("address").minSize(5).maxSize(100)
  val ownerCity = owner.text("city").minSize(2).maxSize(50)
  val ownerTelephone = owner.text("telephone").minSize(5).maxSize(15)

  val petType = sqlModel("PetType")
  val petTypeId = petType.int("id").primaryKey
  val petTypeName = petType.text("name").minSize(1).maxSize(30)

  val pet = sqlModel("Pet")
  val petId = pet.int("id").primaryKey
  val petName = pet.text("name").minSize(2).maxSize(40)
  val petBirthDate = pet.date("birthDate")
  val petOwner = pet.reference("owner", ownerId)
  val petPetType = pet.reference("petType", petTypeId)

  val vetSpecialty = sqlModel("VetSpecialty")
  val vetSpecialtyId = vetSpecialty.int("id").primaryKey
  val vetSpecialtyName = vetSpecialty.text("name").minSize(3).maxSize(50)

  val vet = sqlModel("Vet")
  val vetId = vet.int("id").primaryKey
  val vetFirstName = vet.text("firstName").minSize(1).maxSize(40)
  val vetLastName = vet.text("lastName").minSize(1).maxSize(60)

  val vetSpecialties = sqlModel("VetSpecialties")
  val vetSpecialtyLinkId = vetSpecialties.int("id").primaryKey
  val vetSpecialtyVet = vetSpecialties.reference("vet", vetId)
  val vetSpecialtySpecialty = vetSpecialties.reference("specialty", vetSpecialtyId)

  val visit = sqlModel("Visit")
  val visitId = visit.int("id").primaryKey
  val visitDate = visit.date("date")
  val visitDescription = visit.text("description").maxSize(1024)
  val visitPet = visit.reference("pet", petId)

  //APIs
  val userAPI = api("UserApi")

  val userCrudApi = userAPI.crud("user", user,
    new CrudProperty("role", userRole).defaultSave(USER),
    new CrudProperty("username", userUsername).noUpdate,
    new CrudProperty("password", userPassword).optionalUpdate.noOverview
  ).roleSecured(ADMIN).rest()

  val allUsersApi = userAPI.pagedFind("allUsers",
    user.query.
      response(userId).
      response(userUsername).
      response(userRole)).secured.restGet("users")

  //WEB
  val roleEnumDropDown = web.enumDropDown(userRoleEnum)
  val usersDropDown = web.apiDropDown(allUsersApi, userId.name, userUsername.name, "user")

  val createUserForm = web.apiForm(userCrudApi.create, "user")
  val updateUserForm = web.apiForm(userCrudApi.update, "user")

  val userView = web.view(userCrudApi.read.responseDto.get, "user")

  val userListView = web.apiListView(allUsersApi, "user")

  def main(args: Array[String]) {
    generate()
  }

}