package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.post.{ Post, UsesPostRepository }
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.{ AnostUseCase, In }

import scala.concurrent.Future

trait GetPostUseCase extends AnostUseCase[GetPostArg, PostOutput]
    with UsesPostRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    postRepository.resolve.run(arg.postId) map { PostOutput.fromModel }
  }
}

case class GetPostArg(postId: Id[Post]) extends In

object GetPostArg {
  def fromString(id: String): GetPostArg = GetPostArg(Id(id))
}

trait UsesGetPostUseCase {
  val getPostUseCase: GetPostUseCase
}
