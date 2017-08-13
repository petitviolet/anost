package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.Validated
import net.petitviolet.anost.domain.post.{ Post, UsesPostRepository }
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase._

import scala.concurrent.Future

trait UpdatePostUseCase extends AnostAuthUseCase[UpdatePostArg, PostOutput]
    with UsesPostRepository {

  override protected def callWithUser(arg: UpdatePostArg, user: User)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    val postV = arg.asPost(user)
    postV.fold[Future[Out]](toFutureFailed, { post: Post =>
      Post.update(post).run(postRepository) map { PostOutput.fromModel }
    })
  }
}

case class UpdatePostArg(id: String, title: String, fileType: String, contents: String) extends In {
  private[usecase] def asPost(user: User): Validated[Post] = {
    Post.create(Id(id), user.id, title, fileType, contents)
  }
}

trait UsesUpdatePostUseCase {
  val updatePostUseCase: UpdatePostUseCase
}
