package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.comment.{ Comment, UsesCommentRepository }
import net.petitviolet.anost.domain.post.{ Post, UsesPostRepository }
import net.petitviolet.anost.domain.support.Repository.NotFound
import net.petitviolet.anost.domain.user.{ User, UsesUserRepository }
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.{ AnostUseCase, In }

import scala.concurrent.Future
import scalaz._
import Scalaz._

trait GetPostUseCase extends AnostUseCase[GetPostArg, PostOutput]
    with UsesPostRepository with UsesUserRepository with UsesCommentRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    Post.findById(arg.postId) >=> findComments run postRepository map {
      case (p: Post, cus: Seq[(Comment, User)]) =>
        PostOutput.fromModelWithComment(p, cus)
    }
  }

  private def findUsers(implicit ctx: Ctx): Kleisli[Future, Seq[Comment], Seq[(Comment, User)]] =
    Kleisli { comments: Seq[Comment] =>
      import ctx._
      comments.map { _.userId } |> { userIds =>
        User.findByIds(userIds).run(userRepository)
          .map { users =>
            comments map { c => (c, users.find(_.id == c.userId).get) }
          }
      }
    }

  private def findComments(implicit ctx: Ctx): Kleisli[Future, Option[Post], (Post, Seq[(Comment, User)])] =
    Kleisli { postOpt: Option[Post] =>
      import ctx._
      postOpt.map { post =>
        (Comment.findByPost(post.id) >=> findUsers)
          .run(commentRepository).map { cus => (post, cus) }
      } getOrElse { Future.failed(NotFound("post not found")) }
    }
}

case class GetPostArg(postId: Id[Post]) extends In

object GetPostArg {
  def fromString(id: String): GetPostArg = GetPostArg(Id(id))
}

trait UsesGetPostUseCase {
  val getPostUseCase: GetPostUseCase
}
