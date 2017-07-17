package net.petitviolet.anost.adapter.controller

import akka.http.javadsl.server.PathMatchers
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.petitviolet.anost.adapter.presenter.{ MixInPostPresenter, UsesPostPresenter }
import net.petitviolet.anost.adapter.repository.Database.Anost
import net.petitviolet.anost.adapter.repository.MixInPostRepository
import net.petitviolet.anost.domain.post.{ Post, PostRepository }
import net.petitviolet.anost.support.{ Id, MixInLogger, UsesLogger }
import net.petitviolet.anost.usecase.post._

trait PostController extends AnostController with UsesLogger
    with UsesSavePostUseCase with UsesPostPresenter
    with UsesGetPostUseCase with UsesFindPostUseCase {
  override def configKey: String = "post"
  override protected def route: Route = {
    pathPrefix("post") {
      (pathEnd & post & entity(as[SavePostArg])) { arg =>
        implicit val ctx = createContext(Anost.writeSession)
        val out = postPresenter.execute(savePostUseCase.execute(arg))
        onCompleteResponse("/post", out) { ok }
      } ~
        (path(Segment) & get).as(GetPostArg.fromString _) { arg: GetPostArg =>
          implicit val ctx = createContext(Anost.readSession)
          val out = postPresenter.execute(getPostUseCase.execute(arg))
          onCompleteResponse(s"/post/${arg.postId}", out) { ok }
        }
    }
  }

}

object PostControllerImpl extends PostController with MixInLogger with MixInPostPresenter {
  override val savePostUseCase: SavePostUseCase = new SavePostUseCase with MixInPostRepository
  override val getPostUseCase: GetPostUseCase = new GetPostUseCase with MixInPostRepository
  override val findPostUseCase: FindPostUseCase = new FindPostUseCase with MixInPostRepository
}