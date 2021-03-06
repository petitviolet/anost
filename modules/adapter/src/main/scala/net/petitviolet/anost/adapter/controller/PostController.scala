package net.petitviolet.anost.adapter.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.petitviolet.anost.adapter.presenter._
import net.petitviolet.anost.adapter.repository.Database.Anost
import net.petitviolet.anost.adapter.repository.{ MixInCommentRepository, MixInPostRepository, MixInUserRepository }
import net.petitviolet.anost.support._
import net.petitviolet.anost.usecase.AuthArg
import net.petitviolet.anost.usecase.post._

trait PostController extends AnostController with UsesLogger
    with UsesSavePostUseCase with UsesUpdatePostUseCase with UsesPostPresenter with UsesPostsPresenter
    with UsesGetPostUseCase with UsesFindPostUseCase {
  override def configKey: String = "post"

  override protected def route: Route = {
    pathPrefix("post") {
      (pathEnd & post & withAuth & entity(as[SavePostArg])) { (token, arg) =>
        implicit val ctx = writeContext(token)
        val out = postPresenter.execute(savePostUseCase.execute(AuthArg(token, arg)))
        onCompleteResponse("/post", out) { ok }
      } ~
        (pathEnd & put & withAuth & entity(as[UpdatePostArg])) { (token, arg) =>
          implicit val ctx = writeContext(token)
          val out = postPresenter.execute(updatePostUseCase.execute(AuthArg(token, arg)))
          onCompleteResponse("/post", out) { ok }
        } ~
        (get &
          ((path("all") & pathEnd & provide(AllPost))
            | (pathEnd & parameters('user.as[String].?, 'title.as[String].?)).as(FindPostArg.apply _))) {
              arg: FindPostArg =>
                implicit val ctx = readContext()
                val out = postsPresenter.execute(findPostUseCase.execute(arg))
                onCompleteResponse(s"/post?$arg", out) { ok }
            } ~
            (path(Segment) & pathEnd & get).as(GetPostArg.fromString _) { arg: GetPostArg =>
              implicit val ctx = readContext()
              val out = postPresenter.execute(getPostUseCase.execute(arg))
              onCompleteResponse(s"/post/${arg.postId}", out) { ok }
            }
    }
  }
}

object PostControllerImpl extends PostController with MixInLogger
    with MixInPostPresenter
    with MixInPostsPresenter {
  override val savePostUseCase: SavePostUseCase = new SavePostUseCase with MixInPostRepository with MixInUserRepository with MixInLogger
  override val updatePostUseCase: UpdatePostUseCase = new UpdatePostUseCase with MixInPostRepository with MixInUserRepository with MixInLogger
  override val getPostUseCase: GetPostUseCase = new GetPostUseCase with MixInPostRepository with MixInUserRepository with MixInCommentRepository
  override val findPostUseCase: FindPostUseCase = new FindPostUseCase with MixInPostRepository
}