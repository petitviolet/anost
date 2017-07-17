package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.{ MixInLogger, UsesLogger }
import net.petitviolet.anost.support.contracts.Callback
import net.petitviolet.anost.usecase.post.{ PostOutput, PostsOutput }
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

trait PostsPresenter extends JsonPresenter[PostsOutput] with UsesLogger {
  /**
   * @param promise
   * @return
   */
  override protected def callback(promise: Promise[JsonOutput]): Callback[PostsOutput] =
    new Callback[PostsOutput] {
      override def onFailure(t: Throwable): Unit = promise.failure(t)

      override def onSuccess(result: PostsOutput): Unit =
        promise.success(JsonOutput(Nil, result.toJson))
    }
}

trait UsesPostsPresenter {
  val postsPresenter: PostsPresenter
}

trait MixInPostsPresenter {
  val postsPresenter: PostsPresenter = PostsPresenterImpl
}

private object PostsPresenterImpl extends PostsPresenter with MixInLogger
