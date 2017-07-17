package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.MixInLogger
import net.petitviolet.anost.support.contracts.Callback
import net.petitviolet.anost.usecase.user.UserOutput

import scala.concurrent.Promise
import spray.json._

trait UserPresenter extends JsonPresenter[UserOutput] with MixInLogger {
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

private object UserPresenterImpl extends UserPresenter