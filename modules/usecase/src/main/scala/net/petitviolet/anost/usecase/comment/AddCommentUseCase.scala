package net.petitviolet.anost.usecase.comment

import net.petitviolet.anost.domain.comment.{ Comment, UsesCommentRepository }
import net.petitviolet.anost.domain.post.{ Post, UsesPostRepository }
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase._

import scala.concurrent.Future

trait AddCommentUseCase extends AnostAuthUseCase[AddCommentArg, CommentOutput]
    with UsesCommentRepository {
  override protected def callWithUser(arg: AddCommentArg, user: User)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    Comment.create(arg.postId, user.id, arg.sentence).fold[Future[Out]](
      toFutureFailed,
      { comment =>
        Comment.store(comment).run(commentRepository).map { _ =>
          CommentOutput.convert(comment, user)
        }
      }
    )
  }
}

case class AddCommentArg(postId: Id[Post], sentence: String) extends In

trait UsesAddCommentUseCase {
  val addCommentUseCase: AddCommentUseCase
}
