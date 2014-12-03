package eu.execom.dry.petclinic.persistence

trait Enum {

  def name: String

}

case class SortOrder(name: String) extends Enum

object SortOrder {
  val ASC = SortOrder("ASC")
  val DESC = SortOrder("DESC")
  val values: List[SortOrder] = ASC :: DESC :: Nil

  def withName(name:String):SortOrder = values.find(_.name == name).get
}

case class UserRole(name: String) extends Enum

object UserRole {
  val ADMIN = UserRole("ADMIN")
  val USER = UserRole("USER")
  val values: List[UserRole] = ADMIN :: USER :: Nil

  def withName(name:String):UserRole = values.find(_.name == name).get
}
