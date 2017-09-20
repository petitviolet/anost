package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.MixInLogger
import net.petitviolet.anost.support.contracts.Callback
import net.petitviolet.anost.usecase.BoolOut

import scala.concurrent.Promise

trait BoolPresenter extends AnostPresenter[BoolOut, BooleanOutput] {

  /**
   * @param promise
   * @return
   */
  override protected def callback(promise: Promise[BooleanOutput]): Callback[BoolOut] = {
    new Callback[BoolOut] {
      override def onSuccess(result: BoolOut): Unit = promise.success(BooleanOutput(Nil, result.success))
      override def onFailure(t: Throwable): Unit = promise.failure(t)
    }
  }

}

trait UsesBoolPresenter {
  val boolPresenter: BoolPresenter
}

trait MixInBoolPresenter {
  val boolPresenter: BoolPresenter = BoolPresenterImpl
}

private object BoolPresenterImpl extends BoolPresenter with MixInLogger