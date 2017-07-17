package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.{ MixInLogger, UsesLogger }
import net.petitviolet.anost.support.contracts.Callback
import net.petitviolet.anost.usecase.post.PostOutput
import spray.json._

import scala.concurrent.Promise

trait PostPresenter extends JsonPresenter[PostOutput] with UsesLogger {
  /**
   * @param promise
   * @return
   */
  override protected def callback(promise: Promise[JsonOutput]): Callback[PostOutput] =
    new Callback[PostOutput] {
      override def onFailure(t: Throwable): Unit = promise.failure(t)

      override def onSuccess(result: PostOutput): Unit =
        promise.success(JsonOutput(Nil, result.toJson))
    }
}

trait UsesPostPresenter {
  val postPresenter: PostPresenter
}

trait MixInPostPresenter {
  val postPresenter: PostPresenter = PostPresenterImpl
}

private object PostPresenterImpl extends PostPresenter with MixInLogger