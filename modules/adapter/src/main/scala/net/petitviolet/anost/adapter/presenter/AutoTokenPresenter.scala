package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.contracts.Callback
import net.petitviolet.anost.support.{ MixInLogger, UsesLogger }
import net.petitviolet.anost.usecase.user.AuthTokenOutput
import spray.json._

import scala.concurrent.Promise

trait AuthTokenPresenter extends JsonPresenter[AuthTokenOutput] with UsesLogger {
  /**
   * @param promise
   * @return
   */
  override protected def callback(promise: Promise[JsonOutput]): Callback[AuthTokenOutput] = {
    new Callback[AuthTokenOutput] {
      override def onFailure(t: Throwable): Unit = promise.failure(t)
      override def onSuccess(result: AuthTokenOutput): Unit =
        promise.success(JsonOutput(Nil, result.toJson))
    }
  }
}

trait UsesAuthTokenPresenter {
  val authTokenPresenter: AuthTokenPresenter
}

trait MixInAuthTokenPresenter {
  val authTokenPresenter: AuthTokenPresenter = AuthTokenPresenterImpl
}

private object AuthTokenPresenterImpl extends AuthTokenPresenter with MixInLogger