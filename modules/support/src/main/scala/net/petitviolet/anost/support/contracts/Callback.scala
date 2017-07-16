package net.petitviolet.anost.support.contracts

/**
 * Output port
 * @tparam Result
 */
trait Callback[Result] {

  def onSuccess(result: Result): Unit

  def onFailure(t: Throwable): Unit
}
