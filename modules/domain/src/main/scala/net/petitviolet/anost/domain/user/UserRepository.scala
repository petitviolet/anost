package net.petitviolet.anost.domain.user

import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.support.Repository
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli

trait UserRepository extends Repository[User] {
  def post(post: Post)(implicit ctx: AppContext): Kleisli[Future, User, Post]
}

trait UsesUserRepository {
  val userRepository: UserRepository
}
