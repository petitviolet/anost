package net.petitviolet.anost.domain.user

import net.petitviolet.anost.domain.support.Repository
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli

trait UserRepository extends Repository[User] {

  def findByToken(implicit ctx: AppContext): Kleisli[Future, AuthToken, Option[User]]

  def generateToken(implicit ctx: AppContext): Kleisli[Future, User, AuthToken]
}

trait UsesUserRepository {
  val userRepository: UserRepository
}
