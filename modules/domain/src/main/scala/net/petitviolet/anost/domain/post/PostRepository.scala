package net.petitviolet.anost.domain.post

import net.petitviolet.anost.domain.support.Repository
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli

trait PostRepository extends Repository[Post] {
  def findByUserId(implicit ctx: AppContext): Kleisli[Future, Id[User], Seq[Post]]

  def findByTitle(implicit ctx: AppContext): Kleisli[Future, String, Seq[Post]]
}

trait UsesPostRepository {
  val postRepository: PostRepository
}
