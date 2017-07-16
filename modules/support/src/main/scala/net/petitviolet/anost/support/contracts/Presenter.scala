package net.petitviolet.anost.support.contracts

import net.petitviolet.anost.support.UsesLogger

import scala.concurrent.{ Promise, Future }

trait Presenter[In, Out, Ctx <: AppContext] extends OutputPort[Callback[In], Ctx] with UsesLogger {

  override type Rendered = Out

  /**
   * @param call
   * @param ctx
   * @return
   */
  override def execute(call: => Callback[In] => Unit)(implicit ctx: Ctx): Future[Out] = {
    // embed the result of usecase into promise
    val promise = Promise[Out]()
    call(callback(promise))
    promise.future
  }

  /**
   * @param promise
   * @return
   */
  protected def callback(promise: Promise[Out]): Callback[In]

}
