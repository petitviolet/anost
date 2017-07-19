package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.Validated
import net.petitviolet.anost.domain.post.{ Post, UsesPostRepository }
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.usecase._

import scala.concurrent.Future

trait SavePostUseCase extends AnostAuthUseCase[SavePostArg, PostOutput]
    with UsesPostRepository {

  override protected def callWithUser(arg: SavePostArg, user: User)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    val postV = arg.asPost(user)
    postV.fold[Future[Out]](toFutureFailed, { post: Post =>
      Post.save(post).run(postRepository) map { PostOutput.fromModel }
    })
  }
}

case class SavePostArg(title: String, fileType: String, contents: String) extends In {
  private[usecase] def asPost(user: User): Validated[Post] = {
    Post.create(user.id, title, fileType, contents)
  }
}

trait UsesSavePostUseCase {
  val savePostUseCase: SavePostUseCase
}
