package net.petitviolet.anost.usecase.comment

import net.petitviolet.anost.domain.comment.{ Comment, UsesCommentRepository }
import net.petitviolet.anost.domain.post.{ Post, UsesPostRepository }
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase._

import scala.concurrent.Future

trait DeleteCommentUseCase extends AnostAuthUseCase[DeleteCommentArg, BoolOut]
    with UsesCommentRepository {
  override protected def callWithUser(arg: DeleteCommentArg, user: User)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    Comment.delete(user.id, arg.commentId).run(commentRepository) map BoolOut.apply
  }
}

case class DeleteCommentArg(commentId: Id[Comment]) extends In

trait UsesDeleteCommentUseCase {
  val deleteCommentUseCase: DeleteCommentUseCase
}
