package net.petitviolet.anost.domain.comment

import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.support.Repository
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli

trait CommentRepository extends Repository[Comment] {
  def findAllByPostId(implicit ctx: AppContext): Kleisli[Future, Id[Post], Seq[Comment]]

  def delete(userId: Id[User])(implicit ctx: AppContext): Kleisli[Future, Id[Comment], Boolean]
}

trait UsesCommentRepository {
  val commentRepository: CommentRepository
}
