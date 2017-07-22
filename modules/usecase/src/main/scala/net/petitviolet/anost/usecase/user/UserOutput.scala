package net.petitviolet.anost.usecase.user

import net.petitviolet.anost.domain.user.{ AuthToken, User }
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.Out

case class UserOutput(id: Id[User], name: String, email: String) extends Out

case class AuthTokenOutput(value: String) extends Out

object UserOutput {
  def fromModel(user: User): UserOutput =
    UserOutput(user.id, user.name.value, user.email.value)
}
