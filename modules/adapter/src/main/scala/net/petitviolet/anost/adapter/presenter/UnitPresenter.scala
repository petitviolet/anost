package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.{ AnostLogger, MixInLogger }
import net.petitviolet.anost.support.contracts.Callback
import net.petitviolet.anost.usecase.UnitOut

import scala.concurrent.Promise

trait UnitPresenter extends JsonPresenter[UnitOut] {
  /**
   * @param promise
   * @return
   */
  override protected def callback(promise: Promise[JsonOutput]): Callback[UnitOut] = {
    new Callback[UnitOut] {
      override def onSuccess(result: UnitOut): Unit = promise.success(JsonOutput.EMPTY)
      override def onFailure(t: Throwable): Unit = promise.failure(t)
    }
  }

}

trait UsesUnitPresenter {
  val unitPresenter: UnitPresenter
}

trait MixInUnitPresenter {
  val unitPresenter: UnitPresenter = UnitPresenterImpl
}

private object UnitPresenterImpl extends UnitPresenter with MixInLogger