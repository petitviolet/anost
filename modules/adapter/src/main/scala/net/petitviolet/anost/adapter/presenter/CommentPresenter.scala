package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.{ MixInLogger, UsesLogger }
import net.petitviolet.anost.support.contracts.Callback
import net.petitviolet.anost.usecase.comment.CommentOutput
import spray.json._

import scala.concurrent.Promise

trait CommentPresenter extends JsonPresenter[CommentOutput] with UsesLogger {
  /**
   * @param promise
   * @return
   */
  override protected def callback(promise: Promise[JsonOutput]): Callback[CommentOutput] =
    new Callback[CommentOutput] {
      override def onFailure(t: Throwable): Unit = promise.failure(t)

      override def onSuccess(result: CommentOutput): Unit =
        promise.success(JsonOutput(Nil, result.toJson))
    }
}

trait UsesCommentPresenter {
  val commentPresenter: CommentPresenter
}

trait MixInCommentPresenter {
  val commentPresenter: CommentPresenter = CommentPresenterImpl
}

private object CommentPresenterImpl extends CommentPresenter with MixInLogger