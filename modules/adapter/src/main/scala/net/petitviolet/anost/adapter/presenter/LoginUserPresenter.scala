package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.contracts.Callback
import net.petitviolet.anost.support.{ MixInLogger, UsesLogger }
import net.petitviolet.anost.usecase.user.LoginResult
import spray.json._

import scala.concurrent.Promise

trait LoginUserPresenter extends JsonPresenter[LoginResult] with UsesLogger {
  /**
   * @param promise
   * @return
   */
  override protected def callback(promise: Promise[JsonOutput]): Callback[LoginResult] = {
    new Callback[LoginResult] {
      override def onFailure(t: Throwable): Unit = promise.failure(t)
      override def onSuccess(result: LoginResult): Unit = {
        // for format json
        val res = result.tokenOpt map { _.toJson } getOrElse { JsObject.empty }
        promise.success(JsonOutput(Nil, res))
      }
    }
  }
}

trait UsesLoginUserPresenter {
  val loginUserPresenter: LoginUserPresenter
}

trait MixInLoginUserPresenter {
  val loginUserPresenter: LoginUserPresenter = new LoginUserPresenter with MixInLogger
}
