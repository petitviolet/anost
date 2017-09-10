package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.post.{ Post, UsesPostRepository }
import net.petitviolet.anost.domain.support.Repository.NotFound
import net.petitviolet.anost.domain.user.{ User, UserRepository, UsesUserRepository }
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.{ AnostUseCase, In, futureBind }

import scala.concurrent.Future
import scalaz._
import Scalaz._

trait GetPostUseCase extends AnostUseCase[GetPostArg, PostOutput]
    with UsesPostRepository with UsesUserRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    Post.findById(arg.postId) >=> findUsers run postRepository map {
      case (p: Post, us: Seq[User]) =>
        PostOutput.fromModelWithComment(p, us)
    }
  }

  private def findUsers(implicit ctx: Ctx): Kleisli[Future, Option[Post], (Post, Seq[User])] =
    Kleisli { postOpt =>
      import ctx._
      postOpt.map {
        post =>
          User.findByIds(post.comments.map { _.userId })
            .run(userRepository).map { users => (post, users) }
      }.getOrElse { Future.failed(NotFound("post not found")) }
    }
}

case class GetPostArg(postId: Id[Post]) extends In

object GetPostArg {
  def fromString(id: String): GetPostArg = GetPostArg(Id(id))
}

trait UsesGetPostUseCase {
  val getPostUseCase: GetPostUseCase
}
