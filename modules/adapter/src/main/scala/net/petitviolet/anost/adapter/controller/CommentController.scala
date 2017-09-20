package net.petitviolet.anost.adapter.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.petitviolet.anost.adapter.presenter._
import net.petitviolet.anost.adapter.repository.Database.Anost
import net.petitviolet.anost.adapter.repository.{ MixInCommentRepository, MixInUserRepository }
import net.petitviolet.anost.support._
import net.petitviolet.anost.usecase.AuthArg
import net.petitviolet.anost.usecase.comment._

trait CommentController extends AnostController
    with UsesAddCommentUseCase with UsesCommentPresenter
    with UsesDeleteCommentUseCase with UsesBoolPresenter {
  override def configKey: String = "comment"

  override protected def route: Route = {
    pathPrefix("comment") {
      (pathEnd & post & withAuth & entity(as[AddCommentArg])) { (token, arg) =>
        implicit val ctx = createContext(Anost.writeSession, token)
        val out = commentPresenter.execute(addCommentUseCase.execute(AuthArg(token, arg)))
        onCompleteResponse("/comment", out) { ok }
      } ~
        (pathEnd & delete & withAuth & entity(as[DeleteCommentArg])) { (token, arg) =>
          implicit val ctx = createContext(Anost.writeSession, token)
          val out = boolPresenter.execute(deleteCommentUseCase.execute(AuthArg(token, arg)))
          onCompleteResponse("/comment", out) { ok }
        }
    }
  }

}

object CommentControllerImpl extends CommentController
    with MixInCommentRepository with MixInCommentPresenter with MixInLogger with MixInBoolPresenter {
  val addCommentUseCase: AddCommentUseCase =
    new AddCommentUseCase with MixInLogger with MixInCommentRepository with MixInUserRepository
  val deleteCommentUseCase: DeleteCommentUseCase =
    new DeleteCommentUseCase with MixInLogger with MixInCommentRepository with MixInUserRepository
}
