package net.petitviolet.anost.adapter.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.petitviolet.anost.adapter.presenter.{ MixInPostPresenter, UsesPostPresenter }
import net.petitviolet.anost.adapter.repository.Database.Anost
import net.petitviolet.anost.adapter.repository.MixInPostRepository
import net.petitviolet.anost.support.{ MixInLogger, UsesLogger }
import net.petitviolet.anost.usecase.post.{ SavePostArg, SavePostUseCase, UsesSavePostUseCase }

trait PostController extends AnostController with UsesLogger
    with UsesSavePostUseCase with UsesPostPresenter {
  override def configKey: String = "post"
  override protected def route: Route = {
    pathPrefix("post") {
      (pathEnd & post & entity(as[SavePostArg])) { arg =>
        implicit val ctx = createContext(Anost.writeSession)
        val out = postPresenter.execute(savePostUseCase.execute(arg))
        onCompleteResponse("/post", out) { ok }
      }
    }
  }

}

object PostControllerImpl extends PostController with MixInLogger with MixInPostPresenter {
  override val savePostUseCase: SavePostUseCase = new SavePostUseCase with MixInPostRepository
}