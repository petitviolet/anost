package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.contracts.Callback
import net.petitviolet.anost.support.{ MixInLogger, UsesLogger }
import net.petitviolet.anost.usecase.user.{ MaybeUserOutput, UserOutput }
import spray.json._

import scala.concurrent.Promise

trait UserPresenter extends JsonPresenter[UserOutput] with UsesLogger {
  /**
   * @param promise
   * @return
   */
  override protected def callback(promise: Promise[JsonOutput]): Callback[UserOutput] = {
    new Callback[UserOutput] {
      override def onFailure(t: Throwable): Unit = promise.failure(t)
      override def onSuccess(result: UserOutput): Unit =
        promise.success(JsonOutput(Nil, result.toJson))
    }
  }
}

trait UsesUserPresenter {
  val userPresenter: UserPresenter
}

trait MixInUserPresenter {
  val userPresenter: UserPresenter = UserPresenterImpl
}

private object UserPresenterImpl extends UserPresenter with MixInLogger

trait MaybeUserPresenter extends JsonPresenter[MaybeUserOutput] with UsesLogger {
  /**
   * @param promise
   * @return
   */
  override protected def callback(promise: Promise[JsonOutput]): Callback[MaybeUserOutput] = {
    new Callback[MaybeUserOutput] {
      override def onFailure(t: Throwable): Unit = promise.failure(t)
      override def onSuccess(result: MaybeUserOutput): Unit =
        promise.success(JsonOutput(Nil, result.toJson))
    }
  }
}

trait UsesMaybeUserPresenter {
  val maybeUserPresenter: MaybeUserPresenter
}

trait MixInMaybeUserPresenter {
  val maybeUserPresenter: MaybeUserPresenter = MaybeUserPresenterImpl
}

private object MaybeUserPresenterImpl extends MaybeUserPresenter with MixInLogger