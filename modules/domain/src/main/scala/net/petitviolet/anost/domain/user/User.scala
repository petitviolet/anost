package net.petitviolet.anost.domain.user

import net.petitviolet.anost.domain._
import net.petitviolet.anost.domain.support.Entity
import net.petitviolet.anost.support.Id
import net.petitviolet.operator._

import scalaz.Scalaz._

case class User(id: Id[User], name: UserName, email: Email, password: Password) extends Entity {
  type ID = Id[User]
}

case class UserName(value: String) extends AnyVal

case class Password(value: String) extends AnyVal

case class Email(value: String) extends AnyVal

object User {

  import UserSpecification._

  def create(name: String, email: String, password: String): Validated[User] =
    (nameSpec(name) |@|
      emailSpec(email) |@|
      passwordSpec(password))(User(Id.generate(), _, _, _))

}

private object UserSpecification {
  val nameSpec: String ::> UserName = name =>
    if (name.isEmpty) fail("username should not be empty.")
    else success(UserName(name))

  val passwordSpec: String ::> Password = pass =>
    if (pass.isEmpty or pass.length <= 4) fail("password should be longer than 4.")
    else success(Password(pass))

  val emailSpec: String ::> Email = email =>
    if (email.isEmpty) fail("email should not be empty.")
    else success(Email(email))

}
