package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.Validated
import net.petitviolet.anost.domain.post.{ Post, UsesPostRepository }
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.{ AnostUseCase, In, toFutureFailed }

import scala.concurrent.Future

trait SavePostUseCase extends AnostUseCase[SavePostArg, PostOutput]
    with UsesPostRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    val postV = arg.asPost
    postV.fold[Future[Out]](toFutureFailed, { post: Post =>
      Post.save(post).run(postRepository) map { PostOutput.fromModel }
    })
  }
}

case class SavePostArg(userId: Id[User], title: String, fileType: String, contents: String) extends In {
  private[usecase] def asPost: Validated[Post] = {
    Post.create(userId, title, fileType, contents)
  }
}

trait UsesSavePostUseCase {
  val savePostUseCase: SavePostUseCase
}
