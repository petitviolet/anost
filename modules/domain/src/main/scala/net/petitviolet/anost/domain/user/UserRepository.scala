package net.petitviolet.anost.domain.user

import net.petitviolet.anost.domain.support.Repository
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli

trait UserRepository extends Repository[User] {

  def login()(implicit ctx: AppContext): Kleisli[Future, (Email, Password), Option[(User, AuthToken)]]

  def findByToken(implicit ctx: AppContext): Kleisli[Future, AuthTokenValue, User]

  def generateToken(implicit ctx: AppContext): Kleisli[Future, User, AuthToken]

  def findByIds(implicit ctx: AppContext): Kleisli[Future, Seq[Id[User]], Seq[User]]
}

trait UsesUserRepository {
  val userRepository: UserRepository
}
